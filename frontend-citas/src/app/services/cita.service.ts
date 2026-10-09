import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { CitaRequest, CitaResponse } from '../models/cita.model';

@Injectable({ providedIn: 'root' })
export class CitaService {

  private readonly apiUrl = '/api/citas';

  constructor(private http: HttpClient) {}

  listarPorEstudiante(idEstudiante: number): Observable<CitaResponse[]> {
    return this.http
      .get<CitaResponse[]>(`${this.apiUrl}?idEstudiante=${idEstudiante}`)
      .pipe(catchError(this.manejarError));
  }

  crear(request: CitaRequest): Observable<CitaResponse> {
    return this.http
      .post<CitaResponse>(this.apiUrl, request)
      .pipe(catchError(this.manejarError));
  }

  cancelar(idCita: number, idUsuario: number): Observable<void> {
    return this.http
      .delete<void>(`${this.apiUrl}/${idCita}?idUsuario=${idUsuario}`)
      .pipe(catchError(this.manejarError));
  }

  private manejarError(error: HttpErrorResponse) {
    let mensaje = 'Ocurrió un error inesperado.';
    if (error.status === 409) {
      mensaje = error.error?.message ?? 'Conflicto: el recurso ya no está disponible.';
    } else if (error.status === 404) {
      mensaje = error.error?.message ?? 'Recurso no encontrado.';
    } else if (error.status === 400) {
      mensaje = 'Datos inválidos. Revisa el formulario.';
    } else if (error.status === 0) {
      mensaje = 'No se pudo conectar con el servidor.';
    }
    return throwError(() => new Error(mensaje));
  }
}