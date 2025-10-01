import { HTMLAttributes, FC } from "react";
import {
  Grid2 as Grid,
  Typography,
  Button,
  TextField,
  Popover,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { useStyles } from "./AddPhoneTooltip.style";
import { ActionLive } from "../../../types/live";

interface IAddPhoneTooltipProps {
  open: boolean;
  anchorEl: HTMLElement | null;
  type: ActionLive;
  handleClose: () => void;
}

export const AddPhoneTooltip: FC<
  HTMLAttributes<HTMLDivElement> & IAddPhoneTooltipProps
> = (props) => {
  const { className, open, type, anchorEl, handleClose, ...otherProps } = props;
  const classes = useStyles();

  const id = open ? "simple-popover" : undefined;

  return (
    <Popover
      {...otherProps}
      id={id}
      open={open}
      anchorEl={anchorEl}
      onClose={handleClose}
      anchorOrigin={{
        vertical: "bottom",
        horizontal: "left",
      }}
      transformOrigin={{
        vertical: "top",
        horizontal: "left",
      }}
      PaperProps={{ className: `${classes.root} ${className}` }}
    >
      <CloseIcon onClick={handleClose} className={classes.closeIcon} />
      <Typography variant="subtitle2" className={classes.title}>
        {type === "phone"
          ? "Số điện thoại / Công chúa bong bóng"
          : "Phản hồi / Công chúa bong bóng"}
      </Typography>
      <Grid
        display="flex"
        alignItems="center"
        gap={2}
        className={classes.content}
      >
        <TextField
          fullWidth
          placeholder={type === "phone" ? "Nhập SĐT" : "Nhập bình luận..."}
          className={classes.input}
          sx={{
            height: 36,
            "& .MuiOutlinedInput-root": { height: 36 },
            "& .MuiInputBase-input": { height: 36 },
          }}
        />
        <Button variant="contained" color="primary" className={classes.btnSave}>
          {type === "phone" ? "Lưu lại" : "Bình luận"}
        </Button>
      </Grid>
    </Popover>
  );
};
