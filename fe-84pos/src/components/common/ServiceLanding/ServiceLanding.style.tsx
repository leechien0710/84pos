import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    padding: `${theme.spacing(2)} ${theme.spacing(14)}`,
    backgroundColor: "#F3F3F3",
    textAlign: "center",
  },
  title: {
    fontSize: "56px !important",
    fontWeight: "700 !important",
    color: theme.palette.primary.main,
  },
  desc: {
    fontSize: "18px !important",
    color: "#0c0c0ca6",
    marginTop: `${theme.spacing(3)} !important`,
    marginBottom: `${theme.spacing(5)} !important`,
  },
  btnStart: {
    textTransform: "initial !important" as any,
    fontSize: "16px !important",
    marginBottom: `${theme.spacing(5)} !important`,
  },
}));
