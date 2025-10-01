import { HTMLAttributes, FC } from "react";
import { map } from "lodash-es";
import { useNavigate, useLocation } from "react-router-dom";
import Button from "@mui/material/Button";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import { StaticImage } from "../../common/StaticImage";
import { listMenu } from "../../../utils/layout";
import { PageRoutes } from "../../../constants/router";
import { useStyles } from "./MenuItem.style";

interface IMenuItemProps {
  open: boolean;
  onClose: () => void;
}

export const MenuItem: FC<HTMLAttributes<HTMLDivElement> & IMenuItemProps> = (
  props
) => {
  const { open, onClose } = props;
  const classes = useStyles();
  const navigate = useNavigate();
  const location = useLocation();
  const menus = listMenu(navigate);

  return (
    <>
      {map(menus, ({ url, icon, iconActive, onAction, title }, idx) => (
        <ListItem
          key={idx}
          disablePadding
          sx={{ display: "block" }}
          onClick={() => {
            onAction?.();
            onClose();
          }}
          className={`${url === location.pathname && classes.active}`}
        >
          <ListItemButton
            sx={[
              {
                minHeight: 48,
                px: 2.5,
                justifyContent: open ? "initial" : "center",
              },
            ]}
          >
            <ListItemIcon
              sx={[
                {
                  minWidth: 0,
                  justifyContent: "center",
                  mr: open ? 1 : "auto",
                },
              ]}
            >
              <StaticImage
                src={url === location.pathname ? iconActive.oneX : icon.oneX}
                src2x={url === location.pathname ? iconActive.twoX : icon.twoX}
                src3x={
                  url === location.pathname ? iconActive.threeX : icon.threeX
                }
              />
            </ListItemIcon>
            <ListItemText
              primary={title}
              sx={[
                {
                  opacity: open ? 1 : 0,
                  "& .MuiListItemText-primary": {
                    fontWeight: 500,
                    fontSize: 14,
                  },
                },
              ]}
            />
            {url === PageRoutes.Livestream && open && (
              <Button
                variant="contained"
                color="error"
                className={classes.btnLive}
              >
                <div className={classes.circle} />
                &nbsp;Live
              </Button>
            )}
          </ListItemButton>
        </ListItem>
      ))}
    </>
  );
};
