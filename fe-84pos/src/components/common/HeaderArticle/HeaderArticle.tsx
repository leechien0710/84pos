import { HTMLAttributes, MouseEvent, FC, useState } from "react";
import { map, find } from "lodash-es";
import { Grid2 as Grid, Typography, Button } from "@mui/material";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import FilterAltIcon from "@mui/icons-material/FilterAlt";
import ReplayIcon from "@mui/icons-material/Replay";
import SearchIcon from "@mui/icons-material/Search";
import { ArticleType } from "../../../types/article";
import { ARTICLE_FILTER } from "../../../constants/article";
import {
  useStyles,
  Search,
  SearchIconWrapper,
  StyledInputBase,
} from "./HeaderArticle.style";

interface IHeaderArticleProps {
  onSearch?: (query: string) => void;
  onFilterArticle?: (type: ArticleType) => void;
  articleTypeValue: ArticleType;
}

export const HeaderArticle: FC<
  HTMLAttributes<HTMLDivElement> & IHeaderArticleProps
> = (props) => {
  const classes = useStyles();
  const {
    onSearch,
    onFilterArticle,
    articleTypeValue,
    className,
    ...otherProps
  } = props;
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);

  const handleClick = (event: MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const filterArticle = (type: ArticleType) => {
    handleClose();
    onFilterArticle?.(type);
  };

  return (
    <Grid
      {...otherProps}
      display="flex"
      alignItems="center"
      justifyContent="space-between"
      className={`${classes.root} ${className}`}
    >
      <Grid
        display="flex"
        alignItems="center"
        gap={2}
        className={classes.mainReload}
      >
        <Typography variant="h6" className={classes.textLong}>
          Danh sách bài viết
        </Typography>
        <Button
          variant="contained"
          startIcon={<ReplayIcon />}
          className={classes.btn}
        >
          <Typography
            variant="caption"
            className={`${classes.textLong} ${classes.reloadText}`}
          >
            Làm mới danh sách
          </Typography>
        </Button>
      </Grid>
      <Grid display="flex" gap={2} className={classes.mainFilter}>
        <div>
          <Button
            id="basic-button"
            aria-controls={open ? "basic-menu" : undefined}
            aria-haspopup="true"
            aria-expanded={open ? "true" : undefined}
            variant="outlined"
            color="inherit"
            onClick={handleClick}
            startIcon={<FilterAltIcon />}
            className={classes.btn}
          >
            <Typography
              variant="caption"
              className={`${classes.textLong} ${classes.reloadText}`}
            >
              {find(ARTICLE_FILTER, { id: articleTypeValue })?.title ||
                "Lọc bài viết"}
            </Typography>
          </Button>
          <Menu
            id="basic-menu"
            anchorEl={anchorEl}
            open={open}
            onClose={handleClose}
            MenuListProps={{
              "aria-labelledby": "basic-button",
            }}
          >
            {map(ARTICLE_FILTER, ({ title, id }) => (
              <MenuItem onClick={() => filterArticle(id)} key={id}>
                {title}
              </MenuItem>
            ))}
          </Menu>
        </div>
        <Search>
          <SearchIconWrapper>
            <SearchIcon />
          </SearchIconWrapper>
          <StyledInputBase
            placeholder="Tìm kiếm bài viết"
            inputProps={{ "aria-label": "search" }}
            onChange={(e) => onSearch?.(e.target.value)}
          />
        </Search>
      </Grid>
    </Grid>
  );
};
