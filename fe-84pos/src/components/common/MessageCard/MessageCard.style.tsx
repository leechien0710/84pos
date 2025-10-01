import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    width: "80%",
    "@media(max-width: 600px)": {
      width: "92%",
    },
  },
  me: {
    // float: "right",
    marginLeft: "auto",
  },
  avatar: {
    height: "32px !important",
    width: "32px !important",
    "@media(max-width: 600px)": {
      height: "24px !important",
      width: "24px !important",
    },
  },
  mess: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(2),
    borderRadius: 12,
    borderWidth: 1,
    borderStyle: "solid",
    borderColor: "#D1D1D1",
    wordBreak: "break-word",
  },
  messMe: {
    backgroundColor: theme.palette.primary.main,
    borderColor: "transparent",
    color: theme.palette.common.white,
  },
  imgAttach: {
    width: "inherit",
    maxWidth: 250,
  },
}));
