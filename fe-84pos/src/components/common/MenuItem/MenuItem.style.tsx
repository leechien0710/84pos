import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  active: {
    background: "#EBF0FF",
    color: theme.palette.primary.main,
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
}));
