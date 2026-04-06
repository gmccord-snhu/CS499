import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth';

export const adminGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  console.log('AdminGuard check:', auth.isAdmin);

  if (auth.isAdmin) {
    return true;
  }

  console.warn('Access denied - not admin');
  router.navigate(['/']); // redirect to dashboard
  return false;
};