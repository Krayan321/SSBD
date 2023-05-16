import React, { useState } from "react";
import { Grid, Paper, TextField, Button, Box, Typography } from "@mui/material";
import { useTranslation } from "react-i18next";
import { signUpAccount } from "../api/mok/accountApi";
import VisibilityIcon from "@mui/icons-material/Visibility";
import VisibilityOffIcon from "@mui/icons-material/VisibilityOff";

function SignUp() {
  const paperStyle = {
    padding: "30px 20px",
    width: 300,
    margin: "20px auto",
    justifyContent: "center",
  };
  const [name, setName] = useState("");
  const [lastName, setLastName] = useState("");
  const [login, setLogin] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [pesel, setPesel] = useState("");
  const [nip, setNip] = useState("");

  const [passwordShown, setPasswordShown] = useState(false);

  const { t } = useTranslation();
  const handleSubmit = (event) => {
    signUpAccount(
      name,
      lastName,
      login,
      email,
      password,
      phoneNumber,
      pesel,
      nip
    );
    event.preventDefault();
  };

  return (
    <Grid>
      <Paper elevation={20} sx={paperStyle}>
        <Typography sx={{ fontSize: 40, textAlign: "center" }}>
          {t("sign_up")}{" "}
        </Typography>
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            width: "100%",
          }}
        >
          <TextField
            margin="normal"
            fullWidth
            label={t("name")}
            //placeholder="Enter your name"
            onChange={(event) => setName(event.target.value)}
          />
          <TextField
            margin="normal"
            fullWidth
            label={t("last_name")}
            //placeholder="Enter your last name"
            onChange={(event) => setLastName(event.target.value)}
          />
          <TextField
            margin="normal"
            fullWidth
            label="Login"
            //placeholder="Enter your login"
            onChange={(event) => setLogin(event.target.value)}
          />
          <TextField
            margin="normal"
            fullWidth
            label="Email"
            //placeholder="Enter your email"
            onChange={(event) => setEmail(event.target.value)}
          />
          <TextField
            margin="normal"
            fullWidth
            type={passwordShown ? "text" : "password"}
            label={t("password")}
            //placeholder="Enter your password"
            onChange={(event) => setPassword(event.target.value)}
            InputProps={{
              endAdornment: (
                <Button onClick={() => setPasswordShown(!passwordShown)}>
                  {passwordShown ? (
                    <VisibilityOffIcon
                      fontSize="small"
                      sx={{ color: "black" }}
                    />
                  ) : (
                    <VisibilityIcon fontSize="small" sx={{ color: "black" }} />
                  )}
                </Button>
              ),
            }}
          />

          <TextField
            margin="normal"
            fullWidth
            label={t("phone_number")}
            //placeholder="Enter your phone number"
            onChange={(event) => setPhoneNumber(event.target.value)}
          />
          <TextField
            margin="normal"
            fullWidth
            label="Pesel"
            //placeholder="Enter your pesel number"
            onChange={(event) => setPesel(event.target.value)}
          />
          <TextField
            margin="normal"
            fullWidth
            label="NIP"
            //placeholder="Enter your NIP number"
            onChange={(event) => setNip(event.target.value)}
          />
          <Button
            onClick={handleSubmit}
            type="submit"
            variant="contained"
            color="primary"
          >
            Sign up
          </Button>
        </Box>
      </Paper>
    </Grid>
  );
}

export default SignUp;
