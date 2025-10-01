import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    borderRadius: "16px !important",
    position: "relative !important" as any,
    minWidth: 343,
  },
  header: {
    padding: theme.spacing(2),
    borderBottom: "1px solid #D1D1D1",
    backgroundColor: theme.palette.background.paper,
    borderTopLeftRadius: "16px !important",
    borderTopRightRadius: "16px !important",
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
    height: "calc(100% - 168px)",
  },
  contentLoading: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    padding: theme.spacing(2),
  },
  card: {
    borderBottom: "1px solid #D1D1D1",
    padding: theme.spacing(2),
  },
  footer: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(2),
  },
  btnAction: {
    height: 32,
    width: 32,
    borderRadius: "8px !important",
    textTransform: "unset !important" as any,
  },
}));
