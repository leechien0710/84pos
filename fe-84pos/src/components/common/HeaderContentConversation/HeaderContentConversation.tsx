import { FC, HTMLAttributes } from "react";
import { Grid2 as Grid, Avatar, Typography, Button } from "@mui/material";
import Call from "@mui/icons-material/Call";
import Assignment from "@mui/icons-material/Assignment";
import MoreVert from "@mui/icons-material/MoreVert";
import Description from "@mui/icons-material/Description";
import Launch from "@mui/icons-material/Launch";
import AccountIcon from "@mui/icons-material/AccountBox";
import { useHideRightMenu } from "../../../hooks/screen";
import { useStyles } from "./HeaderSelectConversation.style";

interface IHeaderContentConversationProps {
  onOpenDetail: () => void;
  username: string;
}

export const HeaderContentConversation: FC<
  HTMLAttributes<HTMLDivElement> & IHeaderContentConversationProps
> = (props) => {
  const { username, className, onOpenDetail, ...otherProps } = props;
  const classes = useStyles();
  const isHideRight = useHideRightMenu();

  return (
    <div {...otherProps} className={`${className} ${classes.root}`}>
      <Grid display="flex" alignItems="center" gap={1} className={classes.head}>
        <Avatar />
        <Grid
          display="flex"
          justifyContent="space-between"
          className={classes.username}
        >
          <Grid display="flex" alignItems="center">
            <Typography variant="body1" className={classes.name}>
              {username}
            </Typography>
            <Call className={classes.call} />
          </Grid>
          <Grid display="flex" gap={1}>
            <Button
              variant="outlined"
              color="inherit"
              className={classes.btnAction}
            >
              <Assignment className={classes.icon} />
            </Button>
            <Button
              variant="outlined"
              color="inherit"
              className={classes.btnAction}
            >
              <MoreVert className={classes.icon} />
            </Button>
            {isHideRight && (
              <Button
                variant="outlined"
                color="inherit"
                className={classes.btnAction}
                onClick={onOpenDetail}
              >
                <AccountIcon className={classes.icon} />
              </Button>
            )}
          </Grid>
        </Grid>
      </Grid>
      <Grid display="flex" gap={2} className={classes.postInfo}>
        <div className={classes.descSection}>
          <div className={classes.img} />
          <Grid
            display="flex"
            justifyContent="center"
            alignItems="center"
            className={classes.iconSection}
          >
            <Description className={classes.descIcon} />
          </Grid>
        </div>
        <div>
          <Typography variant="subtitle2" className={classes.title}>
            Cái này là tiêu đề của bài viết hoặc bài live: MEGA Sale 16.6.2024
            săn hàng đẹp SIEEU PHAM
          </Typography>
          <Grid display="flex" gap={0.5} className={classes.linkSection}>
            <Launch className={classes.lauchIcon} />
            <Typography variant="caption" className={classes.linkConnect}>
              Mở bài viết
            </Typography>
          </Grid>
        </div>
      </Grid>
    </div>
  );
};
