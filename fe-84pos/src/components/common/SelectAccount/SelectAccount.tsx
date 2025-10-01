import { HTMLAttributes, FC, useState, useEffect, MouseEvent } from "react";
import { map, find } from "lodash-es";
import {
  Grid2 as Grid,
  Typography,
  Checkbox,
  Badge,
  Avatar,
  Button,
  Popover,
} from "@mui/material";
import DownIcon from "@mui/icons-material/ArrowDropDown";
import { StaticImage } from "../StaticImage";
import Facebook from "../../../assets/sign-in/facebook.webp";
import Facebook2x from "../../../assets/sign-in/facebook@2x.webp";
import Facebook3x from "../../../assets/sign-in/facebook@3x.webp";
import { useAppSelector } from "../../../hook";
import { useStyles } from "./SelectAccount.style";

interface ISelectAccountProps {
  onFetchConversation: (pageId: string, offsetCustom?: number) => Promise<void>;
}

export const SelectAccount: FC<
  HTMLAttributes<HTMLDivElement> & ISelectAccountProps
> = (props) => {
  const { className, onFetchConversation, ...otherProps } = props;
  const classes = useStyles();
  const [pageSelected, setPageSelected] = useState<string>("");
  const [anchorEl, setAnchorEl] = useState<HTMLButtonElement | null>(null);
  const { fbUser } = useAppSelector((state) => state.auth);

  const handleClick = (event: MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const onChangePage = async (pageId: string) => {
    setPageSelected(pageId);
    handleClose();
    await onFetchConversation(pageId, 0);
  };

  useEffect(() => {
    if (!fbUser?.length) return;
    const pageDefault = fbUser?.[1].pages?.[0]?.pageId || "";
    onChangePage(pageDefault);
  }, [fbUser]);

  return (
    <div {...otherProps} className={`${classes.root} ${className}`}>
      <Button
        variant="contained"
        color="inherit"
        onClick={handleClick}
        endIcon={<DownIcon />}
        className={classes.btnSelect}
        fullWidth
      >
        <Typography className={classes.textLong}>
          {!pageSelected
            ? "Chọn page để xem hội thoại"
            : find(fbUser?.[1].pages, { pageId: pageSelected })?.pageName}
        </Typography>
      </Button>
      <Popover
        open={Boolean(anchorEl)}
        anchorEl={anchorEl}
        onClose={handleClose}
        anchorOrigin={{
          vertical: "bottom",
          horizontal: "left",
        }}
      >
        <div className={classes.content}>
          <Typography variant="body1" className={classes.label}>
            Facebook
          </Typography>
          <div>
            {map(fbUser?.[1].pages, (page, idx) => (
              <Grid
                display="flex"
                alignItems="center"
                className={classes.row}
                key={idx}
              >
                <Checkbox
                  checked={pageSelected === page?.pageId}
                  onChange={() => onChangePage(page?.pageId)}
                />
                <Badge
                  overlap="circular"
                  anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
                  badgeContent={
                    <StaticImage
                      src={Facebook}
                      src2x={Facebook2x}
                      src3x={Facebook3x}
                      className={classes.platform}
                    />
                  }
                >
                  <Avatar
                    alt="Avatar"
                    src={page?.pageAvatarUrl}
                    className={classes.avatar}
                  />
                </Badge>
                <Typography
                  variant="body1"
                  className={`${classes.label} ${classes.title}`}
                >
                  {page?.pageName}
                </Typography>
              </Grid>
            ))}
          </div>
        </div>
      </Popover>
    </div>
  );
};
