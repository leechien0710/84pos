import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    padding: `${theme.spacing(2)} !important`,
    borderRadius: "12px !important",
    position: "relative",
  },
  closeIcon: {
    position: "absolute",
    right: theme.spacing(2),
    top: theme.spacing(2),
    cursor: "pointer",
  },
  title: {
    marginTop: `${theme.spacing(3)} !important`,
  },
  content: {
    marginTop: theme.spacing(2),
  },
  input: {
    height: 36,
    flex: 3,
  },
  btnSave: {
    height: 36,
    textTransform: "initial !important" as any,
    flex: 1,
  },
}));
