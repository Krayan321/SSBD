import React, { useState } from 'react';
import { Grid, Paper, TextField, Button , Box} from '@mui/material';
import { useTranslation } from "react-i18next";

function SignUp() {
    const paperStyle = { padding: '30px 20px', width: 300, margin: "20px auto" }
    const headerStyle = { margin: 0 }
    const [name, setName] = useState("");
    const [lastName, setLastName] = useState("");
    const [login, setLogin] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [pesel, setPesel] = useState("");
    const [nip, setNip] = useState("");

    const { t } = useTranslation();
    const handleSubmit = (event) => {
        event.preventDefault();
      };

    return (
        <Grid>
            <Paper elevation={20} sx={paperStyle}>
                <h2 style={headerStyle}>{t("sign_up")} </h2>
                <Box sx={{display: "flex", flexDirection: "column", width: "60%" }} onSubmit={handleSubmit}>
                    <TextField sx={{ width: 300}}
                        fullWidth 
                        label={t("name")} 
                        //placeholder="Enter your name"
                        onChange={(event) => setName(event.target.value)}
                        />
                    <TextField sx={{ width: 300}}
                        fullWidth 
                        label={t("last_name")}
                        //placeholder="Enter your last name" 
                        onChange={(event) => setLastName(event.target.value)}
                        />
                    <TextField sx={{ width: 300}}
                        fullWidth 
                        label='Login' 
                        //placeholder="Enter your login" 
                        onChange={(event) => setLogin(event.target.value)}
                        />
                    <TextField sx={{ width: 300}}
                        fullWidth 
                        label='Email' 
                        //placeholder="Enter your email" 
                        onChange={(event) => setEmail(event.target.value)}
                        />
                    <TextField sx={{ width: 300}}
                        fullWidth 
                        label={t("password")}
                        //placeholder="Enter your password"
                        onChange={(event) => setPassword(event.target.value)}
                        />
                    <TextField sx={{ width: 300}}
                        fullWidth 
                        label={t("phone_number")} 
                        //placeholder="Enter your phone number" 
                        onChange={(event) => setPhoneNumber(event.target.value)}
                        />
                    <TextField sx={{ width: 300}}
                        fullWidth 
                        label='Pesel' 
                        //placeholder="Enter your pesel number"
                        onChange={(event) => setPesel(event.target.value)}
                        />
                    <TextField sx={{ width: 300}}
                        fullWidth 
                        label='NIP' 
                        //placeholder="Enter your NIP number"
                        onChange={(event) => setNip(event.target.value)}
                        />
                    <Button type='submit' variant='contained' color='primary'>Sign up</Button>
                </Box>
            </Paper>
        </Grid>
    )
}

export default SignUp;