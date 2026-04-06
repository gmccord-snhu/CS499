const express = require('express');
const bcrypt = require('bcryptjs');
const User = require('../models/user.model');

const router = express.Router();

router.post('/login', async (req, res) => {
  try {
    const { username, password } = req.body;

    const user = await User.findOne({ username });

    if (!user) {
      return res.status(401).json({ message: 'Invalid username or password' });
    }

    const passwordMatch = await bcrypt.compare(password, user.passwordHash);

    if (!passwordMatch) {
      return res.status(401).json({ message: 'Invalid username or password' });
    }

    req.session.user = {
      id: user._id,
      username: user.username,
      role: user.role
    };

    req.session.save((err) => {
      if (err) {
        console.error('Session save error:', err);
        return res.status(500).json({ message: 'Login succeeded but session failed to save' });
      }

      return res.json({
        message: 'Login successful',
        user: req.session.user
      });
    });
  } catch (error) {
    console.error('Login error:', error);
    return res.status(500).json({ message: 'Server error during login' });
  }
});

router.post('/logout', (req, res) => {
  req.session.destroy((err) => {
    if (err) {
      return res.status(500).json({ message: 'Logout failed' });
    }

    res.clearCookie('connect.sid');
    return res.json({ message: 'Logged out' });
  });
});

router.get('/me', (req, res) => {
  if (!req.session.user) {
    return res.status(401).json({ message: 'Not logged in' });
  }

  return res.json(req.session.user);
});

module.exports = router;