import { HTMLAttributes, FC, useState, useEffect } from "react";
import {
  Grid2 as Grid,
  Button,
  Dialog,
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  CircularProgress,
} from "@mui/material";
import InfiniteScroll from "react-infinite-scroll-component";
import CloseIcon from "@mui/icons-material/Close";
import { HeaderSelectConversation } from "../../common/HeaderSelectConsersation";
import { ConversationCard } from "../../common/ConversationCard";
import { HeaderContentConversation } from "../../common/HeaderContentConversation";
import { ChatSection } from "../../common/ChatSection";
import { CustomerInfo } from "../../common/CustomerInfo";
import {
  getConversation,
  getConversationDetail,
} from "../../../models/conversation";
import {
  useHideChat,
  useHideRightMenu,
  useDeviceType,
} from "../../../hooks/screen";
import { Transition } from "../../common/PopupTransition";
import { ChatMobile } from "../../common/ChatMobile";
import { IConversation, IMessage } from "../../../types/conversation";
import { convertMessage, getSenderName } from "../../../utils/conversation";
import { LIMIT_CONVERSATION } from "../../../constants/conversation";
import { useStyles } from "./Conversation.style";

export const Conversation: FC<HTMLAttributes<HTMLDivElement>> = () => {
  const classes = useStyles();
  const isHideRight = useHideRightMenu();
  const isHideChat = useHideChat();
  const isMobile = useDeviceType();
  const [onOpenRight, setOnOpenRight] = useState(false);
  const [isOpenChatMobile, setIsOpenChatMobile] = useState(false);
  const [conversations, setConversations] = useState<IConversation[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [pageSelected, setPageSelected] = useState("");
  const [messages, setMessages] = useState<IMessage[] | null>(null);
  const [offset, setOffset] = useState(0);
  const [offsetMessage, setOffsetMessage] = useState(0);
  const [conversationSelected, setConversationSelected] = useState<string>("");
  const [isEndConversation, setIsEndConversation] = useState(false);

  const renderRightAction = () => (
    <div className={`${classes.section} ${classes.secondSection}`}>
      <CustomerInfo />
    </div>
  );

  const onFetchConversation = async (
    pageId: string,
    offsetCustom?: number,
    isIgnoreLoading?: boolean
  ) => {
    if (!isIgnoreLoading) {
      setIsLoading(true);
      setOffsetMessage(0);
    }
    setPageSelected(pageId);
    try {
      const res = await getConversation(
        pageId,
        LIMIT_CONVERSATION,
        offsetCustom ?? offset
      );
      if (pageId !== pageSelected) {
        setConversations(res?.data);
        setOffset(res?.data?.length);
      } else {
        setConversations([...(conversations || []), ...res?.data]);
        setOffset([...(conversations || []), ...res?.data].length);
      }
      if (res?.data?.length < LIMIT_CONVERSATION) {
        setIsEndConversation(true);
      }
    } catch (e) {
      console.log(e);
    }
    if (!isIgnoreLoading) {
      setIsLoading(false);
    }
  };

  const onFetchMessage = async (
    id: string,
    offset?: number,
    isFetchMore = false
  ) => {
    try {
      if (!isFetchMore) {
        setMessages(null);
        const res = await getConversationDetail(pageSelected, id, 20, offset);
        setMessages(res?.messages?.data);
        setOffsetMessage(res?.messages?.data?.length || 0);
      } else {
        const res = await getConversationDetail(
          pageSelected,
          id,
          20,
          offsetMessage
        );
        setMessages([...(messages || []), ...res?.messages?.data]);
        setOffsetMessage([...(messages || []), ...res?.messages?.data].length);
      }
    } catch (e) {
      console.log(e);
      if (!isFetchMore) {
        setMessages([]);
      }
    }
  };

  const onToggleRight = () => {
    setOnOpenRight(!onOpenRight);
  };

  const onToggleChatMobile = async (chatId?: string) => {
    setIsOpenChatMobile(!isOpenChatMobile);
    if (chatId) {
      setConversationSelected(chatId);
      await onFetchMessage(chatId, 0);
    }
  };

  const renderChatSection = () => {
    if (!messages) {
      return (
        <Grid
          display="flex"
          justifyContent="center"
          alignItems="center"
          className={classes.contentLoading}
          flex={1}
        >
          <CircularProgress />
        </Grid>
      );
    }
    const messageFormat = convertMessage(messages, pageSelected);
    const senderName = getSenderName(messages, pageSelected);
    console.log("onLoadMore: ", messageFormat);
    return (
      <div className={classes.detail}>
        <HeaderContentConversation
          onOpenDetail={onToggleRight}
          username={senderName}
        />
        <div className={classes.mainChat}>
          <ChatSection
            messages={messageFormat}
            onLoadMore={async () => {
              conversationSelected &&
                (await onFetchMessage(
                  conversationSelected,
                  offsetMessage,
                  true
                ));
            }}
          />
        </div>
      </div>
    );
  };

  const renderInforPopup = () => {
    if (isHideRight && onOpenRight) {
      return (
        <Dialog
          fullScreen
          open
          onClose={onToggleRight}
          TransitionComponent={(props) => (
            <Transition {...props} direction="left" />
          )}
        >
          <AppBar color="inherit" sx={{ position: "relative" }}>
            <Toolbar sx={{ display: "flex", justifyContent: "space-between" }}>
              <Typography variant="body1">Đóng</Typography>
              <IconButton
                edge="start"
                color="inherit"
                onClick={onToggleRight}
                className={classes.closeIcon}
              >
                <CloseIcon />
              </IconButton>
            </Toolbar>
          </AppBar>
          {renderRightAction()}
        </Dialog>
      );
    }
  };

  useEffect(() => {
    if (!conversations) return;
    setConversationSelected(conversations?.[0]?.id);
    onFetchMessage(conversations?.[0]?.id, offsetMessage);
  }, [conversations]);

  if (isOpenChatMobile && isHideChat) {
    return (
      <>
        <ChatMobile
          renderContent={renderChatSection}
          onBack={onToggleChatMobile}
        />
        {renderInforPopup()}
      </>
    );
  }

  console.log(
    "aaa: ",
    conversations?.length,
    offset,
    conversations?.length === offset
  );

  return (
    <div>
      <Grid
        display="flex"
        gap={2}
        className={`${isMobile && classes.actionsSpace}`}
      >
        <Button variant="contained" className={classes.btn}>
          Tất cả
        </Button>
        <Button
          variant="contained"
          className={`${classes.inActive} ${classes.btn}`}
        >
          Tin nhắn
        </Button>
        <Button
          variant="contained"
          className={`${classes.inActive} ${classes.btn}`}
        >
          Bình luận
        </Button>
      </Grid>
      <Grid
        display="flex"
        justifyContent="space-between"
        gap={2}
        className={classes.main}
      >
        <Grid
          display="flex"
          className={`${classes.section} ${classes.mainSection}`}
        >
          <div
            className={`${classes.listMess} ${isHideChat && classes.fullWidth}`}
          >
            <HeaderSelectConversation
              className={classes.hasBottomBorder}
              onFetchConversation={onFetchConversation}
            />
            <div className={classes.content} id="content">
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
                <InfiniteScroll
                  dataLength={conversations?.length || 0}
                  next={() =>
                    onFetchConversation(pageSelected, undefined, true)
                  }
                  style={{ display: "flex", flexDirection: "column" }}
                  inverse={false}
                  hasMore={!isLoading && !isEndConversation}
                  loader={
                    <Grid
                      display="flex"
                      justifyContent="center"
                      alignItems="center"
                      marginTop={2}
                    >
                      <h2>Loading 1 ...</h2>
                      {/* <CircularProgress /> */}
                    </Grid>
                  }
                  scrollableTarget="content"
                >
                  {(conversations || []).map((c, idx) => (
                    <ConversationCard
                      className={classes.hasBottomBorder}
                      onClick={() => onToggleChatMobile(c?.id)}
                      key={idx}
                      conversation={c}
                    />
                  ))}
                </InfiniteScroll>
              )}
            </div>
          </div>
          {!isHideChat && renderChatSection()}
        </Grid>
        {!isHideRight && renderRightAction()}
        {renderInforPopup()}
      </Grid>
    </div>
  );
};
