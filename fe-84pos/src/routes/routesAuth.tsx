import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";
import { map } from "lodash-es";
import { ROUTERS_AUTH } from "./routers";
import { Layout } from "../components/common/Layout";
import { getPageSelected } from "../utils/localstorage";
import { PageRoutes } from "../constants/router";

export const RoutesAuth = () => {
  const pageSelected = getPageSelected();

  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          {map(ROUTERS_AUTH, (r) => (
            <Route key={r.path} path={r.path} element={r.element} />
          ))}
          <Route
            path="*"
            element={
              <Navigate
                to={
                  pageSelected ? PageRoutes.Overview : PageRoutes.SelectAccount
                }
                replace
              />
            }
          />
        </Routes>
      </Layout>
    </BrowserRouter>
  );
};
