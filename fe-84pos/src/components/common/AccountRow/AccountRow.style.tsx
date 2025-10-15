import { Theme } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";
import { tableCellClasses } from "@mui/material";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    "&:last-child td, &:last-child th": {
      border: 0,
    },
  },
  tableCell: {
    [`&.${tableCellClasses.head}`]: {
      backgroundColor: theme.palette.common.white,
    },
    [`&.${tableCellClasses.body}`]: {
      fontSize: 14,
    },
  },
  avatar: {
    marginRight: theme.spacing(1),
  },
  deleteAcc: {
    color: theme.palette.error.main,
    padding: theme.spacing(2),
    cursor: "pointer",
  },
  cellCustom: {
    "@media(max-width: 580px)": {
      display: "none !important",
    },
  },
  addPage: {
    padding: theme.spacing(2),
    cursor: "pointer",
  },
}));
