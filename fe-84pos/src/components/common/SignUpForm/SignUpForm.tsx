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
import { redirectToHome } from "../../../utils/redirect";
import { invalidRegister } from "../../../utils/auth";
import { useAppDispatch } from "../../../hook";
import { register } from "../../../slices/auth";
import { setNotificationError } from "../../../slices/alert";
import { useStyles } from "./SignUpForm.style";

export const SignUpForm: FC<HTMLAttributes<HTMLDivElement>> = (props) => {
  const { className, ...otherProps } = props;
  const classes = useStyles();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [isShowPass, setIsShowPass] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const onNavigateSignIn = () => {
    redirectToHome(navigate);
  };

  const onChangeInput = (
    event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    type: "username" | "pass" | "confirmPass"
  ) => {
    if (type === "username") {
      setUsername(event.target.value);
    } else if (type === "pass") {
      setPassword(event.target.value);
    } else {
      setConfirmPassword(event.target.value);
    }
  };

  const onSubmit = async () => {
    if (isLoading) return;
    setIsLoading(true);
    try {
      await dispatch(register(username, password));
      onNavigateSignIn();
    } catch (e) {
      console.log(e);
      dispatch(setNotificationError((e as Error)?.message));
    }
    setIsLoading(false);
  };

  return (
    <div className={`${classes.root} ${className}`} {...otherProps}>
      <Typography variant="h5" className={classes.title}>
        Đăng ký
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
        <div className={classes.field}>
          <Typography variant="body1" className={classes.label}>
            Xác nhận mật khẩu
          </Typography>
          <TextField
            fullWidth
            className={classes.input}
            type={isShowPass ? "text" : "password"}
            placeholder="Nhập lại mật khẩu"
            onChange={(e) => onChangeInput(e, "confirmPass")}
          />
        </div>
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
      </div>
      <div className={classes.action}>
        <Button
          fullWidth
          variant="contained"
          className={classes.btn}
          disabled={invalidRegister(username, password, confirmPassword)}
          onClick={onSubmit}
        >
          {isLoading ? (
            <CircularProgress size={22} color="inherit" />
          ) : (
            "Đăng ký"
          )}
        </Button>
        <Button
          fullWidth
          variant="outlined"
          className={`${classes.btn} ${classes.btnRegister}`}
          onClick={onNavigateSignIn}
        >
          Đăng nhập
        </Button>
      </div>
    </div>
  );
};
