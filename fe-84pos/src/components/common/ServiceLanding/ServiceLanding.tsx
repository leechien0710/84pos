import { HTMLAttributes, FC } from "react";
import { Typography, Button } from "@mui/material";
import { useStyles } from "./ServiceLanding.style";

export const ServiceLanding: FC<HTMLAttributes<HTMLDivElement>> = (props) => {
  const { className, ...otherProps } = props;
  const classes = useStyles();

  return (
    <div {...otherProps} className={`${classes.root} ${className}`}>
      <Typography className={classes.title}>
        Trải nghiệm dịch vụ trong 30 ngày hoàn toàn miễn phí!
      </Typography>
      <Typography className={classes.desc}>
        Quản lý bán hàng nhanh chóng, đơn giản tiết kiệm chi phí & thời gian là
        những điều mà 84Pos mang lại cho người dùng!
      </Typography>
      <Button variant="contained" className={classes.btnStart}>
        Đăng ký ngay
      </Button>
    </div>
  );
};
