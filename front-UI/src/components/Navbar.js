import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import {red, blue, green, yellow, purple, lightBlue, cyan} from '@mui/material/colors';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from "react-i18next";
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import i18n from "i18next";
import LanguageIcon from '@mui/icons-material/Language';

const guestTheme = createTheme({
  palette: {
    primary: purple,
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

const languages = [
  {
    code: 'en',
    name: 'English',
    country_code: 'gb',
  },
  {
    code: 'pl',
    name: 'Polski',
    country_code: 'pl'
  },
  {
    code: 'cs',
    name: 'Český',
    country_code: 'cs'
  }
]

export default function Navbar() {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);
  const currentLanguage = i18n.language;
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = (event) => {
    event.stopPropagation();
    setAnchorEl(null);
  };

  return (
    <ThemeProvider theme={guestTheme}>
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <Typography onClick={()=>navigate("/")}variant="h6" component="div" sx={{ flexGrow: 1, cursor: 'pointer' }}>
            {t('internet_pharmacy')}
          </Typography>
          <Button color="inherit" onClick={() => navigate("/sign-up")}>
            {t('sign_up')}
          </Button>
          <Button color="inherit" onClick={() => navigate("/login")}>
            Login
          </Button>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
            id="basic-button"
            aria-controls={open ? 'basic-menu' : undefined}
            aria-haspopup="true"
            aria-expanded={open ? 'true' : undefined}
            onClick={handleClick}
          >
          <LanguageIcon />
          <Menu 
            id="basic-menu"
            anchorEl={anchorEl}
            open={open}
            onClose={handleClose}
            MenuListProps={{
              'aria-labelledby': 'basic-button',
            }}
          >
            {languages.map(({ code, name, country_code }) => (
              <MenuItem disabled={code === currentLanguage} key={country_code} onClick={() => {
                  i18n.changeLanguage(code)
                  setAnchorEl(null);}}>
                {name}
              </MenuItem>
            ))}
          </Menu>
          </IconButton>
        </Toolbar>
      </AppBar>
    </Box>
    </ThemeProvider>
  );
}
