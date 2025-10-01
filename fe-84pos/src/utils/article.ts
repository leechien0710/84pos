import { ArticleType } from "../types/article";
import CameraIcon from "../assets/livestream/camera.webp";
import CameraIcon2x from "../assets/livestream/camera@2x.webp";
import CameraIcon3x from "../assets/livestream/camera@3x.webp";
import ImageIcon from "../assets/livestream/image.webp";
import ImageIcon2x from "../assets/livestream/image@2x.webp";
import ImageIcon3x from "../assets/livestream/image@3x.webp";
import PostImage from "../assets/livestream/post.webp";
import PostImage2x from "../assets/livestream/post@2x.webp";
import PostImage3x from "../assets/livestream/post@3x.webp";
import VideoIcon from "../assets/livestream/video.webp";
import VideoIcon2x from "../assets/livestream/video@2x.webp";
import VideoIcon3x from "../assets/livestream/video@3x.webp";

export const getArticleIcon = (type: ArticleType) => {
  if (type === "added_livetream") {
    return {
      icon1x: CameraIcon,
      icon2x: CameraIcon2x,
      icon3x: CameraIcon3x,
    };
  }
  if (type === "added_photos") {
    return {
      icon1x: ImageIcon,
      icon2x: ImageIcon2x,
      icon3x: ImageIcon3x,
    };
  }
  if (type === "added_video") {
    return {
      icon1x: VideoIcon,
      icon2x: VideoIcon2x,
      icon3x: VideoIcon3x,
    };
  }
  return {
    icon1x: PostImage,
    icon2x: PostImage2x,
    icon3x: PostImage3x,
  };
};
