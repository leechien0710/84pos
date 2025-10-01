import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    height: "100vh",
    backgroundColor: "#F2F2F2",
    overflow: "overlay",
  },
  header: {
    width: "100%",
    position: "fixed",
    zIndex: 1,
  },
  form: {
    flex: "1 0 auto",
    paddingTop: 72,
    paddingBottom: theme.spacing(2),
    "@media(max-width: 386px)": {
      paddingTop: 120,
    },
  },
  title: {
    fontSize: "24px !important",
    fontWeight: "500 !important",
    margin: `${theme.spacing(2)} 0px ${theme.spacing(2)} 0px !important`,
  },
  fbTable: {
    margin: `${theme.spacing(2)} 0px 78px 0px !important`,
  },
  bottom: {
    position: "fixed",
    zIndex: 1,
    bottom: 0,
  },
}));
