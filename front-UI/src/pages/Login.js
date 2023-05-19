import React, { useState } from "react";
import { Grid, Paper, TextField, Button, Box, Typography } from "@mui/material";
import { useTranslation } from "react-i18next";
import { signInAccount } from "../api/mok/accountApi";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

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

  const setAuthToken = (token) => {
    if (token) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    } else {
      delete axios.defaults.headers.common['Authorization'];
    }
  };
  

  async function handleSubmit(event) {
    event.preventDefault();
    const response = await signInAccount(username, password);
    console.log(response.data);
    const jwt = response.data.jwtToken;
    console.log(jwt);
    localStorage.setItem('jwtToken', jwt);
    setAuthToken(jwt);
    console.log(localStorage.getItem('jwtToken'));
    navigate('/accounts');
  }



  return (
    <Grid>
      <Paper elevation={20} sx={paperStyle}>
        <Box>
          <Typography sx={{ fontSize: 30, textAlign: "center" }}>
            {t("sign_in")} {t("sign_in_info")}
          </Typography>
          <Box
            sx={{ display: "flex", flexDirection: "column", width: "100%" }}
            //onSubmit={}
          >
            <form>
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
              <Button
                variant="contained"
                color="primary"
                type="submit"
                onClick={handleSubmit}
              >
                {t("sign_in")}
              </Button>
            </form>
          </Box>
        </Box>
      </Paper>
    </Grid>
  );
};

export default Login;
