import { FC, HTMLAttributes } from "react";
import { Grid2 as Grid, Typography } from "@mui/material";
import BackIcon from "@mui/icons-material/ArrowBack";
import { useStyles } from "./ChatMobile.style";
import { useDeviceType } from "../../../hooks/screen";

interface IChatMobileProps {
  renderContent: () => React.ReactNode;
  onBack: () => void;
}

export const ChatMobile: FC<
  HTMLAttributes<HTMLDivElement> & IChatMobileProps
> = (props) => {
  const { renderContent, onBack } = props;
  const classes = useStyles();
  const isMobile = useDeviceType();

  return (
    <div {...props}>
      <Grid
        display="flex"
        alignItems="center"
        gap={1}
        className={`${classes.backSection} ${isMobile && classes.backMobile}`}
        onClick={onBack}
      >
        <BackIcon />
        <Typography variant="button" className={classes.backTxt}>
          Trở về
        </Typography>
      </Grid>
      <div className={classes.content}>{renderContent()}</div>
    </div>
  );
};
