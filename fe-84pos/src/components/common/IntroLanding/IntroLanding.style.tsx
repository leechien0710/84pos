import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    padding: `${theme.spacing(8)} ${theme.spacing(14)}`,
  },
  title: {
    fontSize: "56px !important",
    fontWeight: "700 !important",
    lineHeight: "64px !important",
  },
  introMain: {
    flex: 3,
  },
  desc: {
    fontSize: "18px !important",
    paddingTop: theme.spacing(3),
    paddingBottom: theme.spacing(5),
  },
  banner: {
    flex: 2,
  },
  bannerImg: {
    width: "100%",
  },
  btnStart: {
    textTransform: "initial !important" as any,
    fontSize: "16px !important",
  },
}));
