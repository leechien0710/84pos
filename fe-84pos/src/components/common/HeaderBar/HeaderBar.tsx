import React, { HTMLAttributes, FC } from "react";
import { useNavigate } from "react-router-dom";
import { Grid2 as Grid, Button } from "@mui/material";
import { StaticImage } from "../StaticImage";
import { redirectToHome } from "../../../utils/redirect";
import Logo from "../../../assets/sign-in/logo.webp";
import Logo2x from "../../../assets/sign-in/logo@2x.webp";
import Logo3x from "../../../assets/sign-in/logo@3x.webp";
import Phone from "../../../assets/sign-in/phone.webp";
import Phone2x from "../../../assets/sign-in/phone@2x.webp";
import Phone3x from "../../../assets/sign-in/phone@3x.webp";
import { useStyles } from "./HeaderBar.style";

export const HeaderBar: FC<HTMLAttributes<HTMLDivElement>> = (props) => {
  const classes = useStyles();
  const navigate = useNavigate();

  const onNavigateHome = () => {
    redirectToHome(navigate);
  };

  return (
    <Grid display="flex" alignItems="center" flexDirection="column" {...props}>
      <Grid
        container
        alignItems="center"
        justifyContent="space-between"
        className={classes.root}
      >
        <StaticImage
          className={classes.logo}
          src={Logo}
          src2x={Logo2x}
          src3x={Logo3x}
          onClick={onNavigateHome}
        />
        <Button
          className={classes.btnContact}
          variant="contained"
          startIcon={
            <StaticImage src={Phone} src2x={Phone2x} src3x={Phone3x} />
          }
        >
          Liên hệ hỗ trợ: 037 945 2201
        </Button>
      </Grid>
      <Button
        className={`${classes.btnContact} ${classes.btnPhone}`}
        variant="contained"
        startIcon={<StaticImage src={Phone} src2x={Phone2x} src3x={Phone3x} />}
      >
        Liên hệ hỗ trợ: 037 945 2201
      </Button>
    </Grid>
  );
};
