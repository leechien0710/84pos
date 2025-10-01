import { HTMLAttributes, FC } from "react";
import { map } from "lodash-es";
import { Grid2 as Grid, Avatar, Typography } from "@mui/material";
import Call from "@mui/icons-material/Call";
import Reply from "@mui/icons-material/UTurnLeft";
import Message from "@mui/icons-material/Message";
import { useStyles } from "./ConversationCard.style";
import { COLOR_LIST } from "../../../constants/conversation";
import { IConversation } from "../../../types/conversation";

interface IConversationCardProps {
  conversation: IConversation;
}

export const ConversationCard: FC<
  HTMLAttributes<HTMLDivElement> & IConversationCardProps
> = (props) => {
  const { conversation, className, ...otherProps } = props;
  const classes = useStyles();

  return (
    <Grid
      {...otherProps}
      display="flex"
      alignItems="center"
      gap={1}
      className={`${className} ${classes.root}`}
    >
      <Avatar src="/" />
      <Grid display="flex" flex={1} justifyContent="space-between" gap={1}>
        <div>
          <Grid display="flex" alignItems="center">
            <Typography variant="body2" className={classes.textLong}>
              {conversation?.senders?.data?.[0]?.name}
            </Typography>
            <Call className={classes.call} />
            {conversation?.unread_count > 0 && (
              <Typography variant="caption" className={classes.missMess}>
                {conversation?.unread_count}
              </Typography>
            )}
          </Grid>
          <Grid
            display="flex"
            alignItems="center"
            gap={0.5}
            className={classes.replyMessage}
          >
            <Reply className={classes.reply} />
            <Typography
              variant="caption"
              className={`${classes.subMess} ${classes.textLong}`}
            >
              {conversation?.snippet ||
                "Bạn không thể gửi tin nhắn cho người này nữa"}
            </Typography>
          </Grid>
          <Grid display="flex">
            {map(COLOR_LIST, (color, idx) => (
              <div
                key={idx}
                className={classes.color}
                style={{ backgroundColor: color }}
              />
            ))}
          </Grid>
        </div>
        <Grid display="flex" flexDirection="column" alignItems="end">
          <Typography variant="caption" className={classes.date}>
            {new Date(conversation?.updated_time).toLocaleDateString("en-GB")}
          </Typography>
          <Message className={classes.message} />
        </Grid>
      </Grid>
    </Grid>
  );
};
