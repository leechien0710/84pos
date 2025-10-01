import { HTMLAttributes, FC, useState, MouseEvent } from "react";
import { Grid2 as Grid, Typography, Button } from "@mui/material";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import { useStyles } from "./SelectLiveSession.style";

export const SelectLiveSession: FC<HTMLAttributes<HTMLDivElement>> = (
  props
) => {
  const { className, ...otherProps } = props;
  const classes = useStyles();
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);

  const handleClick = (event: MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  return (
    <Grid
      {...otherProps}
      display="flex"
      gap={2}
      className={`${className}, ${classes.root}`}
    >
      <div className={classes.img} />
      <div className={classes.content}>
        <Grid display="flex" justifyContent="space-between">
          <Button variant="contained" color="error" className={classes.btnLive}>
            <div className={classes.circle} />
            &nbsp;Live
          </Button>
          <Button
            id="basic-button"
            aria-controls={open ? "basic-menu" : undefined}
            aria-haspopup="true"
            aria-expanded={open ? "true" : undefined}
            variant="outlined"
            color="inherit"
            onClick={handleClick}
            endIcon={<KeyboardArrowDownIcon />}
            className={classes.select}
          >
            Chọn phiên live
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
            <MenuItem onClick={handleClose}>Phiên live 1</MenuItem>
            <MenuItem onClick={handleClose}>Phiên live 2</MenuItem>
            <MenuItem onClick={handleClose}>Phiên live 3</MenuItem>
          </Menu>
        </Grid>
        <Typography variant="body2" className={classes.title}>
          MEGA Sale 16.6.2024 săn hàng đẹp
        </Typography>
      </div>
    </Grid>
  );
};
