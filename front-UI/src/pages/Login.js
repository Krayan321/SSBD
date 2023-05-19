import React, {useState} from "react";
import {TextField, Button, Box, Paper} from "@mui/material";
import "./Login.css";
import {useTranslation} from "react-i18next";
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import {Container, Stack} from '@mui/material';
import * as Yup from "yup";
import {yupResolver} from "@hookform/resolvers/yup";
import {useForm} from "react-hook-form";


const logInSchema = Yup.object().shape({
    login: Yup.string()
        .min(2, 'Too Short!')
        .max(50, 'Too Long!')
        .required('Login is required'),
    password: Yup.string()
        .min(8, 'Too Short!')
        .max(50, 'Too Long!')
        .required('Password is required'),
});
const Login = () => {

    const {
        register,
        handleSubmit,
        formState: {errors},
    } = useForm({
        resolver: yupResolver(logInSchema),
    });

    const [passwordShown, setPasswordShown] = useState(false);
    const {t} = useTranslation();
    const paperStyle = {padding: '30px 20px', margin: "auto", width: 400}

    const onSubmit = handleSubmit(({login, password}) => {
        //todo
        console.log(`Username: ${login}, Password: ${password}`);
    });

    return (

        <div style={{display: 'flex', justifyContent: 'center', alignContent: 'center', marginTop: '3rem' }}>
            <Paper elevation={20} style={paperStyle}>
                <h2 style={{fontFamily: 'Lato'}}>
                    {t("sign_in")} </h2>
                <form>
                    <TextField
                        type="text"
                        variant='outlined'
                        color='secondary'
                        label="Login"
                        fullWidth
                        required
                        sx={{mb: 4}}
                        error={errors.login}
                        helperText={errors.login?.message}
                        {...register("login")}
                    />
                    <TextField
                        type={passwordShown ? "text" : "password"}
                        variant='outlined'
                        color='secondary'
                        label="Password"
                        required
                        fullWidth
                        error={errors.password}
                        helperText={errors.password?.message}
                        sx={{mb: 4}}
                        {...register("password")}
                        InputProps={
                            {
                                endAdornment: <Button onClick={() => setPasswordShown(!passwordShown)}>{passwordShown ?
                                    <VisibilityOffIcon fontSize="small" sx={{color: 'black'}}/> :
                                    <VisibilityIcon fontSize="small" sx={{color: 'black'}}/>}</Button>
                            }
                        }
                    />
                    <Button fullWidth
                        onClick={onSubmit} type='submit' variant='contained' color='secondary'>Sign up</Button>
                </form>
            </Paper>
        </div>

        // <div className="sign-in">
        //     <div className="title">
        //         <div>{t("sign_in")} {t("sign_in_info")}</div>
        //     </div>
        // <div className="form-container">
        // <Box sx={{display: "flex", flexDirection: "column", width: "40%" }} onSubmit={handleSubmit}>
        //   <TextField sx={{ width: 9/10}}
        //     id="username"
        //     label={t("username")}
        //     value={username}
        //     onChange={handleUsernameChange}
        //   />
        //   <TextField
        //     id="password"
        //     label={t("password")}
        //     type="password"
        //     value={password}
        //     onChange={handlePasswordChange}
        //     InputProps={{endAdornment: <Button onClick={() => setPasswordShown(!passwordShown)}>{passwordShown ? <VisibilityOffIcon fontSize="small" sx={{color: 'black'}}/> : <VisibilityIcon fontSize="small" sx={{color: 'black'}}/>}</Button>}}
        //   />
        //   <Button variant="contained" color="primary" type="submit">
        //     {t("sign_in")}
        //   </Button>
        // </Box>
        // </div>
        // </div>
    );
};

export default Login;