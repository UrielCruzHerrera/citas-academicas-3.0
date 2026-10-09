import { Component, OnInit } from '@angular/core';
import { Disponibilidad } from '../../models/disponibilidad.model';
import { DisponibilidadService } from '../../services/disponibilidad.service';

@Component({
  selector: 'app-horarios',
  templateUrl: './horarios.component.html',
  styleUrls: ['./horarios.component.css']
})
export class HorariosComponent implements OnInit {

  disponibilidades: Disponibilidad[] = [];
  cargando = false;
  error: string | null = null;

  constructor(private disponibilidadService: DisponibilidadService) {}

  ngOnInit(): void {
    this.cargarDisponibilidades();
  }

  cargarDisponibilidades(): void {
    this.cargando = true;
    this.error = null;

    this.disponibilidadService.listar().subscribe({
      next: (data) => {
        this.disponibilidades = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al cargar disponibilidades', err);
        this.error = 'No se pudieron cargar los horarios. Verifica que el backend esté corriendo.';
        this.cargando = false;
      }
    });
  }
}