import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    backgroundColor: theme.palette.background.default,
    color: theme.palette.common.white,
    padding: theme.spacing(2),
    width: "100%",
  },
  btnContact: {
    "&.MuiButtonBase-root": {
      textTransform: "none",
      fontWeight: 500,
      "@media(max-width: 386px)": {
        display: "none",
      },
    },
  },
  btnPhone: {
    "&.MuiButtonBase-root": {
      display: "none",
      "@media(max-width: 386px)": {
        display: "inline-flex",
        margin: `${theme.spacing(2)} 0px`,
      },
    },
  },
  logo: {
    cursor: "pointer",
  },
}));
