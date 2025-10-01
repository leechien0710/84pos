export interface IAccount {
  avatar: string;
  name: string;
  showName?: string;
  isChecked: boolean;
  userId?: string;
}

export interface IUser {
  username: string;
  role: string;
  id: number;
  iat: number;
  exp: number;
}

export interface IFbUser {
  avatar: string;
  expiresAt: string;
  name: string;
  userAppId: number;
  userId: string;
  pages?: IPage[];
}

export interface IPage {
  pageAvatarUrl: string;
  pageId: string;
  pageName: string;
}

export type AccountType = "tiktok" | "facebook";
