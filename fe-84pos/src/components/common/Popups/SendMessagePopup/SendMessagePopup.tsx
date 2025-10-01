import { HTMLAttributes, FC } from "react";
import { Grid2 as Grid, Typography, Dialog, IconButton } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { Transition } from "../../PopupTransition";
import { ChatSection } from "../../ChatSection";
import { useStyles } from "./SendMessagePopup.style";

interface ISendMessagePopupProps {
  isOpen: boolean;
  onClose: () => void;
}

export const SendMessagePopup: FC<
  HTMLAttributes<HTMLDivElement> & ISendMessagePopupProps
> = (props) => {
  const { isOpen, className, onClose, ...otherProps } = props;
  const classes = useStyles();

  return (
    <Dialog
      {...otherProps}
      open={isOpen}
      onClose={onClose}
      TransitionComponent={Transition}
      PaperProps={{
        className: `${classes.root} ${className}`,
      }}
    >
      <Grid
        display="flex"
        justifyContent="space-between"
        gap={5}
        className={classes.header}
      >
        <Typography variant="body1" className={classes.title}>
          Gửi tin nhắn đến Công chúa bong bóng
        </Typography>
        <IconButton
          edge="start"
          color="inherit"
          onClick={onClose}
          className={classes.closeIcon}
        >
          <CloseIcon />
        </IconButton>
      </Grid>
      <ChatSection hasHeader />
    </Dialog>
  );
};
