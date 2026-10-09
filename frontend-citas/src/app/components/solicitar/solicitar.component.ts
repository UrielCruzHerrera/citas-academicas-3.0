import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Disponibilidad } from '../../models/disponibilidad.model';
import { Motivo } from '../../models/motivo.model';
import { CitaRequest } from '../../models/cita.model';
import { DisponibilidadService } from '../../services/disponibilidad.service';
import { MotivoService } from '../../services/motivo.service';
import { CitaService } from '../../services/cita.service';

@Component({
  selector: 'app-solicitar',
  templateUrl: './solicitar.component.html',
  styleUrls: ['./solicitar.component.css']
})
export class SolicitarComponent implements OnInit {

  disponibilidades: Disponibilidad[] = [];
  motivos: Motivo[] = [];

  idDisponibilidadSeleccionada: number | null = null;
  idMotivoSeleccionado: number | null = null;
  observaciones = '';

  cargando = false;
  enviando = false;
  error: string | null = null;
  exito: string | null = null;

  private readonly idEstudianteDemo = 1;

  constructor(
    private disponibilidadService: DisponibilidadService,
    private motivoService: MotivoService,
    private citaService: CitaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarCatalogos();
  }

  cargarCatalogos(): void {
    this.cargando = true;
    this.error = null;

    this.disponibilidadService.listar().subscribe({
      next: (data) => {
        this.disponibilidades = data;
        this.cargando = false;
      },
      error: (err) => {
        this.error = err.message;
        this.cargando = false;
      }
    });

    this.motivoService.listar().subscribe({
      next: (data) => (this.motivos = data),
      error: (err) => (this.error = err.message)
    });
  }

  enviar(): void {
    if (!this.idDisponibilidadSeleccionada || !this.idMotivoSeleccionado) {
      this.error = 'Debes seleccionar un horario y un motivo.';
      return;
    }

    const request: CitaRequest = {
      idEstudiante: this.idEstudianteDemo,
      idDisponibilidad: this.idDisponibilidadSeleccionada,
      idMotivo: this.idMotivoSeleccionado,
      observaciones: this.observaciones || undefined
    };

    this.enviando = true;
    this.error = null;
    this.exito = null;

    this.citaService.crear(request).subscribe({
      next: (cita) => {
        this.enviando = false;
        this.exito = `Cita #${cita.id} creada correctamente. Redirigiendo...`;
        setTimeout(() => this.router.navigate(['/mis-citas']), 1500);
      },
      error: (err) => {
        this.enviando = false;
        this.error = err.message;
      }
    });
  }
}