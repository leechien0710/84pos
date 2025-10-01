import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {},
  content: {
    padding: theme.spacing(2),
  },
  label: {
    fontWeight: "500 !important",
  },
  avatar: {
    height: "32px !important",
    width: "32px !important",
  },
  platform: {
    height: 16,
    width: 16,
  },
  title: {
    marginLeft: `${theme.spacing(1)} !important`,
  },
  row: {
    marginTop: theme.spacing(1),
  },
  btnSelect: {
    textTransform: "unset !important" as any,
    backgroundColor: "#f5f5f5 !important",
    boxShadow: "none !important",
    border: "1px solid #D1D1D1 !important",
  },
  textLong: {
    overflow: "hidden",
    display: "-webkit-box",
    "-webkit-line-clamp": 1,
    "-webkit-box-orient": "vertical",
  },
}));
