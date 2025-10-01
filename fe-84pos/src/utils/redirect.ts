import { NavigateFunction } from "react-router-dom";
import { PageRoutes } from "../constants/router";

export const redirectToHome = (navigate: NavigateFunction) => {
  navigate(PageRoutes.SignIn);
};

export const redirectToSignUp = (navigate: NavigateFunction) => {
  navigate(PageRoutes.SignUp);
};

export const redirectToForgotPass = (navigate: NavigateFunction) => {
  navigate(PageRoutes.ForgotPassword);
};

export const redirectToOverview = (navigate: NavigateFunction) => {
  navigate(PageRoutes.Overview);
};

export const redirectToLivestream = (navigate: NavigateFunction) => {
  navigate(PageRoutes.Livestream);
};

export const redirectToTerm = (navigate: NavigateFunction) => {
  navigate(PageRoutes.Term);
};

export const redirectToPolicy = (navigate: NavigateFunction) => {
  navigate(PageRoutes.Policy);
};

export const redirectToLanding = (navigate: NavigateFunction) => {
  navigate(PageRoutes.LandingPage);
};

export const redirectToConversation = (navigate: NavigateFunction) => {
  navigate(PageRoutes.Conversation);
};

export const redirectToArticle = (navigate: NavigateFunction) => {
  navigate(PageRoutes.Article);
};
