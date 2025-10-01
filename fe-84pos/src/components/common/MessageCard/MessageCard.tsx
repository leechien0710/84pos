import { HTMLAttributes, FC } from "react";
import { Grid2 as Grid, Typography, Avatar } from "@mui/material";
import Print from "@mui/icons-material/Print";
import { useStyles } from "./MessageCard.style";

interface IMessageCardProps {
  isMe?: boolean;
  message?: string;
  time?: string;
  avatar?: string;
  type?: "image" | "video";
}

export const MessageCard: FC<
  HTMLAttributes<HTMLDivElement> & IMessageCardProps
> = (props) => {
  const { isMe, message, time, avatar, type, className, ...otherProps } = props;
  const classes = useStyles();

  return (
    <Grid
      {...otherProps}
      display="flex"
      alignItems="center"
      flexDirection={isMe ? "row-reverse" : "row"}
      gap={1}
      className={`${isMe ? classes.me : ""} ${className} ${classes.root}`}
    >
      <Avatar src={avatar} alt="avatar" className={classes.avatar} />
      {type !== "image" && type !== "video" && (
        <Typography
          variant="body2"
          className={`${classes.mess} ${isMe ? classes.messMe : ""}`}
        >
          {message}
        </Typography>
      )}
      {type === "image" && (
        <img
          src={message}
          alt="image-data"
          className={`${classes.mess} ${isMe ? classes.messMe : ""} ${
            classes.imgAttach
          }`}
        />
      )}
      {type === "video" && (
        <video
          className={`${classes.mess} ${isMe ? classes.messMe : ""} ${
            classes.imgAttach
          }`}
          muted
          autoPlay
          loop={true}
          controls
          disablePictureInPicture
          disableRemotePlayback
          controlsList="nodownload noremoteplayback nofullscreen noplaybackrate nofullscreen"
          playsInline={true}
        >
          <source src={message} type="video/mp4" />
        </video>
      )}
      <Print fontSize="small" color="action" />
      <Typography variant="caption">{time}</Typography>
    </Grid>
  );
};
