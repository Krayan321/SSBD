import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import {createTheme, ThemeProvider} from "@mui/material/styles";
import {useNavigate} from "react-router-dom";
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
import {useTranslation} from "react-i18next";
import {useDispatch, useSelector} from "react-redux";
import {changeLevel, logout} from "../redux/UserSlice";
import {blue, pink, red, purple} from "@mui/material/colors";
import ConfirmationDialog from "./ConfirmationDialog";
import {useEffect, useState} from "react";
import {Pathnames} from "../router/Pathnames";
import {ROLES} from "../constants/Constants";
import ManageAccountsIcon from "@mui/icons-material/ManageAccounts";
import {useLocation} from "react-router-dom";
import Breadcrumbs from "@mui/material/Breadcrumbs";
import Link from "@mui/material/Link";
import {languages} from "../constants/Constants";
import {changeLanguage} from "../api/mok/accountApi";


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
    const {t} = useTranslation();
    const userRole = useSelector((state) => state.user.cur);
    const currentLanguage = i18n.language;
    const user = useSelector((state) => state.user);
    const login = useSelector((state) => state.user.sub);
    const roles = useSelector((state) => state.user.roles);
    const dispatch = useDispatch();
    const [currentTheme, setCurrentTheme] = useState(guestTheme);
    const [anchorElRole, setAnchorElRole] = React.useState(null);
    const [anchorElLanguage, setAnchorElLanguage] = React.useState(null);

    const handleClick = (event) => {
        setAnchorElRole(event.currentTarget);
    };

    const handleClose = (event) => {
        event.stopPropagation();
        setAnchorElRole(null);
    };

    useEffect(() => {
        if (userRole === ROLES.ADMIN) {
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

    const location = useLocation();

    let current = "";

    return (
        <ThemeProvider theme={currentTheme}>
            <Box sx={{flexGrow: 1}}>
                <AppBar position="static">
                    <Toolbar>
                        <Typography
                            onClick={() => navigate(Pathnames.auth.landing)}
                            variant="h6"
                            component="div"
                            sx={{flexGrow: 1, cursor: "pointer"}}
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
                        <IconButton color="inherit" onClick={(e) => {
                            setAnchorElRole(e.currentTarget);
                        }}>
                            <ManageAccountsIcon/>
                            <Menu
                                id="simple-menu"
                                anchorEl={anchorElRole}
                                open={Boolean(anchorElRole)}
                                onClose={(e) => {
                                    e.stopPropagation();
                                    setAnchorElRole(null);
                                }}
                                MenuListProps={{
                                    "aria-labelledby": "basic-button",
                                }}>
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
                                            navigate(Pathnames.auth.landing);
                                        }}>
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
                            <AccountCircle/>
                        </IconButton>
                        <IconButton
                            size="large"
                            edge="start"
                            color="inherit"
                            aria-label="menu"
                            // sx={{mr: 2}}
                            id="basic-button"
                            aria-controls={Boolean(anchorElRole) ? 'basic-menu' : undefined}
                            aria-haspopup="true"
                            aria-expanded={Boolean(anchorElRole) ? 'true' : undefined}
                            onClick={(e) => {
                                setAnchorElLanguage(e.currentTarget)
                            }}
                        >
                            <LanguageIcon/>
                            <Menu
                                id="basic-menu"
                                anchorEl={anchorElLanguage}
                                open={Boolean(anchorElLanguage)}
                                onClose={(e) => {
                                    e.stopPropagation();
                                    setAnchorElLanguage(null);
                                }}
                                MenuListProps={{
                                    'aria-labelledby': 'basic-button',
                                }}
                            >
                                {languages.map(({code, name, country_code}) => (
                                    <MenuItem disabled={code === currentLanguage} key={country_code} onClick={() => {
                                        changeLanguage(code)
                                        i18n.changeLanguage(code)
                                        setAnchorElLanguage(null);
                                    }}>
                                        {name}
                                    </MenuItem>
                                ))}
                            </Menu>
                        </IconButton>
                        <IconButton
                            color="inherit"
                            onClick={() => {
                                setDialogOpen(true);
                            }}
                        >
                            <Logout/>
                        </IconButton>
                        <ConfirmationDialog
                            open={dialogOpen}
                            title={t("confirm_logout")}
                            actions={[
                                {label: t("logout"), handler: accept, color: "primary"},
                                {label: t("cancel"), handler: reject, color: "secondary"},
                            ]}
                            onClose={() => setDialogOpen(false)}
                        />
                    </Toolbar>
                </AppBar>
                <Breadcrumbs separator=">" aria-label="breadcrumb">
                    {location.pathname
                        .split("/")
                        .filter((crumb) => crumb !== "")
                        .map((crumb) => {
                            if (!isNaN(crumb)) {
                                return null; // Ignore numbers in the URL
                            }
                            current += `/${crumb}`;
                            const capitalizedCrumb =
                                crumb.charAt(0).toUpperCase() + crumb.slice(1);
                            return (
                                <Link
                                    underline="hover"
                                    color="inherit"
                                    href={current}
                                    sx={{fontSize: "18px"}}
                                >
                                    {t(capitalizedCrumb)}
                                </Link>
                            );
                        })}
                </Breadcrumbs>
            </Box>
        </ThemeProvider>
    );
}
