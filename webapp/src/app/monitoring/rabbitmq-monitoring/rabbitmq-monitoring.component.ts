import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { Observable, timer } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { QueueInfo } from './queue-info.model';
import { ApiService } from '../../shared/_service/api.service';
import { ApiPath } from '../../shared/_model/ApiPath';

@Component({
  selector: 'app-rabbitmq-monitoring',
  templateUrl: './rabbitmq-monitoring.component.html',
  styleUrls: ['./rabbitmq-monitoring.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RabbitmqMonitoringComponent implements OnInit {

  queues$: Observable<QueueInfo[]>;
  displayedColumns: string[] = ['name', 'messages', 'messagesReady', 'messagesUnacknowledged', 'consumers', 'node'];

  chartData: any[] = [];
  readonly maxChartDataPoints = 20;

  constructor(private apiService: ApiService, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.queues$ = timer(0, 5000).pipe(
      switchMap(() => this.apiService.get<QueueInfo[]>(ApiPath.rabbitmqQueues)),
      tap(queues => {
        queues.forEach(queue => {
          // Find or create series for the current queue
          let series = this.chartData.find(s => s.name === queue.name);
          if (!series) {
            series = { name: queue.name, series: [] };
            this.chartData.push(series);
          }

          // Add new data point
          series.series.push({ name: new Date().getTime(), value: queue.messages });

          // Limit data points
          if (series.series.length > this.maxChartDataPoints) {
            series.series.shift(); // Remove the oldest data point
          }
        });

        // Trigger change detection for ngx-charts
        this.chartData = [...this.chartData];
        this.cdr.detectChanges(); // Force change detection
      })
    );
  }

  xAxisTickFormatting(val: number): string {
    const date = new Date(val);
    return date.toLocaleTimeString();
  }
}
