import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SolicitarComponent } from './solicitar.component';

describe('SolicitarComponent', () => {
  let component: SolicitarComponent;
  let fixture: ComponentFixture<SolicitarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SolicitarComponent]
    });
    fixture = TestBed.createComponent(SolicitarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
