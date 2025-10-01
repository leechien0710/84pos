import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    background: theme.palette.background.paper,
    padding: theme.spacing(2),
    borderRadius: 4,
  },
  img: {
    background: "#CCCCCC",
    height: 120,
    width: 80,
    borderRadius: 8,
  },
  btnLive: {
    textTransform: "none !important" as any,
    borderRadius: "30px !important",
    height: 26,
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
  },
  circle: {
    height: 10,
    width: 10,
    borderRadius: "100%",
    border: `2px solid ${theme.palette.common.white}`,
  },
  content: {
    flex: 1,
  },
  select: {
    fontSize: "12px !important",
    fontWeight: "500 !important",
    textTransform: "unset !important" as any,
    height: 28,
  },
  title: {
    marginTop: `${theme.spacing(2)} !important`,
  },
}));
