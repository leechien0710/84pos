import { Theme, styled } from "@mui/material/styles";
import InputBase from "@mui/material/InputBase";
import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles((theme: Theme) => ({
  title: {
    fontWeight: "500 !important",
    marginTop: `${theme.spacing(2)} !important`,
  },
  content: {
    marginTop: theme.spacing(1),
    overflowY: "scroll",
    height: "calc(100vh - 400px)",
  },
  link: {
    color: theme.palette.primary.main,
  },
  created: {
    color: theme.palette.text.disabled,
  },
  actionIcon: {
    color: "#07B32D",
    fontSize: "20px !important",
  },
  amount: {
    color: "#07B32D",
  },
  orderItem: {
    border: "1px solid #D1D1D1",
    marginBottom: theme.spacing(2),
    boxShadow: "none",
    borderRadius: `${theme.spacing(1)} !important`,
    "&:before": {
      display: "none",
    },
  },
  label: {
    fontWeight: "500 !important",
  },
  btnTime: {
    borderColor: "#D1D1D1 !important",
    textTransform: "none !important" as any,
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
  marginTop: theme.spacing(1),
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
