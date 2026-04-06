const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');
const User = require('./models/user.model');

async function seedAdmin() {
  try {
    await mongoose.connect('mongodb://localhost:27017/AAC');

    const existingUser = await User.findOne({ username: 'admin' });
    if (existingUser) {
      console.log('Admin already exists');
      process.exit(0);
    }

    const passwordHash = await bcrypt.hash('admin123', 10);

    await User.create({
      username: 'admin',
      passwordHash,
      role: 'admin'
    });

    console.log('Admin user created');
    process.exit(0);
  } catch (error) {
    console.error(error);
    process.exit(1);
  }
}

seedAdmin();