import React, { HTMLAttributes, FC, ChangeEvent } from "react";
import {
  Grid2 as Grid,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Paper,
  Button,
  Typography,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import { pick } from "lodash-es";
import { StaticImage } from "../StaticImage";
import Tiktok from "../../../assets/sign-in/tiktok.webp";
import Tiktok2x from "../../../assets/sign-in/tiktok@2x.webp";
import Tiktok3x from "../../../assets/sign-in/tiktok@3x.webp";
import Facebook from "../../../assets/sign-in/facebook.webp";
import Facebook2x from "../../../assets/sign-in/facebook@2x.webp";
import Facebook3x from "../../../assets/sign-in/facebook@3x.webp";
import { AccountRow } from "../AccountRow";
import { FacebookAccountRow } from "../FacebookAccountRow";
import { AccountType } from "../../../types/account";
import { useAppSelector } from "../../../hook";
import { authWithFacebook } from "../../../models/auth";
import { useStyles } from "./AccountTable.style";

interface IAccountTableProps {
  type: AccountType;
  pageSelect: string | null;
  onChangePage: (event: ChangeEvent<HTMLInputElement>, pageId: string) => void;
}

export const AccountTable: FC<
  HTMLAttributes<HTMLDivElement> & IAccountTableProps
> = (props: IAccountTableProps) => {
  const { type, pageSelect, onChangePage, ...otherProps } = props;
  const classes = useStyles();
  const { fbUser } = useAppSelector((state) => state.auth);

  const onAuthPlatform = async () => {
    try {
      const { redirectUrl } = await authWithFacebook();
      if (redirectUrl) {
        window.location.href = redirectUrl;
      }
    } catch (error) {
      // Interceptor trong api.ts đã lo việc hiển thị popup lỗi.
      // Khối catch này chỉ để ngăn ứng dụng bị crash do unhandled promise rejection.
      console.error("Failed to get auth URL:", error);
    }
  };

  return (
    // <div {...otherProps} style={{ width: 700 }}>
    <div {...otherProps}>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 300 }} aria-label="customized table">
          <TableHead>
            <TableRow>
              <TableCell className={classes.tableCell}>
                <Grid display="flex" alignItems="center">
                  {/* {showBackButton && (
                    <Button
                      variant="outlined"
                      onClick={() => navigate("/overview")}
                      className={classes.btnAdd}
                      style={{ marginRight: "auto" }}
                    >
                      Quay về trang chính
                    </Button>
                  )} */}
                  <StaticImage
                    src={type === "facebook" ? Facebook : Tiktok}
                    src2x={type === "facebook" ? Facebook2x : Tiktok2x}
                    src3x={type === "facebook" ? Facebook3x : Tiktok3x}
                    className={classes.platformImg}
                  />
                  {type === "facebook" ? "Facebook" : "Tiktok"}
                </Grid>
              </TableCell>
              {type === "tiktok" && (
                <TableCell
                  className={`${classes.tableCell} ${classes.cellCustom}`}
                  align="right"
                />
              )}
              <TableCell className={classes.tableCell} align="right">
                <Button
                  variant="contained"
                  startIcon={<AddIcon />}
                  className={classes.btnAdd}
                  onClick={onAuthPlatform}
                  // disabled={!!fbUser}
                >
                  Thêm tài khoản
                </Button>
              </TableCell>
            </TableRow>
            <TableRow className={classes.head}>
              <TableCell className={classes.tableCell}>
                <Grid display="flex" alignItems="center">
                  ID
                </Grid>
              </TableCell>
              {type === "tiktok" && (
                <TableCell
                  className={`${classes.tableCell} ${classes.cellCustom}`}
                  align="right"
                >
                  Tên hiển thị
                </TableCell>
              )}
              <TableCell className={classes.tableCell} align="right">
                Thao tác
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {fbUser && fbUser.length > 0 ? (
              <>
                {fbUser.map((row, index) => (
                  <React.Fragment key={`user-${row.userId || index}`}>
                    <AccountRow
                      row={{
                        ...pick(row, ["name", "avatar", "userId"]),
                        isChecked: false,
                      }}
                      type={type}
                    />
                    {type === "facebook" &&
                      row?.pages?.map((sub, subIndex) => (
                        <FacebookAccountRow
                          key={`page-${sub?.pageId || subIndex}`}
                          avatar={sub?.pageAvatarUrl}
                          name={sub?.pageName}
                          pageId={sub?.pageId}
                          pageSelect={pageSelect}
                          onChangePage={onChangePage}
                          status={sub?.status}
                        />
                      ))}
                  </React.Fragment>
                ))}
              </>
            ) : (
              <TableRow>
                <TableCell
                  className={classes.tableCell}
                  component="th"
                  scope="row"
                  colSpan={3}
                >
                  <Typography textAlign="center">
                    Hiện chưa có tài khoản Facebook nào được liên kết
                  </Typography>
                </TableCell>
              </TableRow>
            )}
            {/* {rows.map((row) => (
              <>
                <AccountRow row={row} type={type} />
                {type === "facebook" &&
                  row?.listSubAccount?.map((subAccount) => (
                    <FacebookAccountRow {...subAccount} />
                  ))}
              </>
            ))} */}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
};
