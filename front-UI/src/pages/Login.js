import React, { useState } from "react";
import { Grid, Paper, TextField, Button, Box, Typography } from "@mui/material";
import { useTranslation } from "react-i18next";

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const { t } = useTranslation();

  const paperStyle = {
    padding: "30px 20px",
    width: 300,
    margin: "20px auto",
    justifyContent: "center",
  };

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
    <Grid>
      <Paper elevation={20} sx={paperStyle}>
        <Box>
          <Typography sx={{ fontSize: 30, textAlign: "center" }}>
            {t("sign_in")} {t("sign_in_info")}
          </Typography>
          <Box
            sx={{ display: "flex", flexDirection: "column", width: "100%" }}
            onSubmit={handleSubmit}
          >
            <TextField
              fullwidth
              margin="normal"
              id="username"
              label={t("username")}
              value={username}
              onChange={handleUsernameChange}
            />
            <TextField
              margin="normal"
              fullwidth
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
        </Box>
      </Paper>
    </Grid>
  );
};

export default Login;
