import { HTMLAttributes, FC } from "react";
import { HeaderLanding } from "../../common/HeaderLanding";
import { IntroLanding } from "../../common/IntroLanding";
import { ServiceLanding } from "../../common/ServiceLanding";

export const LandingPage: FC<HTMLAttributes<HTMLDivElement>> = () => {
  return (
    <div>
      <HeaderLanding />
      <IntroLanding />
      <ServiceLanding />
    </div>
  );
};
