import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    backgroundColor: theme.palette.background.default,
    borderRadius: 20,
    padding: theme.spacing(4),
    maxWidth: 380,
    width: 380,
    boxSizing: "border-box",
  },
  title: {
    textAlign: "center",
  },
  actions: {
    marginTop: theme.spacing(2),
  },
  btn: {
    height: 52,
    marginTop: `${theme.spacing(2)} !important`,
    "&.MuiButtonBase-root": {
      textTransform: "none",
      fontWeight: 500,
      justifyContent: "flex-start",
      textAlign: "left",
    },
  },
}));
