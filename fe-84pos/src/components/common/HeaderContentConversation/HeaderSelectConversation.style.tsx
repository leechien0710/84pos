import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {},
  head: {
    padding: theme.spacing(2),
    borderBottom: "1px solid #D1D1D1",
  },
  name: {
    fontWeight: "500 !important",
    overflow: "hidden",
    display: "-webkit-box",
    "-webkit-line-clamp": 1,
    "-webkit-box-orient": "vertical",
  },
  call: {
    color: "#07B32D",
    fontSize: "16px !important",
    margin: "0px 5px",
  },
  btnAction: {
    borderColor: "#D1D1D1 !important",
    minWidth: "28px !important",
    width: 28,
    height: 28,
    padding: "0px !important",
  },
  icon: {
    fontSize: "16px !important",
  },
  username: {
    width: "100%",
  },
  postInfo: {
    padding: theme.spacing(2),
  },
  descSection: {
    position: "relative",
  },
  img: {
    height: 100,
    width: 100,
    backgroundColor: "#E2E2E2",
    borderRadius: 4,
  },
  iconSection: {
    backgroundColor: "#00000066",
    height: 28,
    width: 40,
    borderRadius: 4,
    position: "absolute",
    top: 5,
    right: 5,
  },
  descIcon: {
    fontSize: "20px !important",
    color: theme.palette.common.white,
  },
  linkSection: {
    marginTop: theme.spacing(1),
    color: theme.palette.primary.main,
  },
  lauchIcon: {
    fontSize: "20px !important",
  },
  linkConnect: {
    fontWeight: "500 !important",
  },
  title: {
    overflow: "hidden",
    display: "-webkit-box",
    "-webkit-line-clamp": 3,
    "-webkit-box-orient": "vertical",
  },
}));
