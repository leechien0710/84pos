import { HTMLAttributes, FC } from "react";
import {
  Grid2 as Grid,
  Typography,
  Dialog,
  IconButton,
  Button,
} from "@mui/material";
import { map } from "lodash-es";
import CloseIcon from "@mui/icons-material/Close";
import ReplayIcon from "@mui/icons-material/Replay";
import { LivePostCard } from "../../LivePostCard";
import { Transition } from "../../PopupTransition";
import { useStyles } from "./PostLiveListPopup.style";

const LIVE_POST = [
  {
    title: "MEGA Sale 16.6.2024 săn hàng đẹp SIEEU PHAM",
    createdAt: "18:98 18/7/2024",
    likeCount: 123,
    commentCount: 68,
  },
  {
    title: "MEGA Sale 16.6.2024 săn hàng đẹp SIEEU PHAM",
    createdAt: "18:98 18/7/2024",
    likeCount: 123,
    commentCount: 68,
  },
  {
    title: "MEGA Sale 16.6.2024 săn hàng đẹp SIEEU PHAM",
    createdAt: "18:98 18/7/2024",
    likeCount: 123,
    commentCount: 68,
  },
  {
    title: "MEGA Sale 16.6.2024 săn hàng đẹp SIEEU PHAM",
    createdAt: "18:98 18/7/2024",
    likeCount: 123,
    commentCount: 68,
  },
  {
    title: "MEGA Sale 16.6.2024 săn hàng đẹp SIEEU PHAM",
    createdAt: "18:98 18/7/2024",
    likeCount: 123,
    commentCount: 68,
  },
  {
    title: "MEGA Sale 16.6.2024 săn hàng đẹp SIEEU PHAM",
    createdAt: "18:98 18/7/2024",
    likeCount: 123,
    commentCount: 68,
  },
  {
    title: "MEGA Sale 16.6.2024 săn hàng đẹp SIEEU PHAM",
    createdAt: "18:98 18/7/2024",
    likeCount: 123,
    commentCount: 68,
  },
  {
    title: "MEGA Sale 16.6.2024 săn hàng đẹp SIEEU PHAM",
    createdAt: "18:98 18/7/2024",
    likeCount: 123,
    commentCount: 68,
  },
];

interface IPostLiveListPopupProps {
  isOpen: boolean;
  onClose: () => void;
}

export const PostLiveListPopup: FC<
  HTMLAttributes<HTMLDivElement> & IPostLiveListPopupProps
> = (props) => {
  const { isOpen, className, onClose, ...otherProps } = props;
  const classes = useStyles();

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
            Danh sách bài live
          </Typography>
          <Button
            color="primary"
            variant="contained"
            className={classes.btnReload}
            startIcon={<ReplayIcon />}
          >
            Làm mới danh sách
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
      <div className={classes.content}>
        {map(LIVE_POST, (lp, idx) => (
          <LivePostCard livePost={lp} key={idx} className={classes.postCard} />
        ))}
      </div>
    </Dialog>
  );
};
