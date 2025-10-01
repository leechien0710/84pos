import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  username: {
    fontWeight: "500 !important",
  },
  label: {
    height: "20px !important",
    color: `${theme.palette.primary.contrastText} !important`,
    fontSize: "10px !important",
    fontWeight: 500,
    marginRight: theme.spacing(1),
  },
  info: {
    marginTop: theme.spacing(2),
  },
  call: {
    fontSize: "20px !important",
    color: "#07B32D",
  },
  title: {
    color: "#303030",
  },
  link: {
    color: theme.palette.primary.main,
  },
  inputField: {
    marginTop: `${theme.spacing(1)} !important`,
    "& > div.MuiOutlinedInput-root": {
      height: 32,
      borderRadius: 8,
      "&.MuiInputBase-input": {
        height: 32,
        fontSize: "16px !important",
      },
    },
  },
  address: {
    marginTop: theme.spacing(2),
  },
  content: {
    borderTop: "1px solid #D1D1D1",
    marginTop: theme.spacing(2),
  },
  allCmt: {
    fontWeight: "500 !important",
    margin: `${theme.spacing(1)} 0px !important`,
  },
  messages: {
    height: "calc(100vh - 577px)",
    overflowY: "scroll",
  },
  createdAt: {
    color: theme.palette.text.disabled,
  },
  empty: {},
}));
