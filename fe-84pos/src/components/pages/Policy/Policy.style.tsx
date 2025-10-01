import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  content: {
    padding: `${theme.spacing(2)} ${theme.spacing(14)}`,
    backgroundColor: "#F3F3F3",
  },
  wrapper: {
    backgroundColor: theme.palette.background.default,
    borderRadius: theme.spacing(2),
    padding: 40,
  },
  policyTitle: {
    fontSize: "18px !important",
    fontWeight: "600 !important",
    color: theme.palette.primary.main,
    marginBottom: "25px !important",
  },
  title: {
    fontSize: "16px !important",
    fontWeight: "bold !important",
    marginBottom: "10px !important",
  },
  desc: {
    marginBottom: "10px !important",
  },
  item: {
    marginLeft: 20,
  },
}));
