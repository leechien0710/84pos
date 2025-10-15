import { HTMLAttributes, FC, ChangeEvent, useState } from "react";
import {
  Grid2 as Grid,
  Radio,
  Avatar,
  Badge,
  Typography,
  Chip,
  TableRow,
  TableCell,
} from "@mui/material";
import { StaticImage } from "../StaticImage";
import Border from "../../../assets/select-account/border.webp";
import Border2x from "../../../assets/select-account/border@2x.webp";
import Border3x from "../../../assets/select-account/border@3x.png";
import Facebook from "../../../assets/select-account/facebook.webp";
import { removeFbPage } from "../../../models/auth";
import { useAppDispatch } from "../../../hook";
import { fetchListFbPageActive } from "../../../slices/auth";
import { useStyles } from "./FacebookAccountRow.style";

interface IFacebookAccountRowProps {
  avatar: string;
  name: string;
  pageId: string;
  pageSelect: string | null;
  status: number;
  onChangePage: (event: ChangeEvent<HTMLInputElement>, pageId: string) => void;
}

export const FacebookAccountRow: FC<
  HTMLAttributes<HTMLDivElement> & IFacebookAccountRowProps
> = (props) => {
  const classes = useStyles();
  const {
    avatar,
    name,
    pageId,
    status,
    pageSelect,
    onChangePage,
    className,
    ...otherProps
  } = props;
  const [isLoading, setIsLoading] = useState(false);
  const dispatch = useAppDispatch();

  const onRemovePage = async () => {
    if (!pageId || isLoading) return;
    setIsLoading(true);
    try {
      await removeFbPage([pageId]);
      // Reload trang để cập nhật dữ liệu mới
      window.location.reload();
    } catch (e) {
      console.log(e);
      setIsLoading(false);
    }
  };

  return (
    <TableRow className={`${classes.root} ${className}`} {...otherProps}>
      <TableCell
        className={classes.tableCell}
        component="th"
        scope="row"
        colSpan={3}
      >
        <Grid display="flex" alignItems="center">
          <Grid display="flex" alignItems="flex-start">
            <StaticImage
              src={Border}
              src2x={Border2x}
              src3x={Border3x}
              className={classes.borderImage}
            />
            <Radio
              checked={pageSelect === pageId}
              onChange={(event) => onChangePage(event, pageId)}
              value={pageSelect || ""}
              disabled={status !== 1}
            />
          </Grid>
          <Badge
            overlap="circular"
            anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
            badgeContent={
              <Avatar
                alt="facebook"
                src={Facebook}
                className={classes.avatarPlatform}
              />
            }
          >
            <Avatar alt="user-avatar" src={avatar} />
          </Badge>
          <Typography className={classes.name}>{name}</Typography>
          {status === 1 ? (
            <Chip label="Đang hoạt động" size="small" color="success" className={classes.chipActive} />
          ) : (
            <Chip label="Không hoạt động" size="small" className={classes.chipInactive} />
          )}
          {status === 1 && (
            <Chip
              label={isLoading ? "Đang xóa..." : "Gỡ page"}
              size="small"
              className={classes.chip}
              onClick={onRemovePage}
              disabled={isLoading}
            />
          )}
        </Grid>
      </TableCell>
    </TableRow>
  );
};
