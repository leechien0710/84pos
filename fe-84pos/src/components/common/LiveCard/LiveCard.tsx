import { HTMLAttributes, FC, useState } from "react";
import { Grid2 as Grid, Typography, Button, Avatar } from "@mui/material";
import Print from "@mui/icons-material/Print";
import AddCall from "@mui/icons-material/AddIcCall";
import AccountBox from "@mui/icons-material/AccountBox";
import Message from "@mui/icons-material/QuestionAnswer";
import Call from "@mui/icons-material/Call";
import CloudSync from "@mui/icons-material/CloudSync";
import EyeOff from "@mui/icons-material/VisibilityOff";
import { StaticImage } from "../StaticImage";
import Cart from "../../../assets/livestream/cart.webp";
import Cart2x from "../../../assets/livestream/cart@2x.webp";
import Cart3x from "../../../assets/livestream/cart@3x.webp";
import { useDeviceType } from "../../../hooks/screen";
import { AddPhoneTooltip } from "../AddPhoneTooltip";
import { ActionLive } from "../../../types/live";
import { SendMessagePopup } from "../Popups/SendMessagePopup";
import { useStyles } from "./LiveCard.style";

interface ILiveCardProps {
  avatar: string;
  username: string;
  createdAt: string;
  message: string;
  isHideStatus?: boolean;
  onSetMessageId: (id: string) => void;
}

export const LiveCard: FC<HTMLAttributes<HTMLDivElement> & ILiveCardProps> = (
  props
) => {
  const {
    avatar,
    username,
    message,
    createdAt,
    isHideStatus,
    className,
    onSetMessageId,
    ...otherProps
  } = props;
  const classes = useStyles();
  const isMobile = useDeviceType();
  const [anchorEl, setAnchorEl] = useState(null);
  const [type, setType] = useState<ActionLive>("phone");
  const [openSendMess, setOpenSendMess] = useState(false);

  const handleClick = (event: any, action: ActionLive) => {
    setAnchorEl(event.currentTarget);
    setType(action);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const onToggleSendMess = () => {
    setOpenSendMess(!openSendMess);
  };

  const renderActions = () => (
    <Grid display="flex" alignItems="center" gap={1}>
      <Button
        variant="contained"
        startIcon={<Print className={classes.iconPrint} />}
        className={classes.btnPrint}
      >
        In đơn
      </Button>
      <AddCall
        color="primary"
        className={classes.actionIcon}
        onClick={(e) => handleClick(e, "phone")}
      />
      <AccountBox
        color="disabled"
        className={classes.actionIcon}
        onClick={() => onSetMessageId(message)}
      />
      <Message
        color="disabled"
        className={classes.actionIcon}
        onClick={onToggleSendMess}
      />
      <CloudSync color="disabled" className={classes.actionIcon} />
      <EyeOff
        color={isHideStatus ? "error" : "disabled"}
        className={classes.actionIcon}
      />
      {!isMobile && (
        <Call className={`${classes.actionIcon} ${classes.call}`} />
      )}
      <Button
        variant="outlined"
        color="inherit"
        className={classes.btnPrint}
        onClick={(e) => handleClick(e, "feedback")}
      >
        Phản hồi
      </Button>
    </Grid>
  );

  return (
    <Grid
      {...otherProps}
      display={!isMobile ? "flex" : "block"}
      justifyContent="space-between"
      className={`${className} ${classes.root} ${
        isHideStatus && classes.hideStatus
      }`}
    >
      <Grid display="flex" gap={2}>
        <Avatar src={avatar} />
        <div>
          <Grid display="flex" alignItems="center" gap={1}>
            <Typography variant="subtitle2">{username}</Typography>
            <StaticImage
              src={Cart}
              src2x={Cart2x}
              src3x={Cart3x}
              height={16}
              width={14}
            />
            <Typography variant="subtitle2" className={classes.amount}>
              (1)
            </Typography>
            <Typography variant="subtitle2" className={classes.link}>
              #7012
            </Typography>
          </Grid>
          <Typography variant="body2" className={classes.message}>
            {message}
          </Typography>
          {!isMobile && renderActions()}
        </div>
      </Grid>
      <Typography variant="body2" className={classes.created}>
        {createdAt}
      </Typography>
      {isMobile && renderActions()}
      <AddPhoneTooltip
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        type={type}
        handleClose={handleClose}
      />
      <SendMessagePopup isOpen={openSendMess} onClose={onToggleSendMess} />
    </Grid>
  );
};
