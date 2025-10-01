import React, { HTMLAttributes, FC, useState } from "react";
import {
  Grid2 as Grid,
  Button,
  Typography,
  TextField,
  Checkbox,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import {
  redirectToForgotPass,
  redirectToSignUp,
} from "../../../utils/redirect";
import { useStyles } from "./SendOtpForm.style";

export const SendOtpForm: FC<HTMLAttributes<HTMLDivElement>> = (props) => {
  const classes = useStyles();
  const navigate = useNavigate();
  const { className, ...otherProps } = props;
  const [otp, setOtp] = useState<string[]>(Array(6).fill(""));

  const handleChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    index: number
  ) => {
    const { value } = event.target;
    if (/^[0-9]*$/.test(value) || value === "") {
      const newOtp = [...otp];
      newOtp[index] = value;
      setOtp(newOtp);
      if (value && index < otp.length - 1) {
        document.getElementById(`otp-${index + 1}`)?.focus();
      }
    }
  };

  const handleKeyDown = (
    event: React.KeyboardEvent<HTMLDivElement>,
    index: number
  ) => {
    if (event.key === "Backspace" && !otp[index] && index > 0) {
      document.getElementById(`otp-${index - 1}`)?.focus();
    }
  };

  return (
    <div className={`${classes.root} ${className}`} {...otherProps}>
      <Typography className={classes.title}>
        Vui lòng nhập mã OTP chúng tôi đã gửi tới số điện thoại{" "}
        <span className={classes.phone}>0942558800</span> để xác thực đăng ký:
      </Typography>
      <Grid
        container
        justifyContent="center"
        spacing={2}
        className={classes.otpContainer}
      >
        {otp.map((digit, index) => (
          <TextField
            key={index}
            id={`otp-${index}`}
            value={digit}
            onChange={(event) => handleChange(event, index)}
            onKeyDown={(event) => handleKeyDown(event, index)}
            inputProps={{ maxLength: 1 }}
            variant="outlined"
            className={classes.input}
          />
        ))}
      </Grid>
      <Button fullWidth variant="contained" className={classes.btn}>
        Xác nhận
      </Button>
      <Button
        fullWidth
        variant="text"
        className={`${classes.btn} ${classes.btnSendAgain}`}
      >
        Gửi lại mã
      </Button>
    </div>
  );
};
