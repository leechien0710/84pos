import React, { HTMLAttributes, FC } from "react";
import { Grid2 as Grid, Button } from "@mui/material";
import { HeaderBar } from "../../common/HeaderBar";
import { ForgotPasswordForm } from "../../common/ForgotPasswordForm";
import { useStyles } from "./ForgotPassword.style";

export const ForgotPassword: FC<HTMLAttributes<HTMLDivElement>> = () => {
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
        <ForgotPasswordForm className={classes.forgotPassForm} />
      </Grid>
    </Grid>
  );
};
