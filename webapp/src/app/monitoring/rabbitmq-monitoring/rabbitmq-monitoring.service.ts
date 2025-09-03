import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../shared/_service/api.service';
import { ApiPath } from '../../shared/_model/ApiPath';
import { DlqMessage } from './dlq.message';
import { QueueInfo } from './queue-info.model';

@Injectable({
  providedIn: 'root'
})
export class RabbitMqMonitoringService {

  constructor(private apiService: ApiService) { }

  getQueues(): Observable<QueueInfo[]> {
    return this.apiService.get<QueueInfo[]>(ApiPath.monitoringRabbitMQQueues);
  }

  getDlqMessages(): Observable<DlqMessage[]> {
    return this.apiService.get<DlqMessage[]>(ApiPath.monitoringRabbitMQDlq);
  }

  requeueMessage(message: DlqMessage): Observable<void> {
    const xDeath = message.properties.headers['x-death'][0];
    const originalExchange = xDeath.exchange;
    const originalRoutingKey = xDeath['routing-keys'][0];

    const requeueRequest = {
      exchange: originalExchange,
      routingKey: originalRoutingKey,
      payload: message.payload,
      properties: message.properties
    };

    return this.apiService.post<void>(ApiPath.monitoringRabbitMQRequeue, requeueRequest);
  }
}
