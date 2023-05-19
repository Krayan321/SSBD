import React, {useState} from "react";
import { Paper} from "@mui/material";
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import * as Yup from "yup";
import {yupResolver} from "@hookform/resolvers/yup";
import {useForm} from "react-hook-form";
import {Grid, TextField, Button, Box, Typography} from "@mui/material";
import {useTranslation} from "react-i18next";
import {signInAccount} from "../api/mok/accountApi";
import axios from "axios";
import {useNavigate} from "react-router-dom";

const logInSchema = Yup.object().shape({
    login: Yup.string()
        .min(2, 'login_length_min')
        .max(50, 'login_length_max')
        .required('login_required'),
    password: Yup.string()
        .min(8, 'password_length_min')
        .max(50, 'password_length_max')
        .matches(
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/,
            "password_invalid"
        )
        .required('password_required'),
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
    const navigate = useNavigate();


    const setAuthToken = (token) => {
        if (token) {
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        } else {
            delete axios.defaults.headers.common['Authorization'];
        }
    };

    const onSubmit = async ({login, password}) => {
        const response = await signInAccount(login, password);
        const jwt = response.data.jwtToken;
        localStorage.setItem('jwtToken', jwt);
        setAuthToken(jwt);
        navigate('/accounts');
    }


    return (

        <div style={{display: 'flex', justifyContent: 'center', alignContent: 'center', marginTop: '3rem'}}>
            <Paper elevation={20} style={paperStyle}>
                <h2 style={{fontFamily: 'Lato'}}>
                    {t("sign_in")} </h2>
                <form>
                    <TextField
                        type="text"
                        variant='outlined'
                        color='secondary'
                        label={t("login")}
                        fullWidth
                        required
                        sx={{mb: 4}}
                        error={errors.login}
                        helperText={t(errors.login?.message)}
                        {...register("login")}
                    />
                    <TextField
                        type={passwordShown ? "text" : "password"}
                        variant='outlined'
                        color='secondary'
                        label={t("password")}
                        required
                        fullWidth
                        error={errors.password}
                        helperText={t(errors.password?.message)}
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
                            onClick={handleSubmit(onSubmit)} type='submit' variant='contained'>{t("sign_up")}</Button>
                </form>
            </Paper>
        </div>

    );
};

export default Login;