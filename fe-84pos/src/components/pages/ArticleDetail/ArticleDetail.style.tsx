import { Theme, styled } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";
import InputBase from "@mui/material/InputBase";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    height: "100vh",
    display: "flex",
    flexDirection: "column",
  },
  header: {
    background: theme.palette.background.paper,
    padding: theme.spacing(2),
  },
  title: {
    textTransform: "none !important" as any,
  },
  livePostCard: {
    padding: `${theme.spacing(2)} 0px`,
  },
  container: {
    marginTop: theme.spacing(3),
    height: "100%",
  },
  btn: {
    textTransform: "none !important" as any,
    backgroundColor: `${theme.palette.background.default} !important`,
    borderColor: "#D1D1D1 !important",
    borderRadius: "8px !important",
  },
  btnActive: {
    backgroundColor: `${theme.palette.primary.main} !important`,
  },
  textFilter: {
    fontWeight: "500 !important",
    "-webkit-line-clamp": 1,
    "-webkit-box-orient": "vertical",
    overflow: "hidden",
    display: "-webkit-box",
    wordBreak: "break-all",
  },
  content: {
    background: theme.palette.background.paper,
    marginTop: theme.spacing(2),
    padding: theme.spacing(2),
    flex: 1,
    overflowY: "auto",
    paddingBottom: 320,
  },
  mainFilter: {
    "@media(max-width: 600px)": {
      padding: `0px ${theme.spacing(2)}`,
    },
  },
  contentLoading: {
    height: "100%",
  },
}));

export const Search = styled("div")(({ theme }) => ({
  position: "relative",
  borderRadius: theme.shape.borderRadius,
  backgroundColor: theme.palette.grey[100],
  marginLeft: 0,
  maxWidth: 256,
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
