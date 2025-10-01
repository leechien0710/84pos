import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    gap: 15,
    height: "calc(100vh - 88px)",
    "@media(max-width: 600px)": {
      marginTop: theme.spacing(2),
      height: "calc(100vh - 72px)",
    },
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
  content: {
    padding: "0px 16px 16px 16px",
    height: "calc(100% - 95px)",
    overflowY: "scroll",
    "@media(max-width: 600px)": {
      padding: 0,
    },
  },
  liveDetail: {
    marginTop: theme.spacing(2),
  },
  closeIcon: {
    padding: "0px !important",
  },
}));
