import { HTMLAttributes, FC } from "react";
import { Grid2 as Grid } from "@mui/material";
import { StaticImage } from "../StaticImage";
import Logo from "../../../assets/sign-in/logo.webp";
import Logo2x from "../../../assets/sign-in/logo@2x.webp";
import Logo3x from "../../../assets/sign-in/logo@3x.webp";
import { useStyles } from "./SplashScreen.style";

export const SplashScreen: FC<HTMLAttributes<HTMLDivElement>> = (props) => {
  const classes = useStyles();
  const { className, ...otherProps } = props;

  return (
    <Grid
      display="flex"
      justifyContent="center"
      alignItems="center"
      className={`${classes.root} ${className}`}
      {...otherProps}
    >
      <StaticImage src={Logo} src2x={Logo2x} src3x={Logo3x} />
    </Grid>
  );
};
