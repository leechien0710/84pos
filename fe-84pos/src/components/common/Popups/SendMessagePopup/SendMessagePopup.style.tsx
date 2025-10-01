import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    borderRadius: "16px !important",
    position: "relative !important" as any,
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
}));
