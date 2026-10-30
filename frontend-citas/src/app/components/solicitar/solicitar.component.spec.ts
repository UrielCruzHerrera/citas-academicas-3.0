import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { SolicitarComponent } from './solicitar.component';
import { DisponibilidadService } from '../../services/disponibilidad.service';
import { MotivoService } from '../../services/motivo.service';
import { CitaService } from '../../services/cita.service';

describe('SolicitarComponent', () => {
  let component: SolicitarComponent;
  let fixture: ComponentFixture<SolicitarComponent>;
  let citaServiceSpy: any;
  let routerSpy: any;

  beforeEach(async () => {
    const dispSpy = jasmine.createSpyObj('DisponibilidadService', ['listar']);
    const motivoSpy = jasmine.createSpyObj('MotivoService', ['listar']);
    citaServiceSpy = jasmine.createSpyObj('CitaService', ['crear']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    dispSpy.listar.and.returnValue(of([]));
    motivoSpy.listar.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      declarations: [SolicitarComponent],
      imports: [FormsModule, HttpClientTestingModule],
      providers: [
        { provide: DisponibilidadService, useValue: dispSpy },
        { provide: MotivoService, useValue: motivoSpy },
        { provide: CitaService, useValue: citaServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SolicitarComponent);
    component = fixture.componentInstance;
  });

  it('debe crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('no debe enviar si faltan campos', () => {
    component.idDisponibilidadSeleccionada = null;
    component.idMotivoSeleccionado = null;
    component.enviar();
    expect(citaServiceSpy.crear).not.toHaveBeenCalled();
    expect(component.error).toContain('Debes seleccionar');
  });

  it('debe crear cita con datos válidos', () => {
    const citaMock = {
      id: 1, estudiante: 'Ana', asesor: 'Luis',
      fechaHora: '2026-10-10 09:00-09:30', motivo: 'Asesoría',
      estado: 'SOLICITADA' as const, fechaSolicitud: '2026-10-09T10:00:00'
    };

    citaServiceSpy.crear.and.returnValue(of(citaMock));
    component.idDisponibilidadSeleccionada = 1;
    component.idMotivoSeleccionado = 1;
    component.enviar();

    expect(citaServiceSpy.crear).toHaveBeenCalled();
    expect(component.exito).toContain('Cita #1 creada');
  });
});