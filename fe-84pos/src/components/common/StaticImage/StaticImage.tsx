import React, { HTMLAttributes, FC, memo } from "react";

interface IStaticImageProps {
  src?: string;
  src2x?: string;
  src3x?: string;
  alt?: string;
  height?: number | string;
  width?: number | string;
}

export const StaticImage: FC<
  IStaticImageProps & HTMLAttributes<HTMLImageElement>
> = memo((props) => {
  const { src, src2x, src3x, alt, height, width, ...otherProps } = props;

  const dataSrc = src2x && src3x ? `${src} 1x, ${src2x} 2x, ${src3x} 3x` : src;

  return (
    <img
      {...otherProps}
      data-src={dataSrc}
      srcSet={dataSrc}
      alt={alt}
      height={height}
      width={width}
    />
  );
});
