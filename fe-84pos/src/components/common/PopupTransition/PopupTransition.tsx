import { forwardRef } from "react";
import { TransitionProps } from "@mui/material/transitions";
import Slide from "@mui/material/Slide";

export const Transition = forwardRef(function Transition(
  props: TransitionProps & {
    children: React.ReactElement<unknown>;
    direction?: "left" | "right" | "up" | "down";
  },
  ref: React.Ref<unknown>
) {
  return <Slide direction={props?.direction || "up"} ref={ref} {...props} />;
});
