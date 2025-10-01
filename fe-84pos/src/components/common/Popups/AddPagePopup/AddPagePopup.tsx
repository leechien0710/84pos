import { HTMLAttributes, FC, useState, useEffect, ChangeEvent } from "react";
import {
  Grid2 as Grid,
  Typography,
  Dialog,
  Button,
  Checkbox,
  Avatar,
  CircularProgress,
} from "@mui/material";
import { differenceBy, find, map, filter } from "lodash-es";
import { Transition } from "../../PopupTransition";
import { addFbPages, getFbPage, syncAllPage } from "../../../../models/auth";
import { useAppDispatch, useAppSelector } from "../../../../hook";
import { IPage } from "../../../../types/account";
import { fetchListFbPageActive } from "../../../../slices/auth";
import { useStyles } from "./AddPagePopup.style";

interface IAddPagePopupProps {
  isOpen: boolean;
  onClose: () => void;
  fbUserId: string;
}

export const AddPagePopup: FC<
  HTMLAttributes<HTMLDivElement> & IAddPagePopupProps
> = (props) => {
  const { isOpen, fbUserId, className, onClose, ...otherProps } = props;
  const classes = useStyles();
  const [isLoading, setIsLoading] = useState(false);
  const [pagesInactive, setPagesInactive] = useState<IPage[]>();
  const [pagesId, setPagesId] = useState<string[]>([]);
  const [isLoadingAdd, setIsLoadingAdd] = useState(false);
  const { fbUser } = useAppSelector((state) => state.auth);
  const dispatch = useAppDispatch();

  const fetchFbPage = async () => {
    if (isLoading || !fbUserId || !fbUser) return;
    setIsLoading(true);
    try {
      const res = await getFbPage(fbUserId);
      const fbUserSelect = find(fbUser, { userId: fbUserId });
      const pagesUpdate = differenceBy(
        res as IPage[],
        fbUserSelect?.pages || [],
        "pageId"
      );
      setPagesInactive(pagesUpdate);
    } catch (e) {
      console.log(e);
    }
    setIsLoading(false);
  };

  const onSelectPage = (event: ChangeEvent<HTMLInputElement>, id: string) => {
    if (event.target.checked) {
      setPagesId([...pagesId, id]);
    } else {
      setPagesId(filter(pagesId, (pId) => pId !== id));
    }
  };

  const onAddPages = async () => {
    if (isLoadingAdd) return;
    setIsLoadingAdd(true);
    try {
      await addFbPages(pagesId);
      await syncAllPage(pagesId);
      await dispatch(fetchListFbPageActive());
      onClose();
    } catch (e) {
      console.log(e);
    }
    setIsLoadingAdd(false);
  };

  useEffect(() => {
    fetchFbPage();
  }, [fbUserId, fbUser]);

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
          Thêm page mới
        </Typography>
      </Grid>
      <div
        className={`${classes.content} ${isLoading && classes.contentLoading}`}
      >
        {isLoading ? (
          <CircularProgress size={30} />
        ) : (
          <>
            {map(pagesInactive, (page) => (
              <Grid
                display="flex"
                alignItems="center"
                justifyContent="space-between"
                className={classes.card}
              >
                <Grid display="flex" alignItems="center" gap={1}>
                  <Avatar src={page?.pageAvatarUrl} />
                  <Typography>{page?.pageName}</Typography>
                </Grid>
                <Checkbox
                  inputProps={{ "aria-label": page?.pageId }}
                  onChange={(event) => onSelectPage(event, page?.pageId)}
                />
              </Grid>
            ))}
          </>
        )}
      </div>
      <Grid
        display="flex"
        justifyContent="end"
        gap={1}
        className={classes.footer}
      >
        <Button
          variant="outlined"
          color="primary"
          className={classes.btnAction}
          onClick={onClose}
        >
          Huỷ
        </Button>
        <Button
          variant="contained"
          color="primary"
          className={classes.btnAction}
          onClick={onAddPages}
        >
          {isLoadingAdd ? (
            <CircularProgress size={18} color="inherit" />
          ) : (
            "Thêm"
          )}
        </Button>
      </Grid>
    </Dialog>
  );
};
