import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { MisCitasComponent } from './mis-citas.component';
import { CitaService } from '../../services/cita.service';
import { CitaResponse } from '../../models/cita.model';

describe('MisCitasComponent', () => {
  let component: MisCitasComponent;
  let fixture: ComponentFixture<MisCitasComponent>;
  let citaServiceSpy: any;

  const mockCitas: CitaResponse[] = [
    { id: 1, estudiante: 'Ana', asesor: 'Luis',
      fechaHora: '2026-10-10 09:00-09:30', motivo: 'Asesoría',
      estado: 'SOLICITADA', fechaSolicitud: '2026-10-09T10:00:00' },
    { id: 2, estudiante: 'Ana', asesor: 'Luis',
      fechaHora: '2026-10-10 09:30-10:00', motivo: 'Duda',
      estado: 'CANCELADA', fechaSolicitud: '2026-10-09T11:00:00' }
  ];

  beforeEach(async () => {
    citaServiceSpy = jasmine.createSpyObj('CitaService', ['listarPorEstudiante', 'cancelar']);
    citaServiceSpy.listarPorEstudiante.and.returnValue(of(mockCitas));
    citaServiceSpy.cancelar.and.returnValue(of(void 0));

    await TestBed.configureTestingModule({
      declarations: [MisCitasComponent],
      imports: [HttpClientTestingModule],
      providers: [{ provide: CitaService, useValue: citaServiceSpy }]
    }).compileComponents();

    fixture = TestBed.createComponent(MisCitasComponent);
    component = fixture.componentInstance;
  });

  it('debe crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debe cargar citas al inicializar', () => {
    fixture.detectChanges();
    expect(citaServiceSpy.listarPorEstudiante).toHaveBeenCalled();
    expect(component.citas.length).toBe(2);
    expect(component.cargando).toBeFalse();
  });

  it('esCancelable debe retornar true solo para SOLICITADA o CONFIRMADA', () => {
    expect(component.esCancelable(mockCitas[0])).toBeTrue();
    expect(component.esCancelable(mockCitas[1])).toBeFalse();
  });
});