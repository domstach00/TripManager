import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RabbitmqMonitoringComponent } from './rabbitmq-monitoring.component';

const routes: Routes = [
  {
    path: 'rabbitmq',
    component: RabbitmqMonitoringComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RabbitmqMonitoringRoutingModule { }
