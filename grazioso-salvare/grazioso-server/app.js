const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const outcomeRoutes = require('./routes/outcome.routes');
const animalRoutes = require('./routes/animal.routes');

const app = express();

app.use(cors());
app.use(express.json());

// MongoDB connection
mongoose.connect('mongodb://localhost:27017/AAC')
  .then(() => console.log('MongoDB connected'))
  .catch(err => console.log(err));

// Routes
app.use('/api/outcomes', outcomeRoutes);
app.use('/api/animals', animalRoutes);

// Start server
app.listen(3000, () => console.log('Server running on port 3000'));