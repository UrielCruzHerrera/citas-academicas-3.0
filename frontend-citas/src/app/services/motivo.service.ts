import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Motivo } from '../models/motivo.model';

@Injectable({ providedIn: 'root' })
export class MotivoService {

  private readonly apiUrl = '/api/motivos';

  constructor(private http: HttpClient) {}

  listar(): Observable<Motivo[]> {
    return this.http.get<Motivo[]>(this.apiUrl);
  }
}