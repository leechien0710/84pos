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
    fontWeight: "500 !important",
    textAlign: "center",
    marginBottom: `${theme.spacing(4)} !important`,
  },
  label: {
    fontWeight: "500 !important",
    marginBottom: `${theme.spacing(1)} !important`,
  },
  input: {
    "&.MuiOutlinedInput-root": {
      height: 48,
      borderRadius: 12,
      "&.MuiInputBase-input": {
        height: 48,
        fontSize: "16px !important",
      },
    },
  },
  field: {
    marginTop: theme.spacing(2),
  },
  checkBox: {
    paddingLeft: "0px !important",
  },
  labelBottom: {
    fontWeight: "500 !important",
  },
  action: {
    marginTop: theme.spacing(2),
  },
  btn: {
    height: 52,
    "&.MuiButtonBase-root": {
      textTransform: "none",
      fontWeight: 500,
    },
  },
  btnRegister: {
    marginTop: `${theme.spacing(2)} !important`,
  },
}));
