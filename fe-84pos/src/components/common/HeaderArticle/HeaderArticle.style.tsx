import { Theme, styled } from "@mui/material/styles";
import InputBase from "@mui/material/InputBase";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    padding: theme.spacing(2),
    "@media(max-width: 1044px)": {
      flexDirection: "column",
    },
  },
  btn: {
    height: 36,
    textTransform: "unset !important" as any,
  },
  mainReload: {
    "@media(max-width: 1044px)": {
      justifyContent: "space-between",
      width: "100%",
    },
  },
  mainFilter: {
    "@media(max-width: 1044px)": {
      width: "100%",
      justifyContent: "end",
      marginTop: theme.spacing(2),
    },
    "@media(max-width: 600px)": {
      justifyContent: "start",
    },
  },
  reloadText: {
    fontWeight: "500 !important",
  },
  textLong: {
    "-webkit-line-clamp": 1,
    "-webkit-box-orient": "vertical",
    overflow: "hidden",
    display: "-webkit-box",
    wordBreak: "break-all",
  },
}));

export const Search = styled("div")(({ theme }) => ({
  position: "relative",
  borderRadius: theme.shape.borderRadius,
  backgroundColor: theme.palette.grey[100],
  marginLeft: 0,
  [theme.breakpoints.up("sm")]: {
    width: "auto",
  },
  border: "1px solid #D1D1D1",
  height: 36,
}));

export const SearchIconWrapper = styled("div")(({ theme }) => ({
  padding: theme.spacing(0, 1),
  height: "100%",
  position: "absolute",
  pointerEvents: "none",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
}));

export const StyledInputBase = styled(InputBase)(({ theme }) => ({
  color: "inherit",
  height: 36,
  fontSize: 14,
  "& .MuiInputBase-input": {
    padding: theme.spacing(1, 1, 1, 0),
    paddingLeft: `calc(1em + ${theme.spacing(3)})`,
  },
}));
