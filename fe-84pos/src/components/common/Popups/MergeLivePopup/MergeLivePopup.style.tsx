import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    borderRadius: "16px !important",
    position: "relative !important" as any,
    maxWidth: "fit-content !important",
  },
  header: {
    padding: theme.spacing(2),
    borderBottom: "1px solid #D1D1D1",
    backgroundColor: theme.palette.background.paper,
    borderTopLeftRadius: "16px !important",
    borderTopRightRadius: "16px !important",
  },
  closeIcon: {
    padding: "0px !important",
  },
  title: {
    fontWeight: "500 !important",
    "-webkit-line-clamp": 1,
    "-webkit-box-orient": "vertical",
    overflow: "hidden",
    display: "-webkit-box",
    wordBreak: "break-all",
  },
  content: {
    backgroundColor: "#f8f8f8",
    overflowY: "scroll",
    borderBottom: "1px solid #D1D1D1",
    height: "calc(100% - 125px)",
    "@media(max-width: 600px)": {
      flexDirection: "column",
    },
  },
  btnReload: {
    height: 36,
    fontSize: "12px !important",
    fontWeight: "500 !important",
    textTransform: "initial !important" as any,
  },
  btnHead: {
    "@media(min-width: 600px)": {
      display: "none !important",
    },
  },
  btnBottom: {
    marginTop: `${theme.spacing(2)} !important`,
    "@media(max-width: 599px)": {
      display: "none !important",
    },
  },
  list: {
    overflowY: "scroll",
    "@media(max-width: 600px)": {
      width: "100%",
      overflowY: "unset",
    },
  },
  footer: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(2),
  },
  empty: {
    textAlign: "center",
    padding: theme.spacing(4),
  },
  main: {
    overflowY: "scroll",
    minHeight: 200,
    "@media(max-width: 600px)": {
      overflowY: "unset",
    },
  },
  textSelect: {
    fontWeight: "500 !important",
    flex: 1,
  },
}));
