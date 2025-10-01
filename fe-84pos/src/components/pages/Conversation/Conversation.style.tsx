import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  main: {
    marginTop: theme.spacing(2),
  },
  contentLoading: {
    height: "100%",
  },
  btn: {
    textTransform: "none !important" as any,
  },
  inActive: {
    backgroundColor: `${theme.palette.background.default} !important`,
    color: "unset !important",
  },
  section: {
    background: theme.palette.background.paper,
    borderRadius: 4,
  },
  mainSection: {
    flex: 4,
  },
  secondSection: {
    flex: 2,
    background: "transparent",
  },
  hasBottomBorder: {
    borderBottom: "1px solid #D1D1D1",
  },
  listMess: {
    borderRight: "1px solid #D1D1D1",
    height: "calc(100vh - 139.5px)",
    flex: 0.7,
  },
  content: {
    height: "calc(100% - 115.5px)",
    overflowY: "scroll",
    "-ms-overflow-style": "none",
    scrollbarWidth: "none",
    "&::-webkit-scrollbar": {
      display: "none",
    },
  },
  detail: {
    flex: 1,
    height: "calc(100vh - 139.5px)",
  },
  mainChat: {
    height: "calc(100% - 205px)",
  },
  closeIcon: {
    padding: "0px !important",
  },
  fullWidth: {
    width: "100%",
    minWidth: "100%",
  },
  actionsSpace: {
    marginTop: theme.spacing(2),
    marginLeft: theme.spacing(2),
  },
}));
