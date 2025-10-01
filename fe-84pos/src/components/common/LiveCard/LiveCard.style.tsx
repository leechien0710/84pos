import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    borderBottom: "1px solid #D1D1D1",
    padding: 16,
  },
  hideStatus: {
    background: "#FFE8E8",
  },
  amount: {
    color: "#07B32D",
  },
  link: {
    textDecoration: "underline",
    color: theme.palette.primary.main,
  },
  message: {
    margin: "8px 0px !important",
  },
  btnPrint: {
    fontSize: "12px !important",
    fontWeight: "500 !important",
    borderRadius: "8px !important",
    textTransform: "unset !important" as any,
    height: 30,
    whiteSpace: "nowrap",
  },
  iconPrint: {
    fontSize: "16px !important",
  },
  actionIcon: {
    fontSize: "20px !important",
  },
  call: {
    color: "#07B32D",
  },
  created: {
    color: theme.palette.text.disabled,
    "@media(max-width: 600px)": {
      display: "none",
    },
  },
}));
