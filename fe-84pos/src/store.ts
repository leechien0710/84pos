import { configureStore, ThunkAction, Action } from "@reduxjs/toolkit";
import auth from "./slices/auth";
import alert from "./slices/alert";

export const store = configureStore({
  reducer: {
    auth,
    alert,
  },
  enhancers: (getDefaultEnhancers) => getDefaultEnhancers(),
});

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action<string>
>;

export type AsyncAppThunk<ReturnType = void> = ThunkAction<
  Promise<ReturnType>,
  RootState,
  unknown,
  Action<string>
>;
