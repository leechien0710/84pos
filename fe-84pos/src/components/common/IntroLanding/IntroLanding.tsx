import { HTMLAttributes, FC } from "react";
import { Grid2 as Grid, Typography, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { StaticImage } from "../StaticImage";
import Banner from "../../../assets/landing/banner.webp";
import { useStyles } from "./IntroLanding.style";
import { redirectToHome } from "../../../utils/redirect";

export const IntroLanding: FC<HTMLAttributes<HTMLDivElement>> = (props) => {
  const { className, ...otherProps } = props;
  const classes = useStyles();
  const navigate = useNavigate();

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
      <div className={classes.introMain}>
        <Typography className={classes.title}>
          Phần Mềm Quản Lý Bán Hàng Được Tin Dùng Nhất!
        </Typography>
        <Typography className={classes.desc}>
          Với việc tích hợp nhiều tính năng vượt trội, phần mềm quản lý bán hàng
          84Pos giúp người dùng quản lý mọi hoạt động kinh doanh tại cửa hàng và
          bán hàng online hiệu quả, giúp tăng trưởng doanh số nhanh chóng và
          liên tục. Dịch vụ tốt nhất cùng chế độ chăm sóc khách hàng chuyên
          nghiệp, hậu mãi ưu việt sẽ khiến bạn hài lòng!
        </Typography>
        <Button
          variant="contained"
          className={classes.btnStart}
          onClick={onClickApp}
        >
          Dùng thử miễn phí
        </Button>
      </div>
      <div className={classes.banner}>
        <StaticImage src={Banner} className={classes.bannerImg} />
      </div>
    </Grid>
  );
};
