import { Client, IMessage, IFrame } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { getToken } from './localstorage';

const SOCKET_URL = 'http://localhost:1106/api/face/ws'; // Trỏ đến Gateway

let stompClient: Client | null = null;

const createWebSocketClient = () => {
  const client = new Client({
    webSocketFactory: () => new SockJS(SOCKET_URL),
    connectHeaders: {
      Authorization: `Bearer ${getToken()}`,
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    onConnect: () => {
      console.log('Connected to WebSocket');
    },
    onStompError: (frame: IFrame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    },
  });
  return client;
};

export const connectWebSocket = (userId: number, onMessageReceived: (message: any) => void) => {
  if (stompClient && stompClient.active) {
    console.log('WebSocket is already connected.');
    return;
  }

  stompClient = createWebSocketClient();

  stompClient.onConnect = (frame: IFrame) => {
    console.log('Successfully connected to WebSocket broker');
    stompClient?.subscribe(`/topic/sync-status/${userId}`, (message: IMessage) => {
      onMessageReceived(JSON.parse(message.body)); // Backend gửi object với status và message
    });
  };

  stompClient.activate();
};

export const disconnectWebSocket = () => {
  if (stompClient) {
    stompClient.deactivate();
    stompClient = null;
    console.log('Disconnected from WebSocket');
  }
};
