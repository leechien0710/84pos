import { HTMLAttributes, FC } from "react";
import dayjs from "dayjs";
import { Grid2 as Grid, Typography, Checkbox } from "@mui/material";
import LikeIcon from "@mui/icons-material/ThumbUp";
import CommentIcon from "@mui/icons-material/Comment";
import { ILivePost } from "../../../types/live";
import { ArticleType, ILivePostCard } from "../../../types/article";
import { StaticImage } from "../StaticImage";
import { useStyles } from "./LivePostCard.style";
import { getArticleIcon } from "../../../utils/article";

interface ILivePostCardProps {
  livePost: Partial<ILivePost> & Partial<ILivePostCard>;
  isShowCheck?: boolean;
}

export const LivePostCard: FC<
  HTMLAttributes<HTMLDivElement> & ILivePostCardProps
> = (props) => {
  const { livePost, isShowCheck, className, ...otherProps } = props;
  const classes = useStyles();

  return (
    <Grid
      {...otherProps}
      display="flex"
      alignItems="center"
      gap={2}
      className={`${classes.root} ${className}`}
    >
      {isShowCheck && <Checkbox />}
      <div className={classes.first}>
        {livePost?.fullPictureUrl ? (
          <StaticImage
            src={livePost?.fullPictureUrl}
            className={classes.gameImage}
          />
        ) : (
          <div className={classes.gameImage} />
        )}
        <Grid
          display="flex"
          justifyContent="center"
          alignItems="center"
          className={`${classes.iconLabel} ${
            livePost.statusType === "added_livetream" && classes.contentCamera
          } ${
            (livePost.statusType === "mobile_status_update" ||
              livePost.statusType === "added_video" ||
              livePost.statusType === "added_photos") &&
            classes.post
          }`}
        >
          <StaticImage
            src={getArticleIcon(livePost?.statusType as ArticleType).icon1x}
            src2x={getArticleIcon(livePost?.statusType as ArticleType).icon2x}
            src3x={getArticleIcon(livePost?.statusType as ArticleType).icon3x}
          />
        </Grid>
      </div>
      <div>
        <Typography variant="body1" className={classes.title}>
          {livePost?.title || livePost?.message}
        </Typography>
        <Typography variant="caption" className={classes.time}>
          {dayjs(livePost?.createdAt || livePost?.createdTime).format(
            "HH:mm DD/MM/YYYY"
          )}
        </Typography>
        <Grid
          display="flex"
          alignItems="center"
          gap={2}
          className={classes.actions}
        >
          <Grid display="flex" alignItems="center" gap={0.5}>
            <LikeIcon fontSize="small" />
            <Typography variant="body2">{livePost.likeCount}</Typography>
          </Grid>
          <Grid display="flex" alignItems="center" gap={0.5}>
            <CommentIcon fontSize="small" />
            <Typography variant="body2">{livePost.commentCount}</Typography>
          </Grid>
        </Grid>
      </div>
    </Grid>
  );
};
