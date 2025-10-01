import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  content: {
    backgroundColor: "#f8f8f8",
    padding: theme.spacing(2),
    overflowY: "scroll",
    borderBottom: "1px solid #D1D1D1",
    height: "calc(100% - 168px)",
  },
  contentNoHeader: {
    height: "calc(100% - 111px)",
  },
  contentMobile: {
    height: "calc(100% - 85px)",
  },
  messCard: {
    marginBottom: theme.spacing(1),
  },
  footer: {
    backgroundColor: theme.palette.background.paper,
  },
  inputSection: {
    position: "relative",
    display: "flex",
    alignItems: "center",
    width: "100%",
  },
  btnSend: {
    position: "absolute !important" as any,
    right: theme.spacing(2),
    height: 32,
    width: 32,
    minWidth: "32px !important",
    borderRadius: "8px !important",
  },
  sendIcon: {
    fontSize: "13px !important",
  },
  label: {
    height: 20,
    width: 22,
  },
  actions: {
    paddingRight: theme.spacing(2),
    paddingBottom: theme.spacing(1),
  },
  date: {
    fontWeight: "500 !important",
    fontSize: "12px !important",
    color: theme.palette.text.disabled,
    textAlign: "center",
    margin: `${theme.spacing(2)} 0px !important`,
  },
}));
