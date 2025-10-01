import { Theme, styled, CSSObject } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";
import MuiDrawer from "@mui/material/Drawer";
import MuiAppBar, { AppBarProps as MuiAppBarProps } from "@mui/material/AppBar";
import { DRAWER_WIDTH } from "../../../constants/layout";

interface AppBarProps extends MuiAppBarProps {
  open?: boolean;
}

export const useStyles = makeStyles((theme: Theme) => ({
  root: {},
  appBar: {
    "@media(min-width: 600px)": {
      left: `calc(${theme.spacing(7)} + 9px) !important`,
      width: "calc(100% - 65px) !important",
    },
  },
  wrapper: {
    background: "#F2F2F2",
    height: "100vh",
    "@media(max-width: 600px)": {
      padding: "0px !important",
    },
    overflow: "hidden",
  },
  contentLogo: {
    display: "flex !important",
    justifyContent: "center !important",
    alignItems: "center !important",
    "&.open": {
      justifyContent: "space-between !important",
    },
  },
  miniLogo: {
    height: 63,
    width: 63,
  },
  btnTry: {
    background: `${theme.palette.common.black} !important`,
    textTransform: "none !important" as any,
    borderRadius: "30px !important",
  },
  toolBar: {
    justifyContent: "space-between",
  },
}));

export const DrawerHeader = styled("div")(({ theme }) => ({
  display: "flex",
  alignItems: "center",
  justifyContent: "flex-end",
  padding: theme.spacing(0, 1),
  ...theme.mixins.toolbar,
}));

export const Drawer = styled(MuiDrawer, {
  shouldForwardProp: (prop) => prop !== "open",
})(({ theme }) => ({
  width: DRAWER_WIDTH,
  flexShrink: 0,
  whiteSpace: "nowrap",
  boxSizing: "border-box",
  variants: [
    {
      props: ({ open }) => open,
      style: {
        ...openedMixin(theme),
        "& .MuiDrawer-paper": openedMixin(theme),
      },
    },
    {
      props: ({ open }) => !open,
      style: {
        ...closedMixin(theme),
        "& .MuiDrawer-paper": closedMixin(theme),
      },
    },
  ],
}));

export const AppBar = styled(MuiAppBar, {
  shouldForwardProp: (prop) => prop !== "open",
})<AppBarProps>(({ theme }) => ({
  zIndex: theme.zIndex.drawer + 1,
  transition: theme.transitions.create(["width", "margin"], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  background: theme.palette.background.paper,
  boxShadow: "none",
  variants: [
    {
      props: ({ open }) => open,
      style: {
        marginLeft: DRAWER_WIDTH,
        width: `calc(100% - ${DRAWER_WIDTH}px)`,
        transition: theme.transitions.create(["width", "margin"], {
          easing: theme.transitions.easing.sharp,
          duration: theme.transitions.duration.enteringScreen,
        }),
      },
    },
  ],
}));

const openedMixin = (theme: Theme): CSSObject => ({
  width: DRAWER_WIDTH,
  transition: theme.transitions.create("width", {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.enteringScreen,
  }),
  overflowX: "hidden",
});

const closedMixin = (theme: Theme): CSSObject => ({
  transition: theme.transitions.create("width", {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  overflowX: "hidden",
  width: `calc(${theme.spacing(7)} + 1px)`,
  [theme.breakpoints.up("sm")]: {
    width: `calc(${theme.spacing(8)} + 1px)`,
  },
});
