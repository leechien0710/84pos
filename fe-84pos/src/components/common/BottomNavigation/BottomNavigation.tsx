import React, { HTMLAttributes, FC } from "react";
import { useNavigate } from "react-router-dom";
import { Grid2 as Grid, Button } from "@mui/material";
import { StaticImage } from "../StaticImage";
import Access from "../../../assets/select-account/access.webp";
import Access2x from "../../../assets/select-account/access@2x.webp";
import Access3x from "../../../assets/select-account/access@3x.webp";
import { useStyles } from "./BottomNavigation.style";
import { useAppDispatch } from "../../../hook";
import { setNotificationWarning } from "../../../slices/alert";

interface IBottomNavigationProps {
  isAccessDisabled?: boolean;
}

export const BottomNavigation: FC<
  HTMLAttributes<HTMLDivElement> & IBottomNavigationProps
> = (props) => {
  const { className, isAccessDisabled, ...otherProps } = props;
  const classes = useStyles();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const onNavigateToOverview = () => {
    navigate("/overview");
  };

  const handleAccessClick = () => {
    if (isAccessDisabled) {
      dispatch(
        setNotificationWarning("Vui lòng chọn một tài khoản để tiếp tục")
      );
    } else {
      onNavigateToOverview();
    }
  };

  return (
    <Grid
      display="flex"
      alignItems="center"
      justifyContent="flex-end"
      className={`${classes.root} ${className}`}
      {...otherProps}
    >
      <div onClick={handleAccessClick} style={{ cursor: isAccessDisabled ? 'not-allowed' : 'pointer' }}>
        <Button
          variant="contained"
          endIcon={
            <StaticImage src={Access} src2x={Access2x} src3x={Access3x} />
          }
          className={classes.btnAccess}
          disabled={isAccessDisabled}
        >
          Truy cập
        </Button>
      </div>
    </Grid>
  );
};
