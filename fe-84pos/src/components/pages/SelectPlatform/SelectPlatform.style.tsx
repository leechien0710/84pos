import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    height: "100vh",
    backgroundColor: "#F2F2F2",
  },
  header: {
    flex: "0 0 auto",
  },
  form: {
    flex: "1 0 auto",
  },
  signInForm: {
    margin: `0px ${theme.spacing(2)}`,
  },
}));
