import { PayloadAction, createSlice } from "@reduxjs/toolkit";
import { map } from "lodash-es";
import { AppThunk, AsyncAppThunk } from "../store";
import { IAuthSlice } from "../types/slices";
import { IFbUser, IUser } from "../types/account";
import {
  login as loginRequest,
  register as registerRequest,
  getFbPageActive,
  userFacebook,
} from "../models/auth";
import {
  setToken,
  removeToken,
  removePageSelected,
} from "../utils/localstorage";
import { decodeJWT } from "../utils/auth";
import {} from "../utils/redirect";
import { setNotificationError } from "./alert";

const initialState = {
  user: null,
  fbUser: null,
  fbUserStatus: "idle",
} as IAuthSlice;

export const authSlice = createSlice({
  name: "user",
  initialState: initialState,
  reducers: {
    currentUser: (
      state,
      action: PayloadAction<{
        user: IUser | null;
      }>
    ) => {
      state.user = action.payload.user;
    },
    updateFbUser: (
      state,
      action: PayloadAction<{
        fbUser: IFbUser[] | null;
      }>
    ) => {
      state.fbUser = action.payload.fbUser;
    },
    setFbUserStatus: (
      state,
      action: PayloadAction<"idle" | "loading" | "succeeded" | "failed">
    ) => {
      state.fbUserStatus = action.payload;
    },
    logout: (state) => {
      state.user = null;
      state.fbUser = null;
      state.fbUserStatus = "idle";
    },
  },
});

// Action creators are generated for each case reducer function
export const {
  currentUser,
  updateFbUser,
  setFbUserStatus,
  logout: logoutAction,
} = authSlice.actions;

export const login =
  (username: string, password: string): AsyncAppThunk<IUser> =>
  async (dispatch) => {
    try {
      const res = await loginRequest(username, password);
      
      // Token nằm trong data object
      const token = (res as any)?.data?.token;
      
      if (!token || typeof token !== "string") {
        throw new Error("Missing token");
      }
      
      const user = decodeJWT(token) as IUser | null;
      
      if (!user) {
        throw new Error("Invalid token");
      }
      
      setToken(token);
      // Không dispatch ở đây nữa mà trả về user
      return user;
    } catch (error) {
      throw error;
    }
  };

export const register =
  (username: string, password: string): AsyncAppThunk =>
  async (dispatch) => {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const res = await registerRequest(username, password);

    // const { token } = await registerRequest(username, password);
    // const user = decodeJWT(token);
    // setToken(token);
    // dispatch(currentUser({ user }));
  };

export const logout = (): AppThunk => (dispatch) => {
  removeToken();
  removePageSelected();
  dispatch(logoutAction());
};

export const getUserFacebook =
  (): AsyncAppThunk => async (dispatch, getState) => {
    const { auth } = getState();

    // Chỉ gọi API nếu user tồn tại và trạng thái là 'idle'
    if (!auth.user?.id || auth.fbUserStatus !== "idle") {
      return;
    }

    try {
      dispatch(setFbUserStatus("loading"));
      const res = await userFacebook();
      dispatch(updateFbUser({ fbUser: res.data }));
      dispatch(setFbUserStatus("succeeded"));
    } catch (e: any) {
      // Dispatch action để hiển thị lỗi cho người dùng
      dispatch(setFbUserStatus("failed"));

      if (e?.response?.status === 404) {
        // Chỉ hiển thị lỗi, không đăng xuất
        dispatch(
          setNotificationError(
            e?.response?.data?.message ||
              "Chưa có tài khoản Facebook nào được liên kết."
          )
        );
      } else {
        // Các lỗi không mong muốn khác
        dispatch(
          setNotificationError(
            "Đã có lỗi không mong muốn xảy ra. Vui lòng liên hệ 0379452201 để được hỗ trợ."
          )
        );
      }
    }
  };

export const fetchListFbPageActive =
  (): AsyncAppThunk => async (dispatch, getState) => {
    try {
      const { auth } = getState();
      
      // Chỉ gọi API nếu có fbUser và chưa có pages
      if (!auth?.fbUser?.length) {
        return;
      }
      
      // Kiểm tra xem đã fetch pages chưa (pages !== undefined)
      // KHÔNG check pages.length > 0 vì API có thể trả về [] (mảng rỗng hợp lệ)
      const alreadyFetched = auth.fbUser.some(user => user.pages !== undefined);
      if (alreadyFetched) {
        return;
      }
      
      const newFbUser = await Promise.all(
        map(auth.fbUser, async (user) => {
          const pagesRes = await getFbPageActive(user.userId);
          return { ...user, pages: pagesRes.data };
        })
      );
      dispatch(updateFbUser({ fbUser: newFbUser }));
    } catch (e) {
      console.error("Failed to fetch active Facebook pages:", e);
    }
  };

export default authSlice.reducer;
