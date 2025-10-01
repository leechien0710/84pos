import { get } from "../utils/api";

export const getConversation = async (
  pageId: string,
  limit: number = 20,
  offset: number = 0
) => {
  if (!pageId) return;
  const res = await get(
    `/api/face/page/${pageId}/conversations?limit=${limit}&offset=${offset}`
  );

  return res.data;
};

export const getConversationDetail = async (
  pageId: string,
  coversationId: string,
  limit: number = 20,
  offset: number = 0
) => {
  if (!coversationId) return;
  const res = await get(
    `/api/face/page/${pageId}/conversation/${coversationId}/messages?limit=${limit}&offset=${offset}`
  );

  return res.data;
};
