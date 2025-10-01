import { HTMLAttributes, FC, useState } from "react";
import { Grid2 as Grid, Typography, Button } from "@mui/material";
import ArrowDown from "@mui/icons-material/KeyboardArrowDown";
import SearchIcon from "@mui/icons-material/Search";
import { PostLiveListPopup } from "../Popups/PostLiveListPopup";
import { MergeLivePopup } from "../Popups/MergeLivePopup";
import { FilterTag } from "../FilterTag";
import {
  useStyles,
  Search,
  SearchIconWrapper,
  StyledInputBase,
} from "./HeaderLivestream.style";

export const HeaderLivestream: FC<HTMLAttributes<HTMLDivElement>> = (props) => {
  const { className, ...otherProps } = props;
  const classes = useStyles();
  const [isOpen, setIsOpen] = useState(false);
  const [isOpenMerge, setIsOpenMerge] = useState(false);

  const onToggle = () => {
    setIsOpen(!isOpen);
  };

  const onToggleMerge = () => {
    setIsOpenMerge(!isOpenMerge);
  };

  return (
    <Grid
      {...otherProps}
      display="flex"
      justifyContent="space-between"
      alignItems="center"
      className={`${classes.root} ${className}`}
    >
      <div>
        <Grid display="flex" alignItems="center" gap={1}>
          <Typography variant="caption" className={classes.title}>
            Lựa chọn phiên live để theo dõi:
          </Typography>
          <Button
            variant="contained"
            className={classes.mergeLive}
            onClick={onToggleMerge}
          >
            Gộp live
          </Button>
        </Grid>
        <Grid
          display="flex"
          alignItems="center"
          gap={1}
          className={classes.liveInfo}
        >
          <Button variant="contained" color="error" className={classes.btnLive}>
            <div className={classes.circle} />
            &nbsp;Live
          </Button>
          <Typography variant="body2">
            MEGA Sale 16.6.2024 săn hàng đẹp
          </Typography>
          <ArrowDown onClick={onToggle} />
        </Grid>
      </div>
      <Grid display="flex" gap={1} className={classes.actions}>
        <FilterTag />
        <Search>
          <SearchIconWrapper>
            <SearchIcon />
          </SearchIconWrapper>
          <StyledInputBase
            placeholder="Tìm kiếm bình luận"
            inputProps={{ "aria-label": "search" }}
          />
        </Search>
      </Grid>
      <PostLiveListPopup isOpen={isOpen} onClose={onToggle} />
      <MergeLivePopup isOpen={isOpenMerge} onClose={onToggleMerge} />
    </Grid>
  );
};
