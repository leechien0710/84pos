export type ArticleType =
  | "mobile_status_update"
  | "added_video"
  | "added_livetream"
  | "added_photos"
  | null;

export interface ILivePostCard {
  id: string;
  message: string;
  fullPictureUrl: string;
  createdTime: string;
  updatedTime: string;
  commentCount: number;
  likeCount: number;
  statusType: string;
  pageId: string;
}

export interface IComment {
  id: string;
  message: string;
  fromUser: string;
  attachment: string | null;
  postId: string;
  createdTime: string;
  hasPhone: boolean;
}
