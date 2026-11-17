import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  imports: [FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  name = '';
  email = '';
  password = '';
  message = signal('');
  error = signal('');

  constructor(private http: HttpClient) {}

  register() {
    this.message.set('');
    this.error.set('');
    this.http.post<any>('http://localhost:8080/api/users/register', {
      name: this.name,
      email: this.email,
      password: this.password,
      role: 'CUSTOMER'
    }).subscribe({
      next: (user) => this.message.set('Registered! Your user id is ' + user.id),
      error: () => this.error.set('Registration failed. The email may already exist.')
    });
  }
}