import { HTMLAttributes, FC, useEffect, useState } from "react";
import { map, filter, includes, toLower } from "lodash-es";
import InfiniteScroll from "react-infinite-scroll-component";
import {
  Grid2 as Grid,
  Box,
  Typography,
  Button,
  CircularProgress,
} from "@mui/material";
import BackIcon from "@mui/icons-material/ArrowBackIos";
import FilterAltIcon from "@mui/icons-material/FilterAlt";
import SearchIcon from "@mui/icons-material/Search";
import CloseIcon from "@mui/icons-material/Close";
import { LivePostCard } from "../../common/LivePostCard";
import { LiveCard } from "../../common/LiveCard";
import { getArticleDetail } from "../../../models/article";
import { IComment, ILivePostCard } from "../../../types/article";
import { LIMIT_COMMENTS } from "../../../constants/article";
import {
  useStyles,
  Search,
  SearchIconWrapper,
  StyledInputBase,
} from "./ArticleDetail.style";

interface IArticleDetailProps {
  article: ILivePostCard;
  onBack: () => void;
}

export const ArticleDetail: FC<
  HTMLAttributes<HTMLDivElement> & IArticleDetailProps
> = (props) => {
  const classes = useStyles();
  const { article, onBack, ...otherProps } = props;
  const [isLoading, setIsLoading] = useState(false);
  const [offset, setOffset] = useState(0);
  const [comments, setComments] = useState<IComment[]>([]);
  const [commentsFilter, setCommentsFilter] = useState<IComment[]>([]);
  const [hasPhone, setHasPhone] = useState(false);
  const [totalPages, setTotalPages] = useState(0);

  const onFetchDetail = async (
    isShowLoading: boolean = true,
    _hasPhone?: boolean
  ) => {
    if (!article) return;
    if (isShowLoading) {
      setIsLoading(true);
    }
    try {
      const res = await getArticleDetail(
        article?.id,
        _hasPhone && isShowLoading ? 0 : offset / LIMIT_COMMENTS,
        _hasPhone
      );
      if (_hasPhone && isShowLoading) {
        setComments(res?.content);
        setCommentsFilter(res?.content);
        setOffset(res?.content?.length || 0);
      } else {
        setComments([...comments, ...res?.content]);
        setCommentsFilter([...commentsFilter, ...res?.content]);
        setOffset(offset + (res?.content?.length || 0));
      }
      setTotalPages(res?.page?.totalPages);
    } catch (e) {
      console.log(e);
    }
    if (isShowLoading) {
      setIsLoading(false);
    }
  };

  const onSearchComment = (keyword: string) => {
    const result = filter(comments, (item) =>
      includes(toLower(item.message), toLower(keyword))
    );
    setCommentsFilter(result);
  };

  const onToggleHasPhone = async () => {
    setHasPhone(!hasPhone);
    await onFetchDetail(true, !hasPhone);
  };

  useEffect(() => {
    onFetchDetail();
  }, [article]);

  return (
    <Box {...otherProps} className={classes.root}>
      <div className={classes.header}>
        <Grid display="flex" alignItems="center" gap={1}>
          <BackIcon onClick={onBack} />
          <Typography variant="button" className={classes.title}>
            Trở lại danh sách bài viết
          </Typography>
        </Grid>
        <LivePostCard livePost={article} className={classes.livePostCard} />
      </div>
      <Box display="flex" flexDirection="column" className={classes.container}>
        {isLoading ? (
          <Grid
            display="flex"
            justifyContent="center"
            alignItems="center"
            className={classes.contentLoading}
          >
            <CircularProgress />
          </Grid>
        ) : (
          <>
            <Box
              display="flex"
              justifyContent="end"
              gap={1}
              className={classes.mainFilter}
            >
              <Button
                id="basic-button"
                aria-haspopup="true"
                variant={hasPhone ? "contained" : "outlined"}
                color={hasPhone ? "primary" : "inherit"}
                startIcon={!hasPhone && <FilterAltIcon />}
                endIcon={hasPhone && <CloseIcon />}
                className={`${classes.btn} ${hasPhone && classes.btnActive}`}
                onClick={onToggleHasPhone}
              >
                <Typography variant="caption" className={classes.textFilter}>
                  Bình luận có số điện thoại
                </Typography>
              </Button>
              <Search>
                <SearchIconWrapper>
                  <SearchIcon />
                </SearchIconWrapper>
                <StyledInputBase
                  placeholder="Tìm kiếm bình luận"
                  inputProps={{ "aria-label": "search" }}
                  onChange={(e) => onSearchComment(e.target.value)}
                />
              </Search>
            </Box>
            <Box className={classes.content} id="content-comment">
              <InfiniteScroll
                dataLength={commentsFilter?.length || 0}
                next={() => onFetchDetail(false)}
                style={{
                  display: "flex",
                  flexDirection: "column",
                  overflow: "hidden",
                }}
                inverse={false}
                hasMore={
                  commentsFilter?.length === comments?.length &&
                  Math.ceil(offset / LIMIT_COMMENTS) < totalPages
                }
                loader={
                  <Grid
                    display="flex"
                    justifyContent="center"
                    alignItems="center"
                    marginTop={2}
                  >
                    <CircularProgress />
                  </Grid>
                }
                scrollableTarget="content-comment"
              >
                {map(commentsFilter, (cmt, idx) => (
                  <LiveCard
                    key={idx}
                    avatar="https://contentgrid.homedepot-static.com/hdus/en_US/DTCCOMNEW/Articles/discover-the-secret-language-of-flowers-2022-hero.jpeg"
                    username={JSON.parse(cmt?.fromUser)?.name}
                    createdAt={cmt?.createdTime}
                    message={cmt?.message}
                    onSetMessageId={() => null}
                  />
                ))}
              </InfiniteScroll>
            </Box>
          </>
        )}
      </Box>
    </Box>
  );
};
