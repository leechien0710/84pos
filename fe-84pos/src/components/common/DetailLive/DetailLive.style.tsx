import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    background: theme.palette.background.paper,
    padding: theme.spacing(2),
    paddingBottom: 0,
    borderRadius: 4,
  },
  btnTab: {
    fontSize: "14px !important",
    fontWeight: "500 !important",
    textTransform: "unset !important" as any,
    borderRadius: "20px !important",
    height: 32,
    boxShadow: "none !important",
    whiteSpace: "nowrap",
  },
  inActive: {
    background: "#F8F8F8 !important",
    color: `${theme.palette.common.black} !important`,
  },
  tab: {
    marginTop: theme.spacing(2),
  },
  contentOrder: {
    height: "calc(100vh - 225px) !important",
  },
}));
