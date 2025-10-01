import { HTMLAttributes, FC } from "react";
import { Grid2 as Grid } from "@mui/material";
import { HeaderBar } from "../../common/HeaderBar";
import { SignInForm } from "../../common/SignInForm";
import { useStyles } from "./SignIn.style";

export const SignIn: FC<HTMLAttributes<HTMLDivElement>> = () => {
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
        <SignInForm className={classes.signInForm} />
      </Grid>
    </Grid>
  );
};
