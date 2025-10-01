import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    padding: theme.spacing(2),
    cursor: "pointer",
  },
  call: {
    color: "#07B32D",
    fontSize: "16px !important",
    margin: "0px 5px",
  },
  missMess: {
    fontWeight: "500 !important",
    backgroundColor: theme.palette.error.main,
    color: theme.palette.common.white,
    borderRadius: 8,
    height: 20,
    width: 20,
    textAlign: "center",
  },
  replyMessage: {
    margin: "5px 0px",
  },
  reply: {
    transform: "rotateX(180deg) rotateY(180deg) rotateZ(90deg)",
    fontSize: "16px !important",
  },
  subMess: {
    fontWeight: "500 !important",
    maxWidth: 200,
  },
  color: {
    height: 12,
    width: 16,
  },
  date: {
    color: "#AFAFAF",
  },
  message: {
    fontSize: "20px !important",
    color: "#AFAFAF",
    marginTop: 5,
  },
  textLong: {
    overflow: "hidden",
    display: "-webkit-box",
    "-webkit-line-clamp": 1,
    "-webkit-box-orient": "vertical",
    wordBreak: "break-all",
  },
}));
