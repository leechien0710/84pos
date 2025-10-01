import { NavigateFunction } from "react-router-dom";
import { PageRoutes } from "../constants/router";
import OverView from "../assets/app-bar/overview.webp";
import OverView2x from "../assets/app-bar/overview@2x.webp";
import OverView3x from "../assets/app-bar/overview@3x.webp";
import OverViewActive from "../assets/app-bar/overview-active.webp";
import OverViewActive2x from "../assets/app-bar/overview-active@2x.webp";
import OverViewActive3x from "../assets/app-bar/overview-active@3x.webp";
import Live from "../assets/app-bar/live.webp";
import Live2x from "../assets/app-bar/live@2x.webp";
import Live3x from "../assets/app-bar/live@3x.webp";
import LiveActive from "../assets/app-bar/live-active.webp";
import LiveActive2x from "../assets/app-bar/live-active@2x.webp";
import LiveActive3x from "../assets/app-bar/live-active@3x.webp";
import Post from "../assets/app-bar/post.webp";
import Post2x from "../assets/app-bar/post@2x.webp";
import Post3x from "../assets/app-bar/post@3x.webp";
import PostActive from "../assets/app-bar/post-active.webp";
import PostActive2x from "../assets/app-bar/post-active@2x.webp";
import PostActive3x from "../assets/app-bar/post-active@3x.webp";
import Conversation from "../assets/app-bar/conversation.webp";
import Conversation2x from "../assets/app-bar/conversation@2x.webp";
import Conversation3x from "../assets/app-bar/conversation@3x.webp";
import ConversationActive from "../assets/app-bar/conversation-active.webp";
import ConversationActive2x from "../assets/app-bar/conversation-active@2x.webp";
import ConversationActive3x from "../assets/app-bar/conversation-active@3x.webp";
import Order from "../assets/app-bar/order.webp";
import Order2x from "../assets/app-bar/order@2x.webp";
import Order3x from "../assets/app-bar/order@3x.webp";
import OrderActive from "../assets/app-bar/order-active.webp";
import OrderActive2x from "../assets/app-bar/order-active@2x.webp";
import OrderActive3x from "../assets/app-bar/order-active@3x.webp";
import Profile from "../assets/app-bar/profile.webp";
import Profile2x from "../assets/app-bar/profile@2x.webp";
import Profile3x from "../assets/app-bar/profile@3x.webp";
import ProfileActive from "../assets/app-bar/profile-active.webp";
import ProfileActive2x from "../assets/app-bar/profile-active@2x.webp";
import ProfileActive3x from "../assets/app-bar/profile-active@3x.webp";
import Configuration from "../assets/app-bar/configuration.webp";
import Configuration2x from "../assets/app-bar/configuration@2x.webp";
import Configuration3x from "../assets/app-bar/configuration@3x.webp";
import ConfigurationActive from "../assets/app-bar/configuration-active.webp";
import ConfigurationActive2x from "../assets/app-bar/configuration-active@2x.webp";
import ConfigurationActive3x from "../assets/app-bar/configuration-active@3x.webp";
import {
  redirectToLivestream,
  redirectToOverview,
  redirectToConversation,
  redirectToArticle,
} from "./redirect";

export const isHideAppBar = (pathname: string) => {
  return (
    pathname === PageRoutes.SelectPlatform ||
    pathname === PageRoutes.SelectAccount
  );
};

export const listMenu = (navigate: NavigateFunction) => {
  const menus = [
    {
      title: "Tổng quan",
      icon: { oneX: OverView, twoX: OverView2x, threeX: OverView3x },
      iconActive: {
        oneX: OverViewActive,
        twoX: OverViewActive2x,
        threeX: OverViewActive3x,
      },
      onAction: () => redirectToOverview(navigate),
      url: PageRoutes.Overview,
    },
    {
      title: "Livestream",
      icon: { oneX: Live, twoX: Live2x, threeX: Live3x },
      iconActive: {
        oneX: LiveActive,
        twoX: LiveActive2x,
        threeX: LiveActive3x,
      },
      onAction: () => redirectToLivestream(navigate),
      url: PageRoutes.Livestream,
    },
    {
      title: "Bài viết",
      icon: { oneX: Post, twoX: Post2x, threeX: Post3x },
      iconActive: {
        oneX: PostActive,
        twoX: PostActive2x,
        threeX: PostActive3x,
      },
      onAction: () => redirectToArticle(navigate),
      url: PageRoutes.Article,
    },
    {
      title: "Hội thoại",
      icon: {
        oneX: Conversation,
        twoX: Conversation2x,
        threeX: Conversation3x,
      },
      iconActive: {
        oneX: ConversationActive,
        twoX: ConversationActive2x,
        threeX: ConversationActive3x,
      },
      onAction: () => redirectToConversation(navigate),
      url: PageRoutes.Conversation,
    },
    {
      title: "Đơn hàng",
      icon: {
        oneX: Order,
        twoX: Order2x,
        threeX: Order3x,
      },
      iconActive: {
        oneX: OrderActive,
        twoX: OrderActive2x,
        threeX: OrderActive3x,
      },
    },
    {
      title: "Khách hàng",
      icon: {
        oneX: Profile,
        twoX: Profile2x,
        threeX: Profile3x,
      },
      iconActive: {
        oneX: ProfileActive,
        twoX: ProfileActive2x,
        threeX: ProfileActive3x,
      },
    },
    {
      title: "Cấu hình",
      icon: {
        oneX: Configuration,
        twoX: Configuration2x,
        threeX: Configuration3x,
      },
      iconActive: {
        oneX: ConfigurationActive,
        twoX: ConfigurationActive2x,
        threeX: ConfigurationActive3x,
      },
    },
  ];

  return menus;
};
