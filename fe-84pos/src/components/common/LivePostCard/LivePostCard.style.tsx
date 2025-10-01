import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    color: theme.palette.text.disabled,
    padding: theme.spacing(2),
  },
  gameImage: {
    height: 112,
    width: 80,
    background: "#E2E2E2",
    borderRadius: 4,
  },
  first: {
    position: "relative",
  },
  contentCamera: {
    background: theme.palette.text.disabled,
    width: 32,
    height: 17,
  },
  post: {
    background: theme.palette.text.disabled,
    width: 40,
    height: 28,
  },
  iconLabel: {
    borderRadius: 5,
    position: "absolute",
    top: 5,
    right: 5,
    padding: 8,
  },
  title: {
    fontWeight: "500 !important",
    color: "initial",
  },
  time: {
    fontWeight: "500 !important",
    color: theme.palette.text.disabled,
  },
  actions: {
    marginTop: 5,
  },
}));
