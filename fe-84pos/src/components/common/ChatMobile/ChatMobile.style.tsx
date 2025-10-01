import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {},
  backTxt: {
    textTransform: "initial !important" as any,
  },
  content: {
    background: theme.palette.background.default,
  },
  backSection: {
    marginBottom: theme.spacing(2),
  },
  backMobile: {
    margin: theme.spacing(2),
  },
}));
