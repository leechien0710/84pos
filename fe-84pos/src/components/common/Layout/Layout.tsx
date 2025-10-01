import React, { HTMLAttributes, FC } from "react";
import { useLocation } from "react-router-dom";
import Box from "@mui/material/Box";
import MuiDrawer from "@mui/material/Drawer";
import Toolbar from "@mui/material/Toolbar";
import Button from "@mui/material/Button";
import List from "@mui/material/List";
import CssBaseline from "@mui/material/CssBaseline";
import Divider from "@mui/material/Divider";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import MiniLogo from "../../../assets/app-bar/mini-logo.webp";
import MiniLogo2x from "../../../assets/app-bar/mini-logo@2x.webp";
import MiniLogo3x from "../../../assets/app-bar/mini-logo@3x.webp";
import Logo from "../../../assets/app-bar/logo.webp";
import Logo2x from "../../../assets/app-bar/logo@2x.webp";
import Logo3x from "../../../assets/app-bar/logo@3x.webp";
import { useDeviceType } from "../../../hooks/screen";
import { StaticImage } from "../../common/StaticImage";
import { DRAWER_WIDTH } from "../../../constants/layout";
import { MenuItem } from "../MenuItem";
import { AccountSection } from "../AccountSection";
import { useStyles, DrawerHeader, Drawer, AppBar } from "./Layout.style";
import { isHideAppBar } from "../../../utils/layout";

export const Layout: FC<HTMLAttributes<HTMLDivElement>> = (props) => {
  const classes = useStyles();
  const { children } = props;
  const [open, setOpen] = React.useState(false);
  const container =
    window !== undefined ? () => window.document.body : undefined;
  const isMobile = useDeviceType();
  const location = useLocation();

  const handleDrawerClose = () => {
    setOpen(false);
  };

  const onToggleMenu = () => {
    setOpen((prevOpen) => !prevOpen);
  };

  if (isHideAppBar(location.pathname)) {
    return <>{children}</>;
  }

  return (
    <Box sx={{ display: "flex" }}>
      <CssBaseline />
      <AppBar
        position="fixed"
        open={open}
        className={`${!open && classes.appBar} `}
      >
        <Toolbar className={classes.toolBar}>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            onClick={onToggleMenu}
            edge="start"
            sx={[{ marginRight: 5 }]}
          >
            <MenuIcon color="action" />
          </IconButton>
          <AccountSection />
        </Toolbar>
      </AppBar>
      {isMobile ? (
        <MuiDrawer
          container={container}
          variant="temporary"
          open={open}
          onClose={handleDrawerClose}
          ModalProps={{
            keepMounted: true,
          }}
          sx={{
            display: { xs: "block", sm: "none" },
            "& .MuiDrawer-paper": {
              boxSizing: "border-box",
              width: DRAWER_WIDTH,
            },
          }}
        >
          <DrawerHeader className={`${classes.contentLogo} ${open && "open"}`}>
            <StaticImage src={Logo} src2x={Logo2x} src3x={Logo3x} />{" "}
            <Button variant="contained" className={classes.btnTry}>
              Dùng thử
            </Button>
          </DrawerHeader>
          <List>
            <MenuItem open={open} onClose={onToggleMenu} />
          </List>
        </MuiDrawer>
      ) : (
        <Drawer variant="permanent" open={open}>
          <DrawerHeader className={`${classes.contentLogo} ${open && "open"}`}>
            <StaticImage
              src={open ? Logo : MiniLogo}
              src2x={open ? Logo2x : MiniLogo2x}
              src3x={open ? Logo3x : MiniLogo3x}
              className={`${!open && classes.miniLogo}`}
            />
            {open && (
              <Button variant="contained" className={classes.btnTry}>
                Dùng thử
              </Button>
            )}
          </DrawerHeader>
          <Divider />
          <List>
            <MenuItem open={open} onClose={onToggleMenu} />
          </List>
        </Drawer>
      )}
      <Box
        component="main"
        sx={{ flexGrow: 1, p: 3 }}
        className={classes.wrapper}
      >
        <DrawerHeader />
        <div>{children}</div>
      </Box>
    </Box>
  );
};
