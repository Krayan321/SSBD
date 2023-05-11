import React, { useState } from "react";
import { TextField, Button, Box } from "@mui/material";
import "./Login.css";
import { useTranslation } from "react-i18next";


const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

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
      <TextField sx={{ width: 300}}
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