const express = require('express');
const router = express.Router();
const Outcome = require('../models/outcome.model');
const { calculateAgeInWeeks } = require('../utils/age-utils');

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

async function getFilteredAnimals(type) {
  console.log(`Filtering animals for type: ${type}`);
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

    res.json(result.filtered);
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