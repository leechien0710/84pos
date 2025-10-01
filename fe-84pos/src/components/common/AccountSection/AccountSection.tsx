import { HTMLAttributes, FC, useState } from "react";
import { useNavigate } from "react-router-dom";
import Button from "@mui/material/Button";
import Badge from "@mui/material/Badge";
import Avatar from "@mui/material/Avatar";
import MenuItem from "@mui/material/MenuItem";
import ArrowDown from "@mui/icons-material/ArrowDropDown";
import ArrowUp from "@mui/icons-material/ArrowDropUp";
import { StaticImage } from "../StaticImage";
import Facebook from "../../../assets/sign-in/facebook.webp";
import Facebook2x from "../../../assets/sign-in/facebook@2x.webp";
import Facebook3x from "../../../assets/sign-in/facebook@3x.webp";
import Logout from "../../../assets/app-bar/logout.webp";
import Logout2x from "../../../assets/app-bar/logout@2x.webp";
import Logout3x from "../../../assets/app-bar/logout@3x.webp";
import { useAppDispatch, useAppSelector } from "../../../hook";
import { logout } from "../../../slices/auth";
import { useStyles, StyledMenu } from "./AccountSection.style";
import { redirectToHome } from "../../../utils/redirect";

export const AccountSection: FC<HTMLAttributes<HTMLDivElement>> = (props) => {
  const classes = useStyles();
  const { user, fbUser } = useAppSelector((state) => state.auth);
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = useState<HTMLButtonElement | null>(null);
  const open = Boolean(anchorEl);
  const id = open ? "simple-popover" : undefined;

  const handleOpen = (event: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const onLogout = () => {
    dispatch(logout());
    redirectToHome(navigate);
  };

  return (
    <>
      <Button
        variant="text"
        className={classes.btnAccount}
        onClick={handleOpen}
        startIcon={
          <Badge
            overlap="circular"
            anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
            badgeContent={
              <StaticImage
                src={Facebook}
                src2x={Facebook2x}
                src3x={Facebook3x}
              />
            }
          >
            <Avatar alt="Avatar" src="" />
          </Badge>
        }
        endIcon={
          open ? (
            <ArrowUp className={classes.arrow} />
          ) : (
            <ArrowDown className={classes.arrow} />
          )
        }
      >
        {user?.username}
      </Button>
      <StyledMenu id={id} anchorEl={anchorEl} open={open} onClose={handleClose}>
        <MenuItem onClick={onLogout} disableRipple className={classes.logout}>
          <StaticImage src={Logout} src2x={Logout2x} src3x={Logout3x} />
          Đăng xuất
        </MenuItem>
      </StyledMenu>
    </>
  );
};
