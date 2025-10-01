import { FC, useState, MouseEvent, HTMLAttributes } from "react";
import { map } from "lodash";
import {
  Grid2 as Grid,
  Button,
  MenuItem,
  Checkbox,
  Typography,
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import FilterIcon from "@mui/icons-material/FilterAlt";
import { SelectAccount } from "../SelectAccount";
import { FilterTag } from "../FilterTag";
import { LIST_FILTER } from "../../../constants/conversation";
import {
  useStyles,
  Search,
  SearchIconWrapper,
  StyledInputBase,
  StyledMenu,
} from "./HeaderSelectConversation.style";

interface IHeaderSelectConversationProps {
  onFetchConversation: (pageId: string, offsetCustom?: number) => Promise<void>;
}

export const HeaderSelectConversation: FC<
  HTMLAttributes<HTMLDivElement> & IHeaderSelectConversationProps
> = (props) => {
  const { className, onFetchConversation, ...otherProps } = props;
  const classes = useStyles();
  const [anchorElFilter, setAnchorElFilter] =
    useState<HTMLButtonElement | null>(null);

  const handleClickFilter = (event: MouseEvent<HTMLButtonElement>) => {
    setAnchorElFilter(event.currentTarget);
  };

  const handleCloseFilter = () => {
    setAnchorElFilter(null);
  };

  return (
    <div {...otherProps} className={`${className} ${classes.root}`}>
      <SelectAccount onFetchConversation={onFetchConversation} />
      <Grid display="flex" gap={1} className={classes.actions}>
        <Search>
          <SearchIconWrapper>
            <SearchIcon />
          </SearchIconWrapper>
          <StyledInputBase
            placeholder="Tìm kiếm bài viết"
            inputProps={{ "aria-label": "search" }}
          />
        </Search>
        <FilterTag isHideLabel className={classes.btnAction} />
        <Button
          variant="outlined"
          color="inherit"
          className={`${classes.btnAction} ${
            Boolean(anchorElFilter) && classes.activeBorder
          }`}
          onClick={handleClickFilter}
        >
          <FilterIcon
            className={`${classes.icon} ${
              Boolean(anchorElFilter) && classes.active
            }`}
          />
        </Button>
        <StyledMenu
          anchorEl={anchorElFilter}
          open={Boolean(anchorElFilter)}
          onClose={handleCloseFilter}
        >
          {map(LIST_FILTER, (filter, idx) => (
            <MenuItem disableRipple key={idx}>
              <Checkbox className={classes.checkBox} />
              <Typography>{filter}</Typography>
            </MenuItem>
          ))}
        </StyledMenu>
      </Grid>
    </div>
  );
};
