import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";
import { tableCellClasses } from "@mui/material";

export const useStyles = makeStyles((theme: Theme) => ({
  tableCell: {
    [`&.${tableCellClasses.head}`]: {
      backgroundColor: theme.palette.common.white,
    },
    [`&.${tableCellClasses.body}`]: {
      fontSize: 14,
    },
  },
  head: {
    "& > th": {
      backgroundColor: `${theme.palette.action.hover} !important`,
    },
  },
  btnAdd: {
    "&.MuiButtonBase-root": {
      textTransform: "none",
      fontWeight: 500,
    },
  },
  platformImg: {
    marginRight: theme.spacing(1),
  },
  cellCustom: {
    "@media(max-width: 580px)": {
      display: "none !important",
    },
  },
}));
