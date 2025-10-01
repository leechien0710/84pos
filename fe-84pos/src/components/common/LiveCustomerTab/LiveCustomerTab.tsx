import { HTMLAttributes, FC } from "react";
import { map } from "lodash-es";
import {
  Grid2 as Grid,
  Typography,
  Avatar,
  Chip,
  TextField,
} from "@mui/material";
import Call from "@mui/icons-material/Call";
import Map from "@mui/icons-material/FmdGood";
import { LIVE_TAGS } from "../../../constants/live";
import { EmptyLive } from "../EmptyLive";
import { useStyles } from "./LiveCustomerTab.style";

const LIST_MESSAGE = [
  { createdAt: "11/06/2024 15:03", message: "Lấy cho chị chân váy đen" },
  {
    createdAt: "11/06/2024 15:03",
    message:
      "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị chân váy đen",
  },
  { createdAt: "11/06/2024 15:03", message: "Lấy cho chị chân váy đen" },
  {
    createdAt: "11/06/2024 15:03",
    message:
      "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị chân váy đen",
  },
  {
    createdAt: "11/06/2024 15:03",
    message: "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị",
  },
  {
    createdAt: "11/06/2024 15:03",
    message: "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị",
  },
  {
    createdAt: "11/06/2024 15:03",
    message: "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị",
  },
  { createdAt: "11/06/2024 15:03", message: "Lấy cho chị chân váy đen" },
  {
    createdAt: "11/06/2024 15:03",
    message:
      "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị chân váy đen",
  },
];

const LIST_ORDER = [
  { createdAt: "11/06/2024 15:03", message: "M124" },
  { createdAt: "11/06/2024 15:03", message: "M18 - 0989998999" },
];

interface ILiveCustomerTabProps {
  type: "customer" | "order";
  isHideContent?: boolean;
}

export const LiveCustomerTab: FC<
  HTMLAttributes<HTMLDivElement> & ILiveCustomerTabProps
> = (props) => {
  const { type, isHideContent } = props;
  const classes = useStyles();

  return (
    <div {...props}>
      <Grid display="flex" gap={2}>
        <Avatar
          src="https://contentgrid.homedepot-static.com/hdus/en_US/DTCCOMNEW/Articles/discover-the-secret-language-of-flowers-2022-hero.jpeg"
          alt="avatar"
        />
        <div>
          <Typography variant="body1" className={classes.username}>
            Công chúa bong bóng
          </Typography>
          {map(LIVE_TAGS, ({ color, title }) => (
            <Chip
              key={color}
              label={title}
              style={{ background: color }}
              className={classes.label}
            />
          ))}
        </div>
      </Grid>
      <div className={classes.info}>
        <div>
          <Grid
            display="flex"
            justifyContent="space-between"
            alignItems="center"
          >
            <Grid display="flex" alignItems="center" gap={1}>
              <Call className={classes.call} />
              <Typography variant="body2" className={classes.title}>
                Số điện thoại
              </Typography>
            </Grid>
            <Typography variant="body2" className={classes.link}>
              Thêm số điện thoại
            </Typography>
          </Grid>
          <TextField
            fullWidth
            className={classes.inputField}
            placeholder="Nhập số điện thoại"
            autoComplete="false"
            inputProps={{ autoComplete: "false" }}
          />
        </div>
        <div className={classes.address}>
          <Grid
            display="flex"
            justifyContent="space-between"
            alignItems="center"
          >
            <Grid display="flex" alignItems="center" gap={1}>
              <Map className={classes.call} />
              <Typography variant="body2" className={classes.title}>
                Địa chỉ
              </Typography>
            </Grid>
            <Typography variant="body2" className={classes.link}>
              Thêm địa chỉ
            </Typography>
          </Grid>
          <TextField
            fullWidth
            className={classes.inputField}
            placeholder="Nhập địa chỉ"
            autoComplete="false"
            inputProps={{ autoComplete: "false" }}
          />
        </div>
      </div>
      {!isHideContent && (
        <div className={classes.content}>
          <Typography variant="body1" className={classes.allCmt}>
            {type === "customer" ? "Tất cả bình luận (10)" : "Đơn hàng (2)"}
          </Typography>
          <div className={classes.messages}>
            {/* <EmptyLive type="order" className={classes.empty} /> */}
            {map(
              type === "customer" ? LIST_MESSAGE : LIST_ORDER,
              ({ createdAt, message }, idx) => (
                <Grid key={idx} display="flex" gap={1}>
                  <Typography
                    flex={1}
                    variant="body2"
                    className={classes.createdAt}
                  >
                    {createdAt}
                  </Typography>
                  <Typography flex={2} variant="body2">
                    {message}
                  </Typography>
                </Grid>
              )
            )}
          </div>
        </div>
      )}
    </div>
  );
};
