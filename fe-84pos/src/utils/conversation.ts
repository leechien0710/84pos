import { map, groupBy, find, fromPairs, orderBy } from "lodash-es";
import { IMessage } from "../types/conversation";

export const getSenderName = (messages: IMessage[], pageId: string) => {
  const sender = find(messages, (m) => m?.from?.id !== pageId);
  return sender?.from?.name || "";
};

export const convertMessage = (messages: IMessage[], pageId: string) => {
  const messageMap = map(messages, (m) => {
    if (m?.attachments?.data?.length) {
      const attachment = m.attachments.data[0];
      if (attachment?.image_data?.url) {
        m.message = attachment.image_data.url;
        m.type = "image";
      } else if (attachment?.video_data?.url) {
        m.message = attachment.video_data.url;
        m.type = "video";
      }
    }
    return { ...m, isMe: m?.from?.id === pageId };
  });
  const groupDataByDate = groupBy(messageMap, (item) => {
    const date = new Date(item.created_time);
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  });
  const reversedData = fromPairs(
    orderBy(
      Object.entries(groupDataByDate),
      ([dateStr]) => {
        const [day, month, year] = dateStr.split("/").map(Number);
        return new Date(year, month - 1, day);
      },
      "asc"
    )
  );
  return reversedData;
};
