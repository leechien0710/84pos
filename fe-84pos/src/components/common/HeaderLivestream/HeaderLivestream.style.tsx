import { Theme, styled } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";
import InputBase from "@mui/material/InputBase";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    borderBottom: "1px solid #D1D1D1",
    padding: 16,
    "@media(max-width: 600px)": {
      flexDirection: "column",
      alignItems: "unset !important",
    },
  },
  title: {
    fontWeight: "500 !important",
    color: theme.palette.text.disabled,
  },
  mergeLive: {
    height: 26,
    background:
      "linear-gradient(90deg, rgba(234, 45, 45, 0.3) 0%, rgba(26, 35, 238, 0.3) 100%)",
    fontSize: "12px !important",
    fontWeight: "500 !important",
    whiteSpace: "nowrap",
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
  liveInfo: {
    marginTop: 10,
  },
  actions: {
    "@media(max-width: 600px)": {
      marginTop: theme.spacing(2),
    },
  },
}));

export const Search = styled("div")(({ theme }) => ({
  position: "relative",
  borderRadius: theme.shape.borderRadius,
  backgroundColor: theme.palette.grey[100],
  marginLeft: 0,
  width: "100%",
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
