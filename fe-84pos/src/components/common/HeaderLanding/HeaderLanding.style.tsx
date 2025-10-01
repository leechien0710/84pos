import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    padding: `${theme.spacing(2)} ${theme.spacing(14)}`,
  },
  menu: {
    fontWeight: "500 !important",
    cursor: "pointer",
    "&:hover": {
      color: theme.palette.primary.main,
    },
  },
  btnStart: {
    textTransform: "initial !important" as any,
    fontSize: "16px !important",
  },
}));
