import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    backgroundColor: theme.palette.background.default,
    borderRadius: 20,
    padding: theme.spacing(4),
    maxWidth: 544,
    width: 544,
    boxSizing: "border-box",
  },
  title: {
    textAlign: "center",
  },
  otpContainer: {
    margin: `${theme.spacing(4)} 0px !important`,
  },
  phone: {
    fontWeight: 700,
  },
  input: { width: "40px", height: "48px", textAlign: "center" },
  btn: {
    height: 52,
    "&.MuiButtonBase-root": { textTransform: "none", fontWeight: 500 },
  },
  btnSendAgain: {
    marginTop: `${theme.spacing(2)} !important`,
  },
}));
