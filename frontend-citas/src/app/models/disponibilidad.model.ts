export interface Disponibilidad {
  id: number;
  asesor: string;
  fecha: string;
  horaInicio: string;
  horaFin: string;
  estado: 'DISPONIBLE' | 'OCUPADO' | 'BLOQUEADO';
}