import React, { HTMLAttributes, FC } from "react";
import { Grid2 as Grid, Button, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";
import {
  redirectToForgotPass,
  redirectToSignUp,
} from "../../../utils/redirect";
import { StaticImage } from "../StaticImage";
import Tiktok from "../../../assets/sign-in/tiktok.webp";
import Tiktok2x from "../../../assets/sign-in/tiktok@2x.webp";
import Tiktok3x from "../../../assets/sign-in/tiktok@3x.webp";
import Facebook from "../../../assets/sign-in/facebook.webp";
import Facebook2x from "../../../assets/sign-in/facebook@2x.webp";
import Facebook3x from "../../../assets/sign-in/facebook@3x.webp";
import { useStyles } from "./SelectPlatformForm.style";
import { authWithFacebook } from "../../../models/auth";

export const SelectPlatformForm: FC<HTMLAttributes<HTMLDivElement>> = (
  props
) => {
  const classes = useStyles();
  const navigate = useNavigate();
  const { className, ...otherProps } = props;

  const onAuthPlatform = async () => {
    await authWithFacebook();
  };

  return (
    <div className={`${classes.root} ${className}`} {...otherProps}>
      <Typography className={classes.title}>
        Liên kết đến tài sản của bạn
      </Typography>
      <div className={classes.actions}>
        <Button
          fullWidth
          variant="outlined"
          color="inherit"
          className={classes.btn}
          startIcon={
            <StaticImage src={Tiktok} src2x={Tiktok2x} src3x={Tiktok3x} />
          }
        >
          Liên kết tài khoản TikTok
        </Button>
        <Button
          fullWidth
          variant="outlined"
          color="inherit"
          className={classes.btn}
          startIcon={
            <StaticImage src={Facebook} src2x={Facebook2x} src3x={Facebook3x} />
          }
          onClick={onAuthPlatform}
        >
          Liên kết tài khoản Facebook
        </Button>
      </div>
    </div>
  );
};
