import { HTMLAttributes, FC, useState, useEffect } from "react";
import InfiniteScroll from "react-infinite-scroll-component";
import { filter, includes, toLower, isUndefined } from "lodash-es";
import { Grid2 as Grid, Box, CircularProgress } from "@mui/material";
import { HeaderArticle } from "../../common/HeaderArticle";
import { LivePostCard } from "../../common/LivePostCard";
import { ArticleDetail } from "../ArticleDetail";
import { getArticles } from "../../../models/article";
import { getPageSelected } from "../../../utils/localstorage";
import { ArticleType, ILivePostCard } from "../../../types/article";
import { LIMIT_ARTICLES } from "../../../constants/article";
import { useStyles } from "./Article.style";

export const Article: FC<HTMLAttributes<HTMLDivElement>> = () => {
  const classes = useStyles();
  const [articleSelected, setArticleSelected] = useState<ILivePostCard | null>(
    null
  );
  const [articles, setArticles] = useState<ILivePostCard[]>([]);
  const [articlesFilter, setArticlesFilter] = useState<ILivePostCard[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [offset, setOffset] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [filterArticle, setFilterArticle] = useState<null | ArticleType>(null);

  const onFetchArticles = async (
    isShowLoading: boolean,
    articleType?: ArticleType
  ) => {
    isShowLoading && setIsLoading(true);
    try {
      const pageId = getPageSelected();
      const res = await getArticles(
        pageId || "",
        !isUndefined(articleType) ? articleType : filterArticle,
        !isUndefined(articleType) ? 0 : offset / LIMIT_ARTICLES
      );
      setFilterArticle(isUndefined(articleType) ? filterArticle : articleType);
      if (!isUndefined(articleType)) {
        setArticles(res?.content);
        setArticlesFilter(res?.content);
        setOffset(res?.content?.length || 0);
      } else {
        setArticles([...articles, ...(res?.content || [])]);
        setArticlesFilter([...articlesFilter, ...(res?.content || [])]);
        setOffset(offset + (res?.content?.length || 0));
      }
      setTotalPages(res?.page?.totalPages || 0);
    } catch (e) {
      console.log(e);
    }
    isShowLoading && setIsLoading(false);
  };

  const onSearchArticle = (keyword: string) => {
    const result = filter(articles, (item) =>
      includes(toLower(item.message), toLower(keyword))
    );
    setArticlesFilter(result);
  };

  useEffect(() => {
    onFetchArticles(true);
  }, []);

  if (articleSelected) {
    return (
      <ArticleDetail
        article={articleSelected}
        onBack={() => setArticleSelected(null)}
      />
    );
  }

  return (
    <Box className={classes.root}>
      <HeaderArticle
        onSearch={onSearchArticle}
        onFilterArticle={(articleType: ArticleType) =>
          onFetchArticles(true, articleType)
        }
        articleTypeValue={filterArticle}
      />
      <Box
        sx={{ flex: 1, overflowY: "auto", padding: 2 }}
        className={classes.content}
        id="content-articles"
      >
        {!isLoading ? (
          <InfiniteScroll
            dataLength={articlesFilter?.length || 0}
            next={() => onFetchArticles(false)}
            style={{
              display: "flex",
              flexDirection: "column",
              overflow: "hidden",
            }}
            inverse={false}
            hasMore={
              articlesFilter?.length === articles?.length &&
              Math.ceil(offset / LIMIT_ARTICLES) < totalPages
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
            scrollableTarget="content-articles"
          >
            {articlesFilter.map((live, idx) => (
              <LivePostCard
                livePost={live}
                key={idx}
                onClick={() => setArticleSelected(live)}
                className={classes.livePostCard}
              />
            ))}
          </InfiniteScroll>
        ) : (
          <Grid
            display="flex"
            justifyContent="center"
            alignItems="center"
            className={classes.contentLoading}
          >
            <CircularProgress />
          </Grid>
        )}
      </Box>
    </Box>
  );
};
