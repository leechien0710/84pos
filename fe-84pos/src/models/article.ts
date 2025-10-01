import { LIMIT_ARTICLES, LIMIT_COMMENTS } from "../constants/article";
import { ArticleType } from "../types/article";
import { get } from "../utils/api";

export const getArticles = async (
  pageId: string,
  type: ArticleType,
  offset: number
) => {
  let urlToReq = `/api/face/page/${pageId}/posts?page=${
    offset ?? 0
  }&size=${LIMIT_ARTICLES}`;
  if (type) {
    urlToReq += `&type=${type}`;
  }
  const res = await get(urlToReq);
  return res.data;
};

export const getArticleDetail = async (
  articleId: string,
  offset: number,
  hasPhone?: boolean
) => {
  let urlToReq = `/api/face/post/${articleId}/comments?page=${
    offset ?? 0
  }&size=${LIMIT_COMMENTS}`;
  if (hasPhone) {
    urlToReq += `&hasPhone=${hasPhone}`;
  }

  const res = await get(urlToReq);
  return res.data;
};
