import { PayloadAction, createAction, createSlice } from "@reduxjs/toolkit";
import { AlertColor } from "@mui/material";
import { AppThunk } from "../store";
import { IAlertSlice } from "../types/slices";
import { TIMEOUT_ALERT } from "../constants/system";

const initialState = {
  classify: "success",
  message: "",
  isShowAlert: false,
} as IAlertSlice;

export const resetAlertState = createAction("alert/resetState");

export const alertSlice = createSlice({
  name: "alert",
  initialState: initialState,
  reducers: {
    showAlert: (
      state,
      action: PayloadAction<{ classify: AlertColor; message: string }>
    ) => {
      state.isShowAlert = true;
      state.message = action.payload.message;
      state.classify = action.payload.classify;
    },
    hideAlert: (state) => {
      state.message = "";
      state.isShowAlert = false;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(resetAlertState, () => initialState);
  },
});

// Action creators are generated for each case reducer function
export const { showAlert, hideAlert } = alertSlice.actions;

const setValueToAlert =
  (message: string, classify: AlertColor): AppThunk =>
  async (dispatch) => {
    return new Promise((resolve) => {
      dispatch(showAlert({ message, classify }));
      setTimeout(() => {
        dispatch(hideAlert());
        resolve();
      }, TIMEOUT_ALERT);
    });
  };

export const setNotificationSuccess = (message: string) =>
  setValueToAlert(message, "success");

export const setNotificationInfo = (message: string) =>
  setValueToAlert(message, "info");

export const setNotificationError = (message: string) =>
  setValueToAlert(message, "error");

export const setNotificationWarning = (message: string) =>
  setValueToAlert(message, "warning");

export default alertSlice.reducer;
