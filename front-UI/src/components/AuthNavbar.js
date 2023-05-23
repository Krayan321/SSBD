import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import {createTheme, ThemeProvider} from '@mui/material/styles';
import {useNavigate} from 'react-router-dom';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import i18n from "i18next";
import LanguageIcon from '@mui/icons-material/Language';
import {AccountCircle, Logout} from "@mui/icons-material";
import {useTranslation} from "react-i18next";
import {useDispatch, useSelector} from "react-redux";
import {logout} from "../redux/UserSlice";
import {blue, pink, red, purple} from "@mui/material/colors";
import ConfirmationDialog from "./ConfirmationDialog";
import {useEffect, useState} from "react";
import {Pathnames} from "../router/Pathnames";
import {ROLES} from "../constants/Constants";

const guestTheme = createTheme({
    palette: {
        primary: blue,
    },
    typography: {
        fontFamily: [
            'Lato',
            'sans-serif',
        ].join(','),
    },
});

const adminTheme = createTheme({
    palette: {
        primary: red,
    },
    typography: {
        fontFamily: [
            'Lato',
            'sans-serif',
        ].join(','),
    },
});

const chemistTheme = createTheme({
    palette: {
        primary: purple
    },
    typography: {
        fontFamily: [
            'Lato',
            'sans-serif',
        ].join(','),
    },
});

const patientTheme = createTheme({
    palette: {
        primary: pink,

    },
    typography: {
        fontFamily: [
            'Lato',
            'sans-serif',
        ].join(','),
    },
});

export default function AuthNavbar() {
    const navigate = useNavigate();
    const {t} = useTranslation();
    const userRole = useSelector(state => state.user.cur)
    const dispatch = useDispatch();
    const [currentTheme, setCurrentTheme] = useState(guestTheme);

    useEffect(() => {

        if (userRole == ROLES.ADMIN) {
            setCurrentTheme(adminTheme)
        } else if (userRole === ROLES.CHEMIST) {
            setCurrentTheme(chemistTheme)
        } else if (userRole === ROLES.PATIENT) {
            setCurrentTheme(patientTheme)
        }
    }, [])

    const [dialogOpen, setDialogOpen] = useState(false);

    const accept = () => {
        dispatch(logout())
        navigate(Pathnames.public.login)
    }

    const reject = () => {
        setDialogOpen(false)
    }

    return (
        <ThemeProvider theme={
            currentTheme
        }>
            <Box sx={{flexGrow: 1}}>
                <AppBar position="static">
                    <Toolbar>
                        <Typography onClick={() => navigate(Pathnames.auth.landing)} variant="h6" component="div"
                                    sx={{flexGrow: 1, cursor: 'pointer'}}>
                            {t('internet_pharmacy')}
                        </Typography>
                        <IconButton color="inherit" onClick={() => {
                            navigate(Pathnames.auth.self)
                        }}>
                            <AccountCircle/>
                        </IconButton>
                        <IconButton color="inherit" onClick={() => {
                            setDialogOpen(true)
                        }}>
                            <Logout/>
                        </IconButton>
                        <ConfirmationDialog
                            open={dialogOpen}
                            title={t("confirm_logout")}
                            actions={[
                                {label: t("logout"), handler: accept, color: 'primary'},
                                {label: t("cancel"), handler: reject, color: 'secondary'},
                            ]}
                            onClose={() => setDialogOpen(false)}
                        />
                    </Toolbar>
                </AppBar>
            </Box>
        </ThemeProvider>
    );
}
