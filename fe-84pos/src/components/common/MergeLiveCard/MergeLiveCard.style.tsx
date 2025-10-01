import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  platformImg: {
    height: 16,
    width: 16,
  },
  contentCamera: {
    background: theme.palette.error.light,
    padding: 8,
    borderRadius: 5,
    width: 32,
    height: 17,
  },
  live: {
    padding: `${theme.spacing(2)} 0px`,
  },
  pageName: {
    "-webkit-line-clamp": 1,
    "-webkit-box-orient": "vertical",
    overflow: "hidden",
    display: "-webkit-box",
    wordBreak: "break-all",
  },
}));
