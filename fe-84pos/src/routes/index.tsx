import { FC, HTMLAttributes, useEffect, useState } from "react";
import { RoutesNotAuth } from "./routesNotAuth";
import { RoutesAuth } from "./routesAuth";
import { useAppDispatch, useAppSelector } from "../hook";
import {
  currentUser,
  fetchListFbPageActive,
  getUserFacebook,
} from "../slices/auth";
import { getToken } from "../utils/localstorage";
import { decodeJWT } from "../utils/auth";
import { SplashScreen } from "../components/common/SplashScreen";
import Alert from "@mui/material/Alert";

export const Routing: FC<HTMLAttributes<HTMLDivElement>> = () => {
  const { user } = useAppSelector((state) => state.auth);
  const { isShowAlert, message, classify } = useAppSelector(
    (state) => state.alert
  );
  const dispatch = useAppDispatch();
  const [isLoading, setIsloading] = useState(true);

  useEffect(() => {
    const initializeApp = () => {
      const token = getToken();
      if (token) {
        const user = decodeJWT(token);
        if (user) {
          dispatch(currentUser({ user }));
        }
      }
      setIsloading(false);
    };

    initializeApp();
  }, [dispatch]);

  useEffect(() => {
    // useEffect này sẽ chạy sau khi user được xác định từ token
    const fetchDataForUser = async () => {
      if (user) {
        await dispatch(getUserFacebook());
        await dispatch(fetchListFbPageActive());
      }
    };

    if (!isLoading) {
      fetchDataForUser();
    }
  }, [user, isLoading, dispatch]);

  if (isLoading) {
    return <SplashScreen />;
  }

  return (
    <>
      {isShowAlert && (
        <Alert variant="filled" severity={classify}>
          {message}
        </Alert>
      )}
      {user ? <RoutesAuth /> : <RoutesNotAuth />}
    </>
  );
};
