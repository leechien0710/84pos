import { HTMLAttributes, FC, useState, MouseEvent, ChangeEvent } from "react";
import {
  Grid2 as Grid,
  TableRow,
  TableCell,
  Radio,
  Avatar,
  Popover,
  Typography,
  Divider,
} from "@mui/material";
import { StaticImage } from "../StaticImage";
import LoadMore from "../../../assets/select-account/load-more.webp";
import LoadMore2x from "../../../assets/select-account/load-more@2x.webp";
import LoadMore3x from "../../../assets/select-account/load-more@3x.webp";
import { IAccount, AccountType } from "../../../types/account";
import { DeleteAccount } from "../Popups/DeleteAccount";
import { AddPagePopup } from "../Popups/AddPagePopup";
import { useStyles } from "./AccountRow.style";

interface IAccountRowProps {
  row: IAccount;
  type: AccountType;
}

export const AccountRow: FC<
  HTMLAttributes<HTMLDivElement> & IAccountRowProps
> = (props) => {
  const classes = useStyles();
  const { row, type, className, ...otherProps } = props;
  const [anchorEl, setAnchorEl] = useState<HTMLImageElement | null>(null);
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [fbUserId, setFbUserId] = useState("");

  const onToggleDelete = () => {
    setIsOpen(!isOpen);
    setAnchorEl(null);
  };

  const handleClick = (event: MouseEvent<HTMLImageElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const onCloseAdd = () => {
    setFbUserId("");
    setAnchorEl(null);
  };

  const onOpenAdd = (id: string) => {
    setFbUserId(id);
  };

  const open = Boolean(anchorEl);
  const id = open ? "simple-popover" : undefined;

  return (
    <>
      <TableRow
        className={`${classes.root} ${className}`}
        key={row.name}
        {...otherProps}
      >
        <TableCell className={classes.tableCell} component="th" scope="row">
          <Grid display="flex" alignItems="center">
            {type === "tiktok" && (
              <Radio
                checked={row.isChecked}
                onChange={(event: ChangeEvent<HTMLInputElement>) => {}}
              />
            )}
            <Avatar src={row.avatar} className={classes.avatar} />
            {row.name}
          </Grid>
        </TableCell>
        {type === "tiktok" && (
          <TableCell
            className={`${classes.tableCell} ${classes.cellCustom}`}
            align="right"
          >
            {row.showName}
          </TableCell>
        )}
        <TableCell className={classes.tableCell} align="right">
          <StaticImage
            src={LoadMore}
            src2x={LoadMore2x}
            src3x={LoadMore3x}
            onClick={handleClick}
          />
          <Popover
            id={id}
            open={open}
            anchorEl={anchorEl}
            onClose={handleClose}
            anchorOrigin={{
              vertical: "bottom",
              horizontal: "left",
            }}
            PaperProps={{
              style: {
                borderRadius: 12,
                boxShadow: "rgba(99, 99, 99, 0.2) 0px 2px 8px 0px",
              },
            }}
          >
            <Typography
              className={classes.addPage}
              onClick={() => {
                onOpenAdd(row?.userId || "");
                handleClose();
              }}
            >
              Thêm page
            </Typography>
            <Divider />
            <Typography className={classes.deleteAcc} onClick={onToggleDelete}>
              Xóa liên kết tài khoản
            </Typography>
          </Popover>
        </TableCell>
      </TableRow>
      <DeleteAccount isOpen={isOpen} onClose={onToggleDelete} />
      <AddPagePopup
        isOpen={!!fbUserId}
        fbUserId={fbUserId}
        onClose={onCloseAdd}
      />
    </>
  );
};
