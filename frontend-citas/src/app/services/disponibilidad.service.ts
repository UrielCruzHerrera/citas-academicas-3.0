import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Disponibilidad } from '../models/disponibilidad.model';

@Injectable({ providedIn: 'root' })
export class DisponibilidadService {

  private readonly apiUrl = '/api/disponibilidades';

  constructor(private http: HttpClient) {}

  listar(): Observable<Disponibilidad[]> {
    return this.http.get<Disponibilidad[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Disponibilidad> {
    return this.http.get<Disponibilidad>(`${this.apiUrl}/${id}`);
  }
}