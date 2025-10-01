import { HTMLAttributes, FC } from "react";
import { Grid2 as Grid, Typography } from "@mui/material";
import { EmptyType } from "../../../types/live";
import { StaticImage } from "../StaticImage";
import EmptyComment from "../../../assets/livestream/empty-comment.webp";
import EmptyComment2x from "../../../assets/livestream/empty-comment@2x.webp";
import EmptyComment3x from "../../../assets/livestream/empty-comment@3x.webp";
import EmptyOrder from "../../../assets/livestream/empty-order.webp";
import EmptyOrder2x from "../../../assets/livestream/empty-order@2x.webp";
import EmptyOrder3x from "../../../assets/livestream/empty-order@3x.webp";
import { useStyles } from "./EmptyLive.style";

interface IEmptyLiveProps {
  type: EmptyType;
}

export const EmptyLive: FC<HTMLAttributes<HTMLDivElement> & IEmptyLiveProps> = (
  props
) => {
  const { type, className, ...otherProps } = props;
  const classes = useStyles();

  return (
    <Grid
      display="flex"
      flexDirection="column"
      justifyContent="center"
      alignItems="center"
      {...otherProps}
      className={`${classes.root} ${className}`}
    >
      <StaticImage
        src={type === "comment" ? EmptyComment : EmptyOrder}
        src2x={type === "comment" ? EmptyComment2x : EmptyOrder2x}
        src3x={type === "comment" ? EmptyComment3x : EmptyOrder3x}
        className={classes.img}
      />
      <Typography variant="body2">
        {type === "comment"
          ? "Phiên live chưa có bình luận nào"
          : "Chưa có đơn hàng nào"}
      </Typography>
    </Grid>
  );
};
