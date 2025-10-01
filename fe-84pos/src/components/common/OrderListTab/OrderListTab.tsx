import { HTMLAttributes, FC } from "react";
import {
  Grid2 as Grid,
  Typography,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Button,
} from "@mui/material";
import { map } from "lodash-es";
import SearchIcon from "@mui/icons-material/Search";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import Call from "@mui/icons-material/Call";
import Calendar from "@mui/icons-material/CalendarMonth";
import { StaticImage } from "../StaticImage";
import Cart from "../../../assets/livestream/cart.webp";
import Cart2x from "../../../assets/livestream/cart@2x.webp";
import Cart3x from "../../../assets/livestream/cart@3x.webp";
import {
  useStyles,
  Search,
  SearchIconWrapper,
  StyledInputBase,
} from "./OrderListTab.style";

const ORDER_LIST = [
  {
    username: "Công chúa bong bóng",
    createdAt: "11/06/2024 15:03 PM",
    messageList: [
      {
        message: "Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
      {
        message:
          "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
      {
        message:
          "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
    ],
  },
  {
    username: "Công chúa bong bóng",
    createdAt: "11/06/2024 15:03 PM",
    messageList: [
      {
        message: "Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
      {
        message:
          "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
      {
        message:
          "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
    ],
  },
  {
    username: "Công chúa bong bóng",
    createdAt: "11/06/2024 15:03 PM",
    messageList: [
      {
        message: "Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
      {
        message:
          "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
      {
        message:
          "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
    ],
  },
  {
    username: "Công chúa bong bóng",
    createdAt: "11/06/2024 15:03 PM",
    messageList: [
      {
        message: "Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
      {
        message:
          "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
      {
        message:
          "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
    ],
  },
  {
    username: "Công chúa bong bóng",
    createdAt: "11/06/2024 15:03 PM",
    messageList: [
      {
        message: "Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
      {
        message:
          "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
      {
        message:
          "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
    ],
  },
  {
    username: "Công chúa bong bóng",
    createdAt: "11/06/2024 15:03 PM",
    messageList: [
      {
        message: "Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
      {
        message:
          "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
      {
        message:
          "Lấy cho chị chân váy đen Lấy cho chị chân váy đen Lấy cho chị chân váy đen",
        createdAt: "11/06/2024 15:03",
      },
    ],
  },
];

interface IOrderListTabProps {
  hideSearch?: boolean;
  contentClass?: string;
}

export const OrderListTab: FC<
  HTMLAttributes<HTMLDivElement> & IOrderListTabProps
> = (props) => {
  const { hideSearch, contentClass, ...otherProps } = props;
  const classes = useStyles();

  return (
    <div {...otherProps}>
      {!hideSearch ? (
        <>
          <Search>
            <SearchIconWrapper>
              <SearchIcon />
            </SearchIconWrapper>
            <StyledInputBase
              placeholder="Tìm kiếm đơn hàng"
              inputProps={{ "aria-label": "search" }}
            />
          </Search>
          <Typography variant="subtitle1" className={classes.title}>
            Danh sách KH có đơn (80)
          </Typography>
        </>
      ) : (
        <Grid display="flex" alignItems="center" justifyContent="space-between">
          <Typography variant="body1" className={classes.label}>
            Đơn của khách
          </Typography>
          <Button
            variant="outlined"
            className={classes.btnTime}
            color="inherit"
            startIcon={<Calendar />}
          >
            Thời gian
          </Button>
        </Grid>
      )}
      <div className={`${classes.content} ${contentClass}`}>
        {map(ORDER_LIST, ({ username, createdAt, messageList }, idx) => (
          <Accordion key={idx} className={classes.orderItem}>
            <AccordionSummary
              expandIcon={<ExpandMoreIcon />}
              aria-controls="panel1-content"
              id="panel1-header"
            >
              <div>
                <Grid display="flex" alignItems="center" gap={1}>
                  <Typography variant="subtitle2" className={classes.link}>
                    #7012
                  </Typography>
                  <Typography variant="body2" className={classes.created}>
                    {createdAt}
                  </Typography>
                </Grid>
                <Grid display="flex" alignItems="center" gap={1}>
                  <Typography variant="subtitle2">{username}</Typography>
                  <Call className={classes.actionIcon} />
                </Grid>
                <Grid display="flex" alignItems="center" gap={1}>
                  <StaticImage
                    src={Cart}
                    src2x={Cart2x}
                    src3x={Cart3x}
                    height={16}
                    width={14}
                  />
                  <Typography variant="body2" className={classes.amount}>
                    ({messageList?.length})
                  </Typography>
                  <Typography variant="body2" className={classes.created}>
                    đơn hàng
                  </Typography>
                </Grid>
              </div>
            </AccordionSummary>
            <AccordionDetails>
              {map(messageList, ({ createdAt, message }, idx) => (
                <Grid display="flex" gap={1} key={idx}>
                  <Typography
                    variant="body2"
                    flex={1}
                    className={classes.created}
                  >
                    {createdAt}
                  </Typography>
                  <Typography flex={2} variant="body2">
                    {message}
                  </Typography>
                </Grid>
              ))}
            </AccordionDetails>
          </Accordion>
        ))}
      </div>
    </div>
  );
};
