export interface CitaRequest {
  idEstudiante: number;
  idDisponibilidad: number;
  idMotivo: number;
  observaciones?: string;
}

export interface CitaResponse {
  id: number;
  estudiante: string;
  asesor: string;
  fechaHora: string;
  motivo: string;
  estado: 'SOLICITADA' | 'CONFIRMADA' | 'CANCELADA' | 'ATENDIDA';
  fechaSolicitud: string;
}