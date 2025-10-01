import {
  SignIn,
  ForgotPassword,
  SignUp,
  SelectPlatform,
  SelectAccount,
  Overview,
  Livestream,
  LandingPage,
  Term,
  Policy,
  Conversation,
  Article,
} from "../pages";
import { PageRoutes } from "../constants/router";

export const ROUTERS_NOT_AUTH = [
  {
    path: PageRoutes.SignIn,
    element: <SignIn />,
  },
  {
    path: PageRoutes.SignUp,
    element: <SignUp />,
  },
  {
    path: PageRoutes.ForgotPassword,
    element: <ForgotPassword />,
  },
  {
    path: PageRoutes.LandingPage,
    element: <LandingPage />,
  },
  {
    path: PageRoutes.Term,
    element: <Term />,
  },
  {
    path: PageRoutes.Policy,
    element: <Policy />,
  },
];

export const ROUTERS_AUTH = [
  {
    path: PageRoutes.SelectPlatform,
    element: <SelectPlatform />,
  },
  {
    path: PageRoutes.SelectAccount,
    element: <SelectAccount />,
  },
  {
    path: PageRoutes.Overview,
    element: <Overview />,
  },
  {
    path: PageRoutes.Livestream,
    element: <Livestream />,
  },
  {
    path: PageRoutes.Conversation,
    element: <Conversation />,
  },
  {
    path: PageRoutes.Article,
    element: <Article />,
  },
];
