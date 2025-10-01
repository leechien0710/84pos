export const getToken = () => {
  return localStorage.getItem("token");
};

export const setToken = (token: string) => {
  localStorage.setItem("token", token);
};

export const removeToken = () => {
  localStorage.removeItem("token");
};

export const setPageSelected = (id: string) => {
  localStorage.setItem("page-selected", id);
};

export const getPageSelected = () => {
  return localStorage.getItem("page-selected");
};

export const removePageSelected = () => {
  return localStorage.removeItem("page-selected");
};
