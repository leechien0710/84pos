import { HTMLAttributes, FC, useState, ChangeEvent } from "react";
import {
  Grid2 as Grid,
  Button,
  Typography,
  TextField,
  Checkbox,
  CircularProgress,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import {
  redirectToForgotPass,
  redirectToSignUp,
} from "../../../utils/redirect";
import { useStyles } from "./SignInForm.style";
import { AxiosError } from "axios";
import { login, currentUser } from "../../../slices/auth";
import { useAppDispatch } from "../../../hook";
import { setNotificationSuccess } from "../../../slices/alert";
import { IUser } from "../../../types/account";

export const SignInForm: FC<HTMLAttributes<HTMLDivElement>> = (props) => {
  const { className, ...otherProps } = props;
  const classes = useStyles();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [isShowPass, setIsShowPass] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [isError, setIsError] = useState(false);

  const onNavigateForgotPass = () => {
    redirectToForgotPass(navigate);
  };

  const onNavigateSignUp = () => {
    redirectToSignUp(navigate);
  };

  const onChangeInput = (
    event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    type: "username" | "pass"
  ) => {
    if (type === "username") {
      setUsername(event.target.value);
    } else {
      setPassword(event.target.value);
    }
  };

  const onSubmit = async () => {
    if (isLoading) return;
    setIsLoading(true);
    try {
      // Bước 1: Gọi login và nhận về user, token đã được lưu
      const user = (await dispatch(login(username, password))) as IUser;

      // Bước 2: Hiển thị popup và chờ cho nó tự tắt
      await dispatch(setNotificationSuccess("Đăng nhập thành công!"));

      // Bước 3: Sau khi popup tắt, cập nhật state để kích hoạt chuyển hướng
      if (user) {
        dispatch(currentUser({ user: user as any }));
      }
    } catch (e) {
      if ((e as AxiosError).status === 404) {
        setIsError(true);
      }
    }
    setIsLoading(false);
  };

  return (
    <div className={`${classes.root} ${className}`} {...otherProps}>
      <Typography variant="h5" className={classes.title}>
        Đăng nhập
      </Typography>
      <div>
        <div>
          <Typography variant="body1" className={classes.label}>
            Số điện thoại
          </Typography>
          <TextField
            fullWidth
            className={classes.input}
            placeholder="Nhập số điện thoại"
            autoComplete="false"
            inputProps={{ autoComplete: "false" }}
            onChange={(e) => onChangeInput(e, "username")}
            // InputProps={{
            //   sx: {
            //     fontSize: "16px !important",
            //   },
            // }}
            // InputLabelProps={{
            //   sx: {
            //     fontSize: "16px !important",
            //   },
            // }}
          />
        </div>
        <div className={classes.field}>
          <Typography variant="body1" className={classes.label}>
            Mật khẩu
          </Typography>
          <TextField
            fullWidth
            className={classes.input}
            type={isShowPass ? "text" : "password"}
            placeholder="Nhập mật khẩu"
            onChange={(e) => onChangeInput(e, "pass")}
          />
        </div>
        <Grid display="flex" justifyContent="space-between" alignItems="center">
          <Grid display="flex" alignItems="center">
            <Checkbox
              value={isShowPass}
              className={classes.checkBox}
              onChange={() => setIsShowPass(!isShowPass)}
            />
            <Typography variant="body1" className={classes.labelBottom}>
              Hiện mật khẩu
            </Typography>
          </Grid>
          <Typography
            variant="body1"
            className={`${classes.labelBottom} ${classes.forgetPass}`}
            onClick={onNavigateForgotPass}
          >
            Quên mật khẩu?
          </Typography>
        </Grid>
        <Typography
          variant="body2"
          className={`${classes.error} ${isError && classes.showError}`}
        >
          * Số điện thoại hoặc mật khẩu chưa chính xác
        </Typography>
      </div>
      <div className={classes.action}>
        <Button
          fullWidth
          variant="contained"
          className={classes.btn}
          disabled={!username || !password}
          onClick={onSubmit}
        >
          {isLoading ? (
            <CircularProgress size={22} color="inherit" />
          ) : (
            "Đăng nhập"
          )}
        </Button>
        <Button
          fullWidth
          variant="outlined"
          className={`${classes.btn} ${classes.btnRegister}`}
          onClick={onNavigateSignUp}
        >
          Đăng ký tài khoản
        </Button>
      </div>
    </div>
  );
};
