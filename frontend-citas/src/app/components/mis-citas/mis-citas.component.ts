import { Component, OnInit } from '@angular/core';
import { CitaResponse } from '../../models/cita.model';
import { CitaService } from '../../services/cita.service';

@Component({
  selector: 'app-mis-citas',
  templateUrl: './mis-citas.component.html',
  styleUrls: ['./mis-citas.component.css']
})
export class MisCitasComponent implements OnInit {

  citas: CitaResponse[] = [];
  cargando = false;
  error: string | null = null;
  exito: string | null = null;

  private readonly idEstudianteDemo = 1;

  constructor(private citaService: CitaService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.error = null;

    this.citaService.listarPorEstudiante(this.idEstudianteDemo).subscribe({
      next: (data) => {
        this.citas = data;
        this.cargando = false;
      },
      error: (err) => {
        this.error = err.message;
        this.cargando = false;
      }
    });
  }

  cancelar(idCita: number): void {
    if (!confirm(`¿Cancelar la cita #${idCita}?`)) return;

    this.error = null;
    this.exito = null;

    this.citaService.cancelar(idCita, this.idEstudianteDemo).subscribe({
      next: () => {
        this.exito = `Cita #${idCita} cancelada correctamente.`;
        this.cargar();
      },
      error: (err) => (this.error = err.message)
    });
  }

  esCancelable(cita: CitaResponse): boolean {
    return cita.estado === 'SOLICITADA' || cita.estado === 'CONFIRMADA';
  }
}