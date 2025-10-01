import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    background: theme.palette.background.default,
    height: "100vh",
    display: "flex",
    flexDirection: "column",
  },
  content: {
    overflow: "scroll",
    marginBottom: 80,
  },
  contentLoading: {
    height: "100%",
  },
  livePostCard: {
    cursor: "pointer",
    borderBottom: "1px solid #ECECEC",
    "@media(max-width: 600px)": {
      paddingLeft: 0,
      paddingRight: 0,
    },
  },
}));
