import { HTMLAttributes, FC, useState } from "react";
import { map } from "lodash-es";
import { Button } from "@mui/material";
import MenuItem from "@mui/material/MenuItem";
import Checkbox from "@mui/material/Checkbox";
import Chip from "@mui/material/Chip";
import LabelIcon from "@mui/icons-material/LabelImportant";
import { LIVE_TAGS } from "../../../constants/live";
import { useStyles, StyledMenu } from "./FilterTag.style";

interface IFilterTagProps {
  isHideLabel?: boolean;
}

export const FilterTag: FC<HTMLAttributes<HTMLDivElement> & IFilterTagProps> = (
  props
) => {
  const { isHideLabel, ...otherProps } = props;
  const classes = useStyles();
  const [anchorEl, setAnchorEl] = useState<HTMLButtonElement | null>(null);
  const open = Boolean(anchorEl);
  const id = open ? "simple-popover" : undefined;

  const handleOpen = (event: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  return (
    <div {...otherProps}>
      <Button
        variant="outlined"
        className={`${classes.btnTag} ${open && classes.activeBorder}`}
        startIcon={!isHideLabel && <LabelIcon />}
        onClick={handleOpen}
        fullWidth
      >
        {isHideLabel ? (
          <LabelIcon className={`${classes.icon} ${open && classes.active}`} />
        ) : (
          "Lọc thẻ"
        )}
      </Button>
      <StyledMenu id={id} anchorEl={anchorEl} open={open} onClose={handleClose}>
        {map(LIVE_TAGS, ({ color, title }) => (
          <MenuItem disableRipple key={color}>
            <Checkbox className={classes.checkBox} />
            <Chip
              label={title}
              style={{ background: color }}
              className={classes.label}
            />
          </MenuItem>
        ))}
      </StyledMenu>
    </div>
  );
};
