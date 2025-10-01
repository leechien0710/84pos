import React, { HTMLAttributes, FC } from "react";
import { Grid2 as Grid } from "@mui/material";
import { HeaderBar } from "../../common/HeaderBar";
import { SignUpForm } from "../../common/SignUpForm";
import { SendOtpForm } from "../../common/SendOtpForm";
import { useStyles } from "./SignUp.style";

export const SignUp: FC<HTMLAttributes<HTMLDivElement>> = () => {
  const classes = useStyles();

  return (
    <Grid display="flex" flexDirection="column" className={classes.root}>
      <HeaderBar className={classes.header} />
      <Grid
        display="flex"
        justifyContent="center"
        alignItems="center"
        className={classes.form}
      >
        <SignUpForm className={classes.signUpForm} />
        {/* <SendOtpForm className={classes.signUpForm} /> */}
      </Grid>
    </Grid>
  );
};
