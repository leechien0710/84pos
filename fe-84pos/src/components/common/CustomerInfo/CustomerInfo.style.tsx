import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  title: {
    fontWeight: "500 !important",
  },
  orderCard: {
    backgroundColor: theme.palette.background.default,
    marginTop: theme.spacing(2),
    padding: theme.spacing(2),
    paddingBottom: 0,
    borderRadius: 4,
  },
  contentOrder: {
    height: "calc(100vh - 266px) !important",
  },
  contentOrderExpand: {
    height: "calc(100vh - 504px) !important",
  },
  contentOrderSmall: {
    height: "calc(100vh - 189px) !important",
  },
  contentOrderSmallExpand: {
    height: "calc(100vh - 429px) !important",
  },
}));
