import { post, get } from "../utils/api";

export const login = async (username: string, password: string) => {
  const res = await post("/api/auth/login", { username, password });
  return res.data;
};

export const register = async (username: string, password: string) => {
  const res = await post("/api/auth/register", { username, password });
  return res.data;
};

export const refreshToken = async () => {
  const res = await post("/api/auth/refresh-token", {});
  return res.data;
};

export const authWithFacebook = async () => {
  const res = await get("/api/face/auth-url");
  return res.data.data;
};

export const userFacebook = async () => {
  const res = await get(`/api/face/user-facebook/me`);
  return res.data;
};

export const getFbPageActive = async (userId: string) => {
  const res = await get(`/api/face/user/${userId}/pages/active`);
  return res.data;
};

export const getFbPage = async (userId: string) => {
  const res = await get(`/api/face/user/${userId}/pages`);
  return res.data;
};

export const addFbPages = async (userIds: string[]) => {
  const res = await post("/api/face/pages/active", userIds);
  return res.data;
};

export const syncAllPage = async (pageIds: string[]) => {
  const res = await post("/api/face/pages/sync-all", { pageIds });
  return res.data;
};

export const removeFbPage = async (userIds: string[]) => {
  const res = await post("/api/face/pages/inactive", userIds);
  return res.data;
};
