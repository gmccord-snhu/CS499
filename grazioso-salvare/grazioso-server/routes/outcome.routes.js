const express = require('express');
const router = express.Router();
const Outcome = require('../models/outcome.model');

// Create
router.post('/', async (req, res) => {
  try {
    const newOutcome = new Outcome(req.body);
    const savedOutcome = await newOutcome.save();
    res.status(201).json(savedOutcome);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

// Read all
router.get('/', async (req, res) => {
  try {
    const outcomes = await Outcome.find({}).limit(50);
    res.json(outcomes);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Read one
router.get('/:id', async (req, res) => {
  try {
    const outcome = await Outcome.findById(req.params.id);

    if (!outcome) {
      return res.status(404).json({ error: 'Record not found' });
    }

    res.json(outcome);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Update
router.put('/:id', async (req, res) => {
  try {
    const updatedOutcome = await Outcome.findByIdAndUpdate(
      req.params.id,
      req.body,
      { new: true, runValidators: true }
    );

    if (!updatedOutcome) {
      return res.status(404).json({ error: 'Record not found' });
    }

    res.json(updatedOutcome);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

// Delete
router.delete('/:id', async (req, res) => {
  try {
    const deletedOutcome = await Outcome.findByIdAndDelete(req.params.id);

    if (!deletedOutcome) {
      return res.status(404).json({ error: 'Record not found' });
    }

    res.json({ message: 'Record deleted successfully' });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;