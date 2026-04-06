import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { OutcomeEditComponent } from './components/outcome-edit/outcome-edit';
import { DashboardComponent } from './components/dashboard/dashboard';
import { adminGuard } from './guards/admin';

export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'login', component: LoginComponent },
  { path: 'outcomes/edit/:id', component: OutcomeEditComponent, canActivate: [adminGuard] }
];