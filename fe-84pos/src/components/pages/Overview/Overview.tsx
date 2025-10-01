import { HTMLAttributes, FC } from "react";
import { Grid2 as Grid } from "@mui/material";
import { useStyles } from "./Overview.style";

export const Overview: FC<HTMLAttributes<HTMLDivElement>> = () => {
  const classes = useStyles();

  return (
    <Grid display="flex" flexDirection="column" className={classes.root}>
      <h1>Overview component</h1>
    </Grid>
  );
};
