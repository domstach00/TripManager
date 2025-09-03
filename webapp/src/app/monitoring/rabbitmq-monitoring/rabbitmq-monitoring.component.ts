import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { Observable, timer } from 'rxjs';
import { finalize, switchMap, tap } from 'rxjs/operators';
import { QueueInfo } from './queue-info.model';
import { RabbitMqMonitoringService } from './rabbitmq-monitoring.service';
import { DlqMessage } from './dlq.message';

@Component({
    selector: 'app-rabbitmq-monitoring',
    templateUrl: './rabbitmq-monitoring.component.html',
    styleUrls: ['./rabbitmq-monitoring.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class RabbitmqMonitoringComponent implements OnInit {

  queues$: Observable<QueueInfo[]>;
  displayedColumns: string[] = ['name', 'messages', 'messagesReady', 'messagesUnacknowledged', 'consumers', 'node'];

  dlqMessages: DlqMessage[] = [];
  isLoadingDlq = false;
  displayedDlqColumns: string[] = ['payload', 'reason', 'originalExchange', 'originalRoutingKey', 'actions'];

  chartData: any[] = [];
  readonly maxChartDataPoints = 20;

  constructor(private rabbitMqMonitoringService: RabbitMqMonitoringService, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.queues$ = timer(0, 5000).pipe(
      switchMap(() => this.rabbitMqMonitoringService.getQueues()),
      tap(queues => {
        queues.forEach(queue => {
          let series = this.chartData.find(s => s.name === queue.name);
          if (!series) {
            series = { name: queue.name, series: [] };
            this.chartData.push(series);
          }
          series.series.push({ name: new Date().getTime(), value: queue.messages });
          if (series.series.length > this.maxChartDataPoints) {
            series.series.shift();
          }
        });
        this.chartData = [...this.chartData];
        this.cdr.detectChanges();
      })
    );

    this.loadDlqMessages();
  }

  loadDlqMessages(): void {
    this.isLoadingDlq = true;
    this.rabbitMqMonitoringService.getDlqMessages()
      .pipe(finalize(() => {
        this.isLoadingDlq = false;
        this.cdr.detectChanges();
      }))
      .subscribe(messages => {
        this.dlqMessages = messages;
      });
  }

  requeue(message: DlqMessage): void {
    this.rabbitMqMonitoringService.requeueMessage(message)
      .subscribe(() => {
        this.loadDlqMessages();
      });
  }

  getReason(message: DlqMessage): string {
    const xDeath = message.properties.headers['x-death'][0];
    return xDeath ? xDeath.reason : 'N/A';
  }

  getOriginalExchange(message: DlqMessage): string {
    const xDeath = message.properties.headers['x-death'][0];
    return xDeath ? xDeath.exchange : 'N/A';
  }

  getOriginalRoutingKey(message: DlqMessage): string {
    const xDeath = message.properties.headers['x-death'][0];
    return xDeath ? xDeath['routing-keys'][0] : 'N/A';
  }

  xAxisTickFormatting(val: number): string {
    const date = new Date(val);
    return date.toLocaleTimeString();
  }
}
