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
import { connectWebSocket, disconnectWebSocket } from "../../../../utils/websocketService";
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
  const { fbUser, user } = useAppSelector((state) => state.auth);
  const dispatch = useAppDispatch();

  const fetchFbPage = async () => {
    if (isLoading || !fbUserId || !fbUser) return;
    setIsLoading(true);
    try {
      const res = await getFbPage(fbUserId);
      setPagesInactive(res.data);
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
      // API sync thành công, giữ loading state và chờ WebSocket notification
      // Không reload ngay, chờ WebSocket báo "Đồng bộ thành công!"
    } catch (e) {
      console.log(e);
      setIsLoadingAdd(false);
    }
  };

  useEffect(() => {
    fetchFbPage();
  }, [fbUserId, fbUser]);

  // Lắng nghe WebSocket notification để reload trang khi đồng bộ thành công
  useEffect(() => {
    if (!user) return;

    const handleWebSocketMessage = (data: any) => {
      // Backend gửi object với status (số) và message
      if (data && data.status === 1 && data.message?.includes('Đồng bộ thành công')) {
        // WebSocket báo đồng bộ thành công (status=1=ACTIVE), reload trang
        setTimeout(() => {
          window.location.reload();
        }, 1000); // Delay 1 giây để user thấy thông báo
      } else if (data && data.status === 4) {
        // WebSocket báo đồng bộ thất bại (status=4=FAILED), tắt loading và hiển thị lỗi
        setIsLoadingAdd(false);
        console.error('Đồng bộ thất bại:', data.message);
        // Có thể hiển thị toast/alert thông báo lỗi
      }
    };

    // Kết nối WebSocket với userId
    connectWebSocket(user.id, handleWebSocketMessage);
    
    return () => {
      disconnectWebSocket();
    };
  }, [user]);

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
        style={{ position: 'relative' }}
      >
        {isLoading ? (
          <CircularProgress size={30} />
        ) : (
          <>
            {map(pagesInactive, (page, index) => (
              <Grid
                key={page?.pageId || `page-${index}`}
                display="flex"
                alignItems="center"
                justifyContent="space-between"
                className={classes.card}
                style={{ 
                  opacity: isLoadingAdd ? 0.5 : 1,
                  pointerEvents: isLoadingAdd ? 'none' : 'auto'
                }}
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
            
            {/* Loading overlay khi đang đồng bộ */}
            {isLoadingAdd && (
              <div
                style={{
                  position: 'absolute',
                  top: 0,
                  left: 0,
                  right: 0,
                  bottom: 0,
                  backgroundColor: 'rgba(255, 255, 255, 0.9)',
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'center',
                  justifyContent: 'center',
                  gap: '16px',
                  zIndex: 1000
                }}
              >
                <CircularProgress size={40} />
                <Typography variant="body2" color="primary" textAlign="center">
                  Đang đồng bộ dữ liệu...<br />
                  Vui lòng đợi cho đến khi nhận được thông báo thành công
                </Typography>
              </div>
            )}
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
          disabled={isLoadingAdd || pagesId.length === 0}
        >
          {isLoadingAdd ? (
            <>
              <CircularProgress size={18} color="inherit" style={{ marginRight: 8 }} />
              Đang đồng bộ...
            </>
          ) : (
            "Thêm"
          )}
        </Button>
      </Grid>
    </Dialog>
  );
};
