const express = require('express');
const router = express.Router();
const Outcome = require('../models/outcome.model');

function buildRescueCriteria(type) {
  const criteria = {
    WATER: {
      breeds: ['Labrador Retriever', 'Chesapeake Bay Retriever', 'Newfoundland'],
      sex: 'Female',
      minAge: 26,
      maxAge: 156
    },
    MOUNTAIN: {
      breeds: ['German Shepherd', 'Alaskan Malamute', 'Old English Sheepdog', 'Siberian Husky', 'Rottweiler'],
      sex: 'Male',
      minAge: 26,
      maxAge: 156
    },
    DISASTER: {
      breeds: ['Doberman Pinscher', 'German Shepherd', 'Golden Retriever', 'Bloodhound', 'Rottweiler'],
      sex: 'Male',
      minAge: 20,
      maxAge: 300
    }
  };

  return criteria[type] || null;
}
function parseFlexibleDate(dateString) {
  if (!dateString || typeof dateString !== 'string' || dateString === 'N/A') {
    return null;
  }

  // Supports formats like:
  // 5/5/2020 0:00
  // 5/5/2025 9:55
  // 04/25/2025 12:00:00 AM
  // 2025-05-05T10:08:00

  // First try native parsing for ISO-like values
  const nativeParsed = new Date(dateString);
  if (!isNaN(nativeParsed.getTime())) {
    return nativeParsed;
  }

  // Try M/D/YYYY H:MM or MM/DD/YYYY HH:MM
  let match = dateString.match(
    /^(\d{1,2})\/(\d{1,2})\/(\d{4})\s+(\d{1,2}):(\d{2})$/
  );

  if (match) {
    const month = parseInt(match[1], 10) - 1;
    const day = parseInt(match[2], 10);
    const year = parseInt(match[3], 10);
    const hour = parseInt(match[4], 10);
    const minute = parseInt(match[5], 10);

    const parsed = new Date(year, month, day, hour, minute);
    return isNaN(parsed.getTime()) ? null : parsed;
  }

  // Try MM/DD/YYYY HH:MM:SS AM/PM
  match = dateString.match(
    /^(\d{1,2})\/(\d{1,2})\/(\d{4})\s+(\d{1,2}):(\d{2}):(\d{2})\s+(AM|PM)$/i
  );

  if (match) {
    const month = parseInt(match[1], 10) - 1;
    const day = parseInt(match[2], 10);
    const year = parseInt(match[3], 10);
    let hour = parseInt(match[4], 10);
    const minute = parseInt(match[5], 10);
    const second = parseInt(match[6], 10);
    const ampm = match[7].toUpperCase();

    if (ampm === 'PM' && hour !== 12) hour += 12;
    if (ampm === 'AM' && hour === 12) hour = 0;

    const parsed = new Date(year, month, day, hour, minute, second);
    return isNaN(parsed.getTime()) ? null : parsed;
  }

  return null;
}

function calculateAgeInWeeks(dateOfBirth, outcomeDate) {
  const dob = parseFlexibleDate(dateOfBirth);
  const outcome = parseFlexibleDate(outcomeDate);

  if (!dob || !outcome) {
    return null;
  }

  const diffMs = outcome.getTime() - dob.getTime();

  if (diffMs < 0) {
    return null;
  }

  return Math.floor(diffMs / (1000 * 60 * 60 * 24 * 7));
}

async function getFilteredAnimals(type) {
  const criteria = buildRescueCriteria(type);

  if (!criteria) {
    return { error: 'invalid rescue type', status: 400 };
  }

  const animals = await Outcome.find({
    'Primary Breed': { $regex: criteria.breeds.join('|'), $options: 'i' },
    Sex: { $regex: `^${criteria.sex}$`, $options: 'i' }
  });

  const withAge = animals.map((animal) => {
    const ageWeeks = calculateAgeInWeeks(
      animal['Date of Birth'],
      animal['Outcome Date']
    );

    return {
      ...animal.toObject(),
      calculatedAgeWeeks: ageWeeks
    };
  });

  const filtered = withAge.filter((animal) =>
    animal.calculatedAgeWeeks !== null &&
    animal.calculatedAgeWeeks >= criteria.minAge &&
    animal.calculatedAgeWeeks <= criteria.maxAge
  );

  return {
    criteria,
    animals,
    withAge,
    filtered
  };
}

// App route: returns only array for Angular table
router.post('/filter', async (req, res) => {
  try {
    const type = req.body?.type;

    if (!type) {
      return res.status(400).json({ error: 'type is required' });
    }

    const result = await getFilteredAnimals(type);

    if (result.error) {
      return res.status(result.status).json({ error: result.error });
    }

    res.json(result.filtered.slice(0, 10));
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Debug route: returns metadata and samples for Postman/debugging
router.post('/filter-debug', async (req, res) => {
  try {
    const type = req.body?.type;

    if (!type) {
      return res.status(400).json({ error: 'type is required' });
    }

    const result = await getFilteredAnimals(type);

    if (result.error) {
      return res.status(result.status).json({ error: result.error });
    }

    result.withAge.slice(0, 20).forEach((animal) => {
      console.log(
        `Name: ${animal.Name || 'N/A'} | ` +
        `Breed: ${animal['Primary Breed'] || 'N/A'} | ` +
        `Sex: ${animal.Sex || 'N/A'} | ` +
        `DOB: ${animal['Date of Birth'] || 'N/A'} | ` +
        `Outcome: ${animal['Outcome Date'] || 'N/A'} | ` +
        `AgeWeeks: ${animal.calculatedAgeWeeks}`
      );
    });

    res.json({
      type,
      criteria: result.criteria,
      totalBreedAndSexMatches: result.animals.length,
      totalAfterAgeFilter: result.filtered.length,
      sampleBeforeAgeFilter: result.withAge.slice(0, 10),
      sample: result.filtered.slice(0, 10)
    });
  } catch (err) {
    console.error('FILTER DEBUG ERROR:', err);
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;