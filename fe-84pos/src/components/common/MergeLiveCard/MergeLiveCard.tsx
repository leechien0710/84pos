import { HTMLAttributes, FC } from "react";
import {
  Grid2 as Grid,
  Typography,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Avatar,
  Badge,
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import FacebookIcon from ".././../../assets/select-account/facebook.webp";
import Facebook2x from "../../../assets/sign-in/facebook@2x.webp";
import Facebook3x from "../../../assets/sign-in/facebook@3x.webp";
import { StaticImage } from "../StaticImage";
import CameraIcon from "../../../assets/livestream/camera.webp";
import CameraIcon2x from "../../../assets/livestream/camera@2x.webp";
import CameraIcon3x from "../../../assets/livestream/camera@3x.webp";
import { useStyles } from "./MergeLiveCard.style";

export const MergeLiveCard: FC<HTMLAttributes<HTMLDivElement>> = (props) => {
  const { className, ...otherProps } = props;
  const classes = useStyles();

  return (
    <Accordion>
      <AccordionSummary
        expandIcon={<ExpandMoreIcon />}
        aria-controls="panel1-content"
        id="panel1-header"
      >
        <Grid display="flex" alignItems="center" gap={1}>
          <Avatar src={FacebookIcon} sx={{ width: 24, height: 24 }} />
          <Typography variant="subtitle2">Facebook</Typography>
        </Grid>
      </AccordionSummary>
      <AccordionDetails>
        {Array.from(new Array(15)).map((_, key) => (
          <Grid
            display="flex"
            alignItems="center"
            gap={1}
            className={classes.live}
          >
            <Badge
              overlap="circular"
              anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
              badgeContent={
                <StaticImage
                  src={Facebook2x}
                  src2x={Facebook2x}
                  src3x={Facebook3x}
                  className={classes.platformImg}
                />
              }
            >
              <Avatar
                alt="Travis Howard"
                src="/static/images/avatar/2.jpg"
                sx={{ width: 32, height: 32 }}
              />
            </Badge>
            <Typography variant="body2" className={classes.pageName}>
              phuong.ontiktok
            </Typography>
            <Grid
              display="flex"
              justifyContent="center"
              alignItems="center"
              className={classes.contentCamera}
            >
              <StaticImage
                src={CameraIcon}
                src2x={CameraIcon2x}
                src3x={CameraIcon3x}
              />
            </Grid>
          </Grid>
        ))}
      </AccordionDetails>
    </Accordion>
  );
};
