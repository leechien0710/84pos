export interface IConversation {
  is_subscribed: boolean;
  can_reply: boolean;
  snippet: string;
  senders: {
    data: ISender[];
  };
  unread_count: number;
  updated_time: string;
  id: string;
}

export interface IMessage {
  message: string;
  created_time: string;
  from: ISender;
  id: string;
  attachments?: IAttachment;
  type?: "image" | "video";
}

export interface IChat {
  [key: string]: {
    message?: string;
    isMe?: boolean;
    time?: string;
    type?: "image" | "video";
  }[];
}

interface IAttachment {
  data: IDataAttachment[];
}

interface ISender {
  name: string;
  email: string;
  id: string;
}

interface IDataAttachment {
  image_data: {
    url: string;
  };
  video_data: {
    url: string;
  };
}
