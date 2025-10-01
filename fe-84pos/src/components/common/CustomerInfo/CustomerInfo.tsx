import { FC, HTMLAttributes, useState } from "react";
import {
  Typography,
  Accordion,
  AccordionSummary,
  AccordionDetails,
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { LiveCustomerTab } from "../LiveCustomerTab";
import { OrderListTab } from "../OrderListTab";
import { useStyles } from "./CustomerInfo.style";
import { useHideRightMenu } from "../../../hooks/screen";

export const CustomerInfo: FC<HTMLAttributes<HTMLDivElement>> = (props) => {
  const classes = useStyles();
  const [expanded, setExpanded] = useState(false);
  const isHideRight = useHideRightMenu();

  const handleExpansion = () => {
    setExpanded((prevExpanded) => !prevExpanded);
  };

  const getContentClasses = () => {
    if (!isHideRight) {
      return expanded ? classes.contentOrderExpand : classes.contentOrder;
    }
    return expanded
      ? classes.contentOrderSmallExpand
      : classes.contentOrderSmall;
  };

  return (
    <div {...props}>
      <Accordion expanded={expanded} onChange={handleExpansion}>
        <AccordionSummary
          expandIcon={<ExpandMoreIcon />}
          aria-controls="panel1-content"
          id="panel1-header"
        >
          <Typography variant="body1" className={classes.title}>
            Thông tin khách hàng
          </Typography>
        </AccordionSummary>
        <AccordionDetails>
          <LiveCustomerTab type="customer" isHideContent />
        </AccordionDetails>
      </Accordion>
      <OrderListTab
        hideSearch
        className={classes.orderCard}
        contentClass={getContentClasses()}
      />
    </div>
  );
};
