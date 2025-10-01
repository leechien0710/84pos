export const decodeJWT = (token: string) => {
  try {
    if (!token || typeof token !== "string" || token.indexOf(".") === -1) {
      throw new Error("Invalid token format");
    }
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    return JSON.parse(atob(base64));
  } catch (error) {
    console.error("Invalid Token", error);
    return null;
  }
};

export const invalidRegister = (
  username: string,
  pass: string,
  confirmPass: string
) => {
  if (!username || !pass || !confirmPass) return true;
  if (pass !== confirmPass) return true;
  return false;
};
