import React, { HTMLAttributes, FC } from "react";
import { Grid2 as Grid } from "@mui/material";
import { HeaderBar } from "../../common/HeaderBar";
import { SelectPlatformForm } from "../../common/SelectPlatformForm";
import { useStyles } from "./SelectPlatform.style";

export const SelectPlatform: FC<HTMLAttributes<HTMLDivElement>> = () => {
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
        <SelectPlatformForm className={classes.signInForm} />
      </Grid>
    </Grid>
  );
};
