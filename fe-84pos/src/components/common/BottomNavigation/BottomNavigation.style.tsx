import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    backgroundColor: theme.palette.background.default,
    color: theme.palette.common.white,
    padding: theme.spacing(2),
    width: "100%",
    height: 78,
    boxSizing: "border-box",
  },
  btnAccess: {
    "&.MuiButtonBase-root": {
      backgroundColor: "#10AE59 !important",
      textTransform: "none !important",
      color: "#fff !important",
    },
  },
}));
