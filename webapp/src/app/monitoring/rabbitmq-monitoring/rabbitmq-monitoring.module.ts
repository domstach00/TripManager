import { NgModule } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { RabbitmqMonitoringRoutingModule } from './rabbitmq-monitoring-routing.module';
import { RabbitmqMonitoringComponent } from './rabbitmq-monitoring.component';
import { SharedModule } from '../../shared/shared.module';


@NgModule({
  declarations: [
    RabbitmqMonitoringComponent
  ],
  imports: [
    RabbitmqMonitoringRoutingModule,
    SharedModule,
    MatTableModule,
    MatCardModule,
    MatProgressSpinnerModule,
    NgxChartsModule
  ]
})
export class RabbitmqMonitoringModule { }
