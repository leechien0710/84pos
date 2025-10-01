import { AlertColor } from "@mui/material";
import { IUser, IFbUser } from "./account";

export interface IAuthSlice {
  user: IUser | null;
  fbUser: IFbUser[] | null;
  fbUserStatus: "idle" | "loading" | "succeeded" | "failed";
}

export interface IAlertSlice {
  classify: AlertColor;
  message: string;
  isShowAlert: boolean;
}
