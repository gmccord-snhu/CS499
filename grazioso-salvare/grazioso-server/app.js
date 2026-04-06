const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const session = require('express-session');

const outcomeRoutes = require('./routes/outcome.routes');
const animalRoutes = require('./routes/animal.routes');
const authRoutes = require('./routes/auth.routes');

const app = express();

app.use(cors({
  origin: 'http://localhost:4200',
  credentials: true
}));

app.use(express.json());

app.use(session({
  secret: 'change-this-to-a-long-random-secret',
  resave: false,
  saveUninitialized: false,
  cookie: {
    httpOnly: true,
    secure: false,
    maxAge: 1000 * 60 * 60
  }
}));

mongoose.connect('mongodb://localhost:27017/AAC')
  .then(() => console.log('MongoDB connected'))
  .catch(err => console.log(err));

app.use('/api/auth', authRoutes);
app.use('/api/outcomes', outcomeRoutes);
app.use('/api/animals', animalRoutes);

app.listen(3000, () => console.log('Server running on port 3000'));