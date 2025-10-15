import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";
import { tableCellClasses } from "@mui/material";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    padding: `${theme.spacing(2)} ${theme.spacing(4)}`,
  },
  avatarPlatform: {
    height: "24px !important",
    width: "24px !important",
    "& > img": {
      border: `1.5px solid ${theme.palette.background.default}`,
      boxSizing: "border-box",
      borderRadius: "100%",
    },
  },
  name: {
    marginLeft: `${theme.spacing(2)} !important`,
    width: "max-content",
    fontSize: "14px !important",
  },
  chip: {
    marginLeft: `${theme.spacing(1)} !important`,
    backgroundColor: "#8A8A8A !important",
    color: `${theme.palette.primary.contrastText} !important`,
  },
  chipActive: {
    marginLeft: `${theme.spacing(2)} !important`,
  },
  chipInactive: {
    marginLeft: `${theme.spacing(2)} !important`,
  },
  borderImage: {
    "@media(max-width: 480px)": {
      display: "none",
    },
  },
  tableCell: {
    [`&.${tableCellClasses.head}`]: {
      backgroundColor: theme.palette.common.white,
    },
    [`&.${tableCellClasses.body}`]: {
      fontSize: 14,
    },
    borderBottom: "none !important",
  },
}));
