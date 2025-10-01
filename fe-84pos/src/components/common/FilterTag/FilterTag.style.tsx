import { Theme, styled, alpha } from "@mui/material/styles";
import { makeStyles } from "@mui/styles";
import Menu, { MenuProps } from "@mui/material/Menu";

export const useStyles = makeStyles((theme: Theme) => ({
  root: {
    borderBottom: "1px solid #D1D1D1",
    padding: 16,
    "@media(max-width: 600px)": {
      flexDirection: "column",
      alignItems: "unset !important",
    },
  },
  btnTag: {
    textTransform: "none !important" as any,
    borderColor: "#D1D1D1 !important",
    height: 36,
    fontSize: "12px !important",
    fontWeight: "500 !important",
    color: `${theme.palette.common.black} !important`,
    minWidth: "30% !important",
  },
  checkBox: {
    height: 20,
    width: 20,
  },
  label: {
    height: "20px !important",
    color: `${theme.palette.primary.contrastText} !important`,
    fontSize: "10px !important",
    fontWeight: 500,
  },
  icon: {
    color: theme.palette.text.disabled,
  },
  active: {
    color: theme.palette.primary.main,
  },
  activeBorder: {
    border: `1px solid ${theme.palette.primary.main} !important`,
  },
}));

export const StyledMenu = styled((props: MenuProps) => (
  <Menu
    elevation={0}
    anchorOrigin={{
      vertical: "bottom",
      horizontal: "right",
    }}
    transformOrigin={{
      vertical: "top",
      horizontal: "right",
    }}
    {...props}
  />
))(({ theme }) => ({
  "& .MuiPaper-root": {
    borderRadius: 6,
    marginTop: theme.spacing(1),
    color: "rgb(55, 65, 81)",
    boxShadow:
      "rgb(255, 255, 255) 0px 0px 0px 0px, rgba(0, 0, 0, 0.05) 0px 0px 0px 1px, rgba(0, 0, 0, 0.1) 0px 10px 15px -3px, rgba(0, 0, 0, 0.05) 0px 4px 6px -2px",
    "& .MuiMenu-list": {
      padding: "4px 0",
    },
    "& .MuiMenuItem-root": {
      "& .MuiSvgIcon-root": {
        fontSize: 18,
        color: theme.palette.text.secondary,
        marginRight: theme.spacing(1.5),
      },
      "&:active": {
        backgroundColor: alpha(
          theme.palette.primary.main,
          theme.palette.action.selectedOpacity
        ),
      },
    },
    ...theme.applyStyles("dark", {
      color: theme.palette.grey[300],
    }),
  },
}));
