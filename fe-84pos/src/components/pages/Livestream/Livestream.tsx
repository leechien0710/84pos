import { HTMLAttributes, FC, useState, useEffect } from "react";
import { map } from "lodash-es";
import {
  Grid2 as Grid,
  Dialog,
  AppBar,
  Toolbar,
  IconButton,
  Typography,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { HeaderLivestream } from "../../common/HeaderLivestream";
import { LiveCard } from "../../common/LiveCard";
import { SelectLiveSession } from "../../common/SelectLiveSession";
import { DetailLive } from "../../common/DetailLive";
import { useHideRightMenu } from "../../../hooks/screen";
import { EmptyLive } from "../../common/EmptyLive";
import { Transition } from "../../common/PopupTransition";
import { useStyles } from "./Livestream.style";

export const LIST_LIVES = [
  {
    username: "Công chúa bong bóng",
    avatar:
      "https://contentgrid.homedepot-static.com/hdus/en_US/DTCCOMNEW/Articles/discover-the-secret-language-of-flowers-2022-hero.jpeg",
    message: "Lấy cho chị chân váy đen",
    createdAt: "11/06/2024 15:03 PM",
    isHideStatus: true,
  },
  {
    username: "Công chúa bong bóng",
    avatar:
      "https://contentgrid.homedepot-static.com/hdus/en_US/DTCCOMNEW/Articles/discover-the-secret-language-of-flowers-2022-hero.jpeg",
    message: "Lấy cho chị chân váy đen",
    createdAt: "11/06/2024 15:03 PM",
    isHideStatus: false,
  },
  {
    username: "Công chúa bong bóng",
    avatar:
      "https://contentgrid.homedepot-static.com/hdus/en_US/DTCCOMNEW/Articles/discover-the-secret-language-of-flowers-2022-hero.jpeg",
    message: "Lấy cho chị chân váy đen",
    createdAt: "11/06/2024 15:03 PM",
    isHideStatus: false,
  },
  {
    username: "Công chúa bong bóng",
    avatar:
      "https://contentgrid.homedepot-static.com/hdus/en_US/DTCCOMNEW/Articles/discover-the-secret-language-of-flowers-2022-hero.jpeg",
    message: "Lấy cho chị chân váy đen",
    createdAt: "11/06/2024 15:03 PM",
    isHideStatus: false,
  },
  {
    username: "Công chúa bong bóng",
    avatar:
      "https://contentgrid.homedepot-static.com/hdus/en_US/DTCCOMNEW/Articles/discover-the-secret-language-of-flowers-2022-hero.jpeg",
    message: "Lấy cho chị chân váy đen",
    createdAt: "11/06/2024 15:03 PM",
    isHideStatus: false,
  },
  {
    username: "Công chúa bong bóng",
    avatar:
      "https://contentgrid.homedepot-static.com/hdus/en_US/DTCCOMNEW/Articles/discover-the-secret-language-of-flowers-2022-hero.jpeg",
    message: "Lấy cho chị chân váy đen",
    createdAt: "11/06/2024 15:03 PM",
    isHideStatus: false,
  },
  {
    username: "Công chúa bong bóng",
    avatar:
      "https://contentgrid.homedepot-static.com/hdus/en_US/DTCCOMNEW/Articles/discover-the-secret-language-of-flowers-2022-hero.jpeg",
    message: "Lấy cho chị chân váy đen",
    createdAt: "11/06/2024 15:03 PM",
    isHideStatus: false,
  },
  {
    username: "Công chúa bong bóng",
    avatar:
      "https://contentgrid.homedepot-static.com/hdus/en_US/DTCCOMNEW/Articles/discover-the-secret-language-of-flowers-2022-hero.jpeg",
    message: "Lấy cho chị chân váy đen",
    createdAt: "11/06/2024 15:03 PM",
    isHideStatus: false,
  },
  {
    username: "Công chúa bong bóng",
    avatar:
      "https://contentgrid.homedepot-static.com/hdus/en_US/DTCCOMNEW/Articles/discover-the-secret-language-of-flowers-2022-hero.jpeg",
    message: "Lấy cho chị chân váy đen",
    createdAt: "11/06/2024 15:03 PM",
    isHideStatus: false,
  },
];

export const Livestream: FC<HTMLAttributes<HTMLDivElement>> = () => {
  const classes = useStyles();
  const isHideRight = useHideRightMenu();
  const [messageId, setMessageId] = useState("");

  const onSetMessageId = (id: string) => {
    setMessageId(id);
  };

  const renderRightAction = (isShowSelect?: boolean) => (
    <div className={`${classes.section} ${classes.secondSection}`}>
      {isShowSelect && <SelectLiveSession />}
      <DetailLive className={classes.liveDetail} />
    </div>
  );

  useEffect(() => {
    setMessageId(isHideRight ? "" : LIST_LIVES[0].message);
  }, [isHideRight]);

  return (
    <Grid
      display="flex"
      justifyContent="space-between"
      className={classes.root}
    >
      <div className={`${classes.section} ${classes.mainSection}`}>
        <HeaderLivestream />
        <div className={classes.content}>
          {/* <EmptyLive type="comment" /> */}
          {map(LIST_LIVES, (live, idx) => (
            <LiveCard key={idx} {...live} onSetMessageId={onSetMessageId} />
          ))}
        </div>
      </div>
      {messageId !== "" && renderRightAction(true)}
      {isHideRight && messageId && (
        <Dialog
          fullScreen
          open
          onClose={() => onSetMessageId("")}
          TransitionComponent={(props) => (
            <Transition {...props} direction="left" />
          )}
        >
          <AppBar color="inherit" sx={{ position: "relative" }}>
            <Toolbar sx={{ display: "flex", justifyContent: "space-between" }}>
              <Typography variant="body1">Đóng</Typography>
              <IconButton
                edge="start"
                color="inherit"
                onClick={() => onSetMessageId("")}
                className={classes.closeIcon}
              >
                <CloseIcon />
              </IconButton>
            </Toolbar>
          </AppBar>
          {renderRightAction()}
        </Dialog>
      )}
    </Grid>
  );
};
