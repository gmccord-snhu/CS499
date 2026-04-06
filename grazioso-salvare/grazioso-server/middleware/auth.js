function requireAdmin(req, res, next) {
  console.log('requireAdmin session:', req.session);
  console.log('requireAdmin user:', req.session?.user);

  if (!req.session.user || req.session.user.role !== 'admin') {
    return res.status(403).json({ message: 'Admin access required' });
  }
  next();
}

module.exports = { requireAdmin };