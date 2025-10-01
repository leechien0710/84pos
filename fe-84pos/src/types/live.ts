export type ActionLive = "phone" | "feedback";

export type EmptyType = "comment" | "order";

export interface ILivePost {
  title: string;
  createdAt: string;
  likeCount: number;
  commentCount: number;
}
