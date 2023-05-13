import React, { useState } from "react";
import { TextField, Button, Box } from "@mui/material";
import "./Login.css";
import { useTranslation } from "react-i18next";
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';


const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [passwordShown, setPasswordShown] = useState(false);

  const { t } = useTranslation();

  const handleUsernameChange = (event) => {
    setUsername(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log(`Username: ${username}, Password: ${password}`);
  };

  return (
    <div className="sign-in">
        <div className="title">
            <div>{t("sign_in")} {t("sign_in_info")}</div>
        </div>
    <div className="form-container">
    <Box sx={{display: "flex", flexDirection: "column", width: "40%" }} onSubmit={handleSubmit}>
      <TextField sx={{ width: 9/10}}
        id="username"
        label={t("username")}
        value={username}
        onChange={handleUsernameChange}
      />
      <TextField
        id="password"
        label={t("password")}
        type="password"
        value={password}
        onChange={handlePasswordChange}
        InputProps={{endAdornment: <Button onClick={() => setPasswordShown(!passwordShown)}>{passwordShown ? <VisibilityOffIcon fontSize="small" sx={{color: 'black'}}/> : <VisibilityIcon fontSize="small" sx={{color: 'black'}}/>}</Button>}}
      />
      <Button variant="contained" color="primary" type="submit">
        {t("sign_in")}
      </Button>
    </Box>
    </div>
    </div>
  );
};

export default Login;