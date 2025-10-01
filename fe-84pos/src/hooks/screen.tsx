import { useEffect, useState } from "react";
import {
  RATIO_SWITCH,
  RATIO_HIDE_RIGHT,
  RATIO_HIDE_CHAT,
} from "../constants/layout";

export function useDeviceType() {
  const [isMobile, setIsMobile] = useState(false);

  useEffect(() => {
    const checkDevice = () => {
      setIsMobile(window.screen.width < RATIO_SWITCH);
    };

    checkDevice();
    window.addEventListener("resize", checkDevice);

    return () => window.removeEventListener("resize", checkDevice);
  }, []);

  return isMobile;
}

export function useHideRightMenu() {
  const [isHide, setIsHide] = useState(false);

  useEffect(() => {
    const checkDevice = () => {
      setIsHide(window.screen.width < RATIO_HIDE_RIGHT);
    };

    checkDevice();
    window.addEventListener("resize", checkDevice);

    return () => window.removeEventListener("resize", checkDevice);
  }, []);

  return isHide;
}

export function useHideChat() {
  const [isHide, setIsHide] = useState(false);

  useEffect(() => {
    const checkDevice = () => {
      setIsHide(window.screen.width < RATIO_HIDE_CHAT);
    };

    checkDevice();
    window.addEventListener("resize", checkDevice);

    return () => window.removeEventListener("resize", checkDevice);
  }, []);

  return isHide;
}
