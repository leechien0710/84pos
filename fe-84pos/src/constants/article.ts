import { ArticleType } from "../types/article";

export const LIMIT_ARTICLES = 20;

export const LIMIT_COMMENTS = 20;

export const ARTICLE_FILTER = [
  { id: null as ArticleType, title: "Tất cả bài đăng" },
  { id: "mobile_status_update" as ArticleType, title: "Bài Viết" },
  { id: "added_video" as ArticleType, title: "Video" },
  { id: "added_livetream" as ArticleType, title: "Đã livestream" },
  { id: "added_photos" as ArticleType, title: "Hình ảnh" },
];
