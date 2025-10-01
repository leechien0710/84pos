import { HTMLAttributes, FC, useState } from "react";
import { Grid2 as Grid, Button } from "@mui/material";
import { useStyles } from "./DetailLive.style";
import { OrderListTab } from "../OrderListTab";
import { LiveCustomerTab } from "../LiveCustomerTab";
import { useHideRightMenu } from "../../../hooks/screen";

export const DetailLive: FC<HTMLAttributes<HTMLDivElement>> = (props) => {
  const { className, ...otherProps } = props;
  const classes = useStyles();
  const [tabActive, setTabActive] = useState(1);
  const isHideRight = useHideRightMenu();

  const onChangeTab = (tab: number) => {
    setTabActive(tab);
  };

  return (
    <div {...otherProps} className={`${classes.root} ${className}`}>
      <Grid display="flex" justifyContent="space-between">
        <Button
          variant="contained"
          className={`${classes.btnTab} ${tabActive !== 1 && classes.inActive}`}
          onClick={() => onChangeTab(1)}
        >
          Danh sách
        </Button>
        <Button
          variant="contained"
          className={`${classes.btnTab} ${tabActive !== 2 && classes.inActive}`}
          onClick={() => onChangeTab(2)}
        >
          Khách hàng
        </Button>
        <Button
          variant="contained"
          className={`${classes.btnTab} ${tabActive !== 3 && classes.inActive}`}
          onClick={() => onChangeTab(3)}
        >
          Đơn hàng
        </Button>
      </Grid>
      {tabActive === 1 && (
        <OrderListTab contentClass={isHideRight ? classes.contentOrder : ""} />
      )}
      {tabActive !== 1 && (
        <LiveCustomerTab
          className={classes.tab}
          type={tabActive === 2 ? "customer" : "order"}
        />
      )}
    </div>
  );
};
