import axios, { AxiosError, AxiosRequestConfig } from "axios";
import {
  getToken,
  removePageSelected,
  removeToken,
  setToken,
} from "./localstorage";
import { logout } from "../slices/auth";
import { setNotificationError } from "../slices/alert";

let isRefreshing = false;
let failedQueue: { resolve: (value?: any) => void; reject: (reason?: any) => void }[] = [];

const processQueue = (error: AxiosError | null, token: string | null = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });

  failedQueue = [];
};

const fetch = () => {
  const token = getToken();
  const headers = {
    Accept: "application/json",
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
  } as any;
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const api = axios.create({
    baseURL: process.env.REACT_APP_API_URL,
    headers,
    withCredentials: false,
  });

  api.interceptors.request.use(
    (config) => {
      const currentToken = getToken();
      if (currentToken && config.headers) {
        config.headers.Authorization = `Bearer ${currentToken}`;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  api.interceptors.response.use(
    (response) => response,
    async (error: AxiosError) => {
      const originalRequest = error.config as AxiosRequestConfig & {
        _retry?: boolean;
      };

      // Xử lý lỗi 401 cho việc refresh token
      if (error.response?.status === 401 && !originalRequest._retry) {
        // Avoid retry loops for the refresh token endpoint itself
        if (originalRequest.url?.includes("/api/auth/refresh-token")) {
          return Promise.reject(error);
        }

        if (isRefreshing) {
          return new Promise((resolve, reject) => {
            failedQueue.push({ resolve, reject });
          })
            .then((token) => {
              if (originalRequest.headers) {
                originalRequest.headers.Authorization = `Bearer ${token}`;
              }
              return api(originalRequest);
            })
            .catch((err) => {
              return Promise.reject(err);
            });
        }

        originalRequest._retry = true;
        isRefreshing = true;

        const currentToken = getToken();
        if (!currentToken) {
          isRefreshing = false;
          return Promise.reject(error);
        }

        return new Promise((resolve, reject) => {
          axios
            .post(
              `${process.env.REACT_APP_API_URL}/api/auth/refresh-token`,
              {},
              {
                headers: { Authorization: `Bearer ${currentToken}` },
              }
            )
            .then(({ data }) => {
              const newToken = data.token;
              setToken(newToken);
              api.defaults.headers.common["Authorization"] = `Bearer ${newToken}`;
              if (originalRequest.headers) {
                originalRequest.headers.Authorization = `Bearer ${newToken}`;
              }
              processQueue(null, newToken);
              resolve(api(originalRequest));
            })
            .catch((err) => {
              processQueue(err, null);
              // If refresh fails, logout the user
              const { store } = require("../store");
              store.dispatch(logout());
              removeToken();
              removePageSelected();
              window.location.href = "/";
              reject(err);
            })
            .finally(() => {
              isRefreshing = false;
            });
        });
      }

      // Xử lý TẤT CẢ các lỗi khác
      const { store } = require("../store");
      const errorMessage =
        (error.response?.data as any)?.message || error.message;

      if (
        !error.response ||
        (error.response.status >= 500 && error.response.status <= 599)
      ) {
        store.dispatch(
          setNotificationError(
            "Đã có lỗi không mong muốn xảy ra. Vui lòng liên hệ 0379452201 để được hỗ trợ."
          )
        );
      } else {
        // Hiển thị message từ body của server cho các lỗi 4xx
        store.dispatch(setNotificationError(errorMessage));
      }

      return Promise.reject(error);
    }
  );

  return api;
};

export const get = (url: string, config: AxiosRequestConfig = {}) => {
  const request = fetch();
  return request.get(url, config);
};

export const post = (
  url: string,
  data: any,
  config: AxiosRequestConfig = {}
) => {
  const request = fetch();
  return request.post(url, data, config);
};
