import { HTMLAttributes, FC } from "react";
import { useNavigate } from "react-router-dom";
import { Grid2 as Grid, Typography, Button } from "@mui/material";
import { StaticImage } from "../StaticImage";
import Logo from "../../../assets/sign-in/logo.webp";
import Logo2x from "../../../assets/sign-in/logo@2x.webp";
import Logo3x from "../../../assets/sign-in/logo@3x.webp";
import { useStyles } from "./HeaderLanding.style";
import {
  redirectToTerm,
  redirectToPolicy,
  redirectToLanding,
  redirectToHome,
} from "../../../utils/redirect";

export const HeaderLanding: FC<HTMLAttributes<HTMLDivElement>> = (props) => {
  const { className, ...otherProps } = props;
  const classes = useStyles();
  const navigate = useNavigate();

  const onClickTerm = () => {
    redirectToTerm(navigate);
  };

  const onClickPolicy = () => {
    redirectToPolicy(navigate);
  };

  const onClickHome = () => {
    redirectToLanding(navigate);
  };

  const onClickApp = () => {
    redirectToHome(navigate);
  };

  return (
    <Grid
      {...otherProps}
      display="flex"
      alignItems="center"
      justifyContent="space-between"
      className={`${classes.root} ${className}`}
    >
      <StaticImage src={Logo} src2x={Logo2x} src3x={Logo3x} />
      <Typography
        variant="body1"
        className={classes.menu}
        onClick={onClickHome}
      >
        Trang chủ
      </Typography>
      <Typography
        variant="body1"
        className={classes.menu}
        onClick={onClickTerm}
      >
        điều khoản dịch vụ
      </Typography>
      <Typography
        variant="body1"
        className={classes.menu}
        onClick={onClickPolicy}
      >
        chính sách bảo mật
      </Typography>
      <Button
        variant="contained"
        className={classes.btnStart}
        onClick={onClickApp}
      >
        Bắt đầu miễn phí
      </Button>
    </Grid>
  );
};
