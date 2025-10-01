import React, { HTMLAttributes, FC } from "react";
import { Grid2 as Grid, Button, Typography, TextField } from "@mui/material";
import { useStyles } from "./ForgotPasswordForm.style";

export const ForgotPasswordForm: FC<HTMLAttributes<HTMLDivElement>> = (
  props
) => {
  const classes = useStyles();
  const { className, ...otherProps } = props;

  return (
    <div className={`${classes.root} ${className}`} {...otherProps}>
      <Typography variant="h5" className={classes.title}>
        Quên mật khẩu
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
          />
        </div>
      </div>
      <div className={classes.action}>
        <Button fullWidth variant="contained" className={classes.btn}>
          Lấy lại mật khẩu
        </Button>
      </div>
    </div>
  );
};
