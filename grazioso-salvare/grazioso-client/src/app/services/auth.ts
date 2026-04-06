import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isAdmin = false;
  isAdmin$ = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient) {}

  setAdminStatus(value: boolean): void {
    this.isAdmin = value;
    this.isAdmin$.next(value);
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(
      'http://localhost:3000/api/auth/login',
      { username, password },
      { withCredentials: true }
    ).pipe(
      tap((response: any) => {
        this.setAdminStatus(response?.user?.role === 'admin');
      })
    );
  }

  getCurrentUser(): Observable<any> {
    return this.http.get<any>(
      'http://localhost:3000/api/auth/me',
      { withCredentials: true }
    ).pipe(
      tap((user: any) => {
        this.setAdminStatus(user?.role === 'admin');
      })
    );
  }

  logout(): Observable<any> {
    return this.http.post<any>(
      'http://localhost:3000/api/auth/logout',
      {},
      { withCredentials: true }
    ).pipe(
      tap(() => {
        this.setAdminStatus(false);
      })
    );
  }
}