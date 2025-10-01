import { BrowserRouter, Route, Routes } from "react-router-dom";
import { map } from "lodash-es";
import { ROUTERS_NOT_AUTH } from "./routers";
import { LandingPage } from "../pages";

export const RoutesNotAuth = () => {
  return (
    <BrowserRouter>
      <Routes>
        {map(ROUTERS_NOT_AUTH, (r, idx) => (
          <Route path={r.path} element={r.element} key={idx} />
        ))}
        <Route path="*" element={<LandingPage />} />
      </Routes>
    </BrowserRouter>
  );
};
