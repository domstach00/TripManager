export interface QueueInfo {
  name: string;
  vhost: string;
  durable: boolean;
  autoDelete: boolean;
  messages: number;
  messagesReady: number;
  messagesUnacknowledged: number;
  consumers: number;
  node: string;
}
