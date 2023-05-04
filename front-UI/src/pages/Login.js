import React, { useState } from "react";
import { TextField, Button, Box } from "@mui/material";
import "./Login.css";


const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

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
            <div>Sign In to Internet Pharmacy</div>
        </div>
    <div className="form-container">
    <Box sx={{display: "flex", flexDirection: "column", width: "40%" }} onSubmit={handleSubmit}>
      <TextField
        id="username"
        label="Username"
        value={username}
        onChange={handleUsernameChange}
      />
      <TextField
        id="password"
        label="Password"
        type="password"
        value={password}
        onChange={handlePasswordChange}
      />
      <Button variant="contained" color="primary" type="submit">
        Login
      </Button>
    </Box>
    </div>
    </div>
  );
};

export default Login;