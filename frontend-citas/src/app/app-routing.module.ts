import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HorariosComponent } from './components/horarios/horarios.component';
import { SolicitarComponent } from './components/solicitar/solicitar.component';
import { MisCitasComponent } from './components/mis-citas/mis-citas.component';

const routes: Routes = [
  { path: '', redirectTo: '/horarios', pathMatch: 'full' },
  { path: 'horarios', component: HorariosComponent },
  { path: 'solicitar', component: SolicitarComponent },
  { path: 'mis-citas', component: MisCitasComponent },
  { path: '**', redirectTo: '/horarios' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }