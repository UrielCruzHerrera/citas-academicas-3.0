import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of, throwError } from 'rxjs';
import { HorariosComponent } from './horarios.component';
import { DisponibilidadService } from '../../services/disponibilidad.service';
import { Disponibilidad } from '../../models/disponibilidad.model';

describe('HorariosComponent', () => {
  let component: HorariosComponent;
  let fixture: ComponentFixture<HorariosComponent>;
  let disponibilidadServiceSpy: any;

  const mockDisponibilidades: Disponibilidad[] = [
    { id: 1, asesor: 'Luis Asesor', fecha: '2026-10-10',
      horaInicio: '09:00', horaFin: '09:30', estado: 'DISPONIBLE' },
    { id: 2, asesor: 'Luis Asesor', fecha: '2026-10-10',
      horaInicio: '09:30', horaFin: '10:00', estado: 'DISPONIBLE' }
  ];

  beforeEach(async () => {
    disponibilidadServiceSpy = jasmine.createSpyObj('DisponibilidadService', ['listar']);
    disponibilidadServiceSpy.listar.and.returnValue(of(mockDisponibilidades));

    await TestBed.configureTestingModule({
      declarations: [HorariosComponent],
      imports: [HttpClientTestingModule],
      providers: [{ provide: DisponibilidadService, useValue: disponibilidadServiceSpy }]
    }).compileComponents();

    fixture = TestBed.createComponent(HorariosComponent);
    component = fixture.componentInstance;
  });

  it('debe crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debe cargar disponibilidades al inicializar', () => {
    fixture.detectChanges();
    expect(disponibilidadServiceSpy.listar).toHaveBeenCalled();
    expect(component.disponibilidades.length).toBe(2);
    expect(component.cargando).toBeFalse();
  });

  it('debe mostrar error si falla el servicio', () => {
    disponibilidadServiceSpy.listar.and.returnValue(
      throwError(() => new Error('Error de prueba'))
    );
    fixture.detectChanges();
    expect(component.error).toContain('No se pudieron cargar');
  });
});