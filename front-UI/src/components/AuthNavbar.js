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
import {useDispatch} from "react-redux";
import {logout} from "../redux/UserSlice";
import {blue} from "@mui/material/colors";

const guestTheme = createTheme({
    palette: {
        primary: blue,
        //admin: blue,
        //pharmacist: green,
        //patient: yellow,
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
    const dispatch = useDispatch();


    return (
        <ThemeProvider theme={guestTheme}>
            <Box sx={{flexGrow: 1}}>
                <AppBar position="static">
                    <Toolbar>
                        <Typography onClick={() => navigate("/landing")} variant="h6" component="div"
                                    sx={{flexGrow: 1, cursor: 'pointer'}}>
                            {t('internet_pharmacy')}
                        </Typography>
                        <IconButton color="inherit" onClick={() => {
                            navigate("/accounts/self")
                        }}>
                            <AccountCircle/>
                        </IconButton>
                        <IconButton color="inherit" onClick={() => {
                            dispatch(logout())
                            navigate("/login")
                        }}>
                            <Logout/>
                        </IconButton>

                    </Toolbar>
                </AppBar>
            </Box>
        </ThemeProvider>
    );
}
