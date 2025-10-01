import { HTMLAttributes, FC } from "react";
import {
  Grid2 as Grid,
  TextField,
  Button,
  Typography,
  CircularProgress,
} from "@mui/material";
import { map, flattenDepth } from "lodash-es";
import SendIcon from "@mui/icons-material/Send";
import InfiniteScroll from "react-infinite-scroll-component";
import { MessageCard } from "../MessageCard";
import AttachFileIcon from "@mui/icons-material/AttachFile";
import CheckroomIcon from "@mui/icons-material/Checkroom";
import LightbulbIcon from "@mui/icons-material/Lightbulb";
import ImageIcon from "@mui/icons-material/Image";
import { LIVE_LABELS } from "../../../constants/live";
import { useStyles } from "./ChatSection.style";
import { useDeviceType } from "../../../hooks/screen";
import { IChat } from "../../../types/conversation";

interface IChatSectionProps {
  hasHeader?: boolean;
  messages?: IChat;
  onLoadMore?: () => Promise<void>;
}

export const ChatSection: FC<
  HTMLAttributes<HTMLDivElement> & IChatSectionProps
> = (props) => {
  const classes = useStyles();
  const { hasHeader, messages, onLoadMore } = props;
  const isMobile = useDeviceType();

  return (
    <>
      <div
        className={`${classes.content} ${
          !hasHeader && !isMobile && classes.contentNoHeader
        } ${!hasHeader && isMobile && classes.contentMobile}`}
        id="content-messages"
      >
        <InfiniteScroll
          dataLength={flattenDepth(Object.values(messages || {})).length}
          next={() => onLoadMore?.()}
          style={{ display: "flex", flexDirection: "column" }}
          inverse={false}
          hasMore={true}
          loader={
            <Grid
              display="flex"
              justifyContent="center"
              alignItems="center"
              marginTop={2}
            >
              <CircularProgress />
            </Grid>
          }
          scrollableTarget="content-messages"
        >
          {map(messages, (message, key) => (
            <div key={key}>
              <Typography className={classes.date}>{key}</Typography>
              <div>
                {map(message, (m) => (
                  <MessageCard
                    className={classes.messCard}
                    avatar="https://contentgrid.homedepot-static.com/hdus/en_US/DTCCOMNEW/Articles/discover-the-secret-language-of-flowers-2022-hero.jpeg"
                    {...m}
                  />
                ))}
              </div>
            </div>
          ))}
        </InfiniteScroll>
      </div>
      <div className={classes.footer}>
        <div className={classes.inputSection}>
          <TextField
            placeholder="Aa..."
            multiline
            rows={2}
            variant="outlined"
            fullWidth
            InputProps={{
              sx: {
                "& fieldset": { border: "none" },
              },
            }}
          />
          <Button
            variant="contained"
            color="primary"
            className={classes.btnSend}
          >
            <SendIcon className={classes.sendIcon} />
          </Button>
        </div>
        <Grid display="flex" justifyContent="space-between">
          <Grid display="flex" alignItems="end">
            {map(LIVE_LABELS, (color) => (
              <div
                style={{ backgroundColor: color }}
                className={classes.label}
                key={color}
              />
            ))}
          </Grid>
          <Grid display="flex" gap={1} className={classes.actions}>
            <AttachFileIcon color="action" />
            <CheckroomIcon color="action" />
            <LightbulbIcon color="action" />
            <ImageIcon color="action" />
          </Grid>
        </Grid>
      </div>
    </>
  );
};
