import { HTMLAttributes, FC } from "react";
import {
  Grid2 as Grid,
  Typography,
  Dialog,
  IconButton,
  Button,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import ReplayIcon from "@mui/icons-material/Replay";
import { map } from "lodash-es";
import { Transition } from "../../PopupTransition";
import { useAppSelector } from "../../../../hook";
import { MergeLiveCard } from "../../MergeLiveCard";
import { LivePostCard } from "../../LivePostCard";
import { useStyles } from "./MergeLivePopup.style";

export const LIVES = [
  {
    title: "MEGA Sale 16.6.2024 săn hàng đẹp SIEEU PHAM",
    createdAt: "18:98 18/7/2024",
    likeCount: 123,
    commentCount: 38,
  },
  {
    title: "MEGA Sale 16.6.2024 săn hàng đẹp SIEEU PHAM",
    createdAt: "18:98 18/7/2024",
    likeCount: 123,
    commentCount: 38,
  },
  {
    title: "MEGA Sale 16.6.2024 săn hàng đẹp SIEEU PHAM",
    createdAt: "18:98 18/7/2024",
    likeCount: 123,
    commentCount: 38,
  },
  {
    title: "MEGA Sale 16.6.2024 săn hàng đẹp SIEEU PHAM",
    createdAt: "18:98 18/7/2024",
    likeCount: 123,
    commentCount: 38,
  },
  {
    title: "MEGA Sale 16.6.2024 săn hàng đẹp SIEEU PHAM",
    createdAt: "18:98 18/7/2024",
    likeCount: 123,
    commentCount: 38,
  },
  {
    title: "MEGA Sale 16.6.2024 săn hàng đẹp SIEEU PHAM",
    createdAt: "18:98 18/7/2024",
    likeCount: 123,
    commentCount: 38,
  },
];

interface IPostLiveListPopupProps {
  isOpen: boolean;
  onClose: () => void;
}

export const MergeLivePopup: FC<
  HTMLAttributes<HTMLDivElement> & IPostLiveListPopupProps
> = (props) => {
  const { isOpen, className, onClose, ...otherProps } = props;
  const classes = useStyles();
  const { fbUser } = useAppSelector((state) => state.auth);

  return (
    <Dialog
      {...otherProps}
      open={isOpen}
      onClose={onClose}
      TransitionComponent={Transition}
      PaperProps={{
        className: `${classes.root} ${className}`,
      }}
    >
      <Grid
        display="flex"
        justifyContent="space-between"
        gap={5}
        className={classes.header}
      >
        <Grid display="flex" alignItems="center" gap={2}>
          <Typography variant="body1" className={classes.title}>
            Gộp live
          </Typography>
          <Button
            color="primary"
            variant="contained"
            className={`${classes.btnReload} ${classes.btnHead}`}
            startIcon={<ReplayIcon />}
          >
            Làm mới
          </Button>
        </Grid>
        <IconButton
          edge="start"
          color="inherit"
          onClick={onClose}
          className={classes.closeIcon}
        >
          <CloseIcon />
        </IconButton>
      </Grid>
      <Grid display="flex" className={classes.content}>
        <div className={classes.list}>
          {map(fbUser, (fb, idx) => (
            <MergeLiveCard />
          ))}
        </div>
        <div className={classes.main}>
          {LIVES.map((live, idx) => (
            <LivePostCard livePost={live} isShowCheck key={idx} />
          ))}
          {/* <div className={classes.empty}>
            <Typography variant="body2">
              Tài khoản này hiện không có bài live nào
            </Typography>
            <Button
              color="primary"
              variant="contained"
              className={`${classes.btnReload} ${classes.btnBottom}`}
              startIcon={<ReplayIcon />}
            >
              Làm mới danh sách
            </Button>
          </div> */}
        </div>
      </Grid>
      <Grid
        display="flex"
        alignItems="center"
        justifyContent="end"
        gap={1}
        className={classes.footer}
      >
        <Typography variant="caption" className={classes.textSelect}>
          Chọn từ 2 bài live để gộp live (Đã chọn: 0)
        </Typography>
        <Grid display="flex" gap={1}>
          <Button
            color="inherit"
            variant="outlined"
            className={classes.btnReload}
          >
            Huỷ
          </Button>
          <Button
            color="primary"
            variant="contained"
            className={classes.btnReload}
          >
            Gộp live
          </Button>
        </Grid>
      </Grid>
    </Dialog>
  );
};
