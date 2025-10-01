import { HTMLAttributes, FC, useEffect, useState, ChangeEvent } from "react";
import { Grid2 as Grid, Typography, CircularProgress } from "@mui/material";
import { HeaderBar } from "../../common/HeaderBar";
import { AccountTable } from "../../common/AccountTable";
import { BottomNavigation } from "../../common/BottomNavigation";
import { useAppDispatch, useAppSelector } from "../../../hook";
import { fetchListFbPageActive, getUserFacebook } from "../../../slices/auth";
import { getPageSelected, setPageSelected } from "../../../utils/localstorage";
import { useStyles } from "./SelectAccount.style";

export const SelectAccount: FC<HTMLAttributes<HTMLDivElement>> = () => {
  const classes = useStyles();
  const { user } = useAppSelector((state) => state.auth);
  const dispatch = useAppDispatch();
  const [loading, setLoading] = useState(false);
  const [hasInitialized, setHasInitialized] = useState(false);
  const [pageSelect, setPageSelect] = useState(getPageSelected());

  const onChangePage = (
    event: ChangeEvent<HTMLInputElement>,
    pageId: string
  ) => {
    if (event.target.checked) {
      setPageSelect(pageId);
      setPageSelected(pageId);
    }
  };

  useEffect(() => {
    const fetchDataFb = async () => {
      if (hasInitialized) return; // Tránh gọi nhiều lần
      
      setLoading(true);
      setHasInitialized(true);
      await dispatch(getUserFacebook());
      await dispatch(fetchListFbPageActive());
      setLoading(false);
    };

    if (!user) return;
    fetchDataFb();
  }, [user, hasInitialized, dispatch]);

  return (
    <Grid display="flex" flexDirection="column" className={classes.root}>
      <HeaderBar className={classes.header} />
      <Grid
        display="flex"
        justifyContent="center"
        alignItems="center"
        flexDirection="column"
        className={classes.form}
      >
        {loading ? (
          <CircularProgress size={32} />
        ) : (
          <>
            <Typography className={classes.title}>
              Chọn tài khoản để truy cập
            </Typography>
            <AccountTable
              type="facebook"
              className={classes.fbTable}
              pageSelect={pageSelect}
              onChangePage={onChangePage}
            />
          </>
        )}
      </Grid>
      {!loading && (
        <BottomNavigation
          className={classes.bottom}
          isAccessDisabled={!pageSelect}
        />
      )}
    </Grid>
  );
};
