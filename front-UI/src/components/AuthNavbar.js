import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import { useNavigate } from "react-router-dom";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import i18n from "i18next";
import LanguageIcon from "@mui/icons-material/Language";
import {
  AccountCircle,
  LoginTwoTone,
  LogoDevOutlined,
  Logout,
} from "@mui/icons-material";
import { useTranslation } from "react-i18next";
import { useDispatch, useSelector } from "react-redux";
import { changeLevel, logout } from "../redux/UserSlice";
import { blue, pink, red, purple } from "@mui/material/colors";
import ConfirmationDialog from "./ConfirmationDialog";
import { useEffect, useState } from "react";
import { Pathnames } from "../router/Pathnames";
import { ROLES } from "../constants/Constants";
import ManageAccountsIcon from "@mui/icons-material/ManageAccounts";

const guestTheme = createTheme({
  palette: {
    primary: blue,
  },
  typography: {
    fontFamily: ["Lato", "sans-serif"].join(","),
  },
});

const adminTheme = createTheme({
  palette: {
    primary: red,
  },
  typography: {
    fontFamily: ["Lato", "sans-serif"].join(","),
  },
});

const chemistTheme = createTheme({
  palette: {
    primary: purple,
  },
  typography: {
    fontFamily: ["Lato", "sans-serif"].join(","),
  },
});

const patientTheme = createTheme({
  palette: {
    primary: pink,
  },
  typography: {
    fontFamily: ["Lato", "sans-serif"].join(","),
  },
});

export default function AuthNavbar() {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const userRole = useSelector((state) => state.user.cur);
  const user = useSelector((state) => state.user);
  const login = useSelector((state) => state.user.sub);
  const roles = useSelector((state) => state.user.roles);
  const dispatch = useDispatch();
  const [currentTheme, setCurrentTheme] = useState(guestTheme);
  const [anchorEl, setAnchorEl] = React.useState(null);

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = (event) => {
    event.stopPropagation();
    setAnchorEl(null);
  };

  useEffect(() => {
    if (userRole == ROLES.ADMIN) {
      setCurrentTheme(adminTheme);
    } else if (userRole === ROLES.CHEMIST) {
      setCurrentTheme(chemistTheme);
    } else if (userRole === ROLES.PATIENT) {
      setCurrentTheme(patientTheme);
    }
  }, [userRole]);

  const [dialogOpen, setDialogOpen] = useState(false);

  const accept = () => {
    dispatch(logout());
    navigate(Pathnames.public.login);
  };

  const reject = () => {
    setDialogOpen(false);
  };

  return (
    <ThemeProvider theme={currentTheme}>
      <Box sx={{ flexGrow: 1 }}>
        <AppBar position="static">
          <Toolbar>
            <Typography
              onClick={() => navigate(Pathnames.auth.landing)}
              variant="h6"
              component="div"
              sx={{ flexGrow: 1, cursor: "pointer" }}
            >
              {t("internet_pharmacy")}
            </Typography>
            {userRole === ROLES.ADMIN && (
              <Button
                color="inherit"
                onClick={() => navigate(Pathnames.admin.accounts)}
              >
                {t("accounts")}
              </Button>
            )}
            <Box>
              <Typography>{login}</Typography>
              <Typography>{userRole}</Typography>
            </Box>
            <IconButton color="inherit" onClick={handleClick}>
              <ManageAccountsIcon />
              <Menu
                id="simple-menu"
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleClose}
                MenuListProps={{
                  "aria-labelledby": "basic-button",
                }}
              >
                {roles.map((role, index) => (
                  <MenuItem
                    disabled={role === userRole}
                    key={role}
                    onClick={(event) => {
                      dispatch(
                        changeLevel({
                          sub: user.sub,
                          roles: user.roles,
                          index: index,
                          exp: user.exp,
                        })
                      );
                      handleClose(event);
                    }}
                  >
                    {role}
                  </MenuItem>
                ))}
              </Menu>
            </IconButton>
            <IconButton
              color="inherit"
              onClick={() => {
                navigate(Pathnames.auth.self);
              }}
            >
              <AccountCircle />
            </IconButton>
            <IconButton
              color="inherit"
              onClick={() => {
                setDialogOpen(true);
              }}
            >
              <Logout />
            </IconButton>
            <ConfirmationDialog
              open={dialogOpen}
              title={t("confirm_logout")}
              actions={[
                { label: t("logout"), handler: accept, color: "primary" },
                { label: t("cancel"), handler: reject, color: "secondary" },
              ]}
              onClose={() => setDialogOpen(false)}
            />
          </Toolbar>
        </AppBar>
      </Box>
    </ThemeProvider>
  );
}
