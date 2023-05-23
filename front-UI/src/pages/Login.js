import React, {useState} from "react";
import {Paper, CircularProgress, Link, Dialog, DialogTitle, DialogActions} from "@mui/material";
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import * as Yup from "yup";
import {yupResolver} from "@hookform/resolvers/yup";
import {useForm} from "react-hook-form";
import {Grid, TextField, Button, Box, Typography} from "@mui/material";
import {useTranslation} from "react-i18next";
import {signInAccount} from "../api/mok/accountApi";
import axios from "axios";
import {changeLevel, login as loginDispatch} from "../redux/UserSlice";
import {useNavigate} from "react-router-dom";
import 'react-toastify/dist/ReactToastify.css';
import {ToastContainer, toast} from 'react-toastify';
import jwtDecode from "jwt-decode";
import {useDispatch, useSelector} from "react-redux";
import {Pathnames} from "../router/Pathnames";


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

    const dispatch = useDispatch();
    const JWT_TOKEN = "jwtToken";

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
    const [dialogOpen, setDialogOpen] = useState(false);
    const userRoles = useSelector((state) => state.user.roles);
    const user = useSelector((state) => state.user);
    const [loading, setLoading] = useState(false)
    const onSubmit = async ({login, password}) => {

        setLoading(true)

        signInAccount(login, password).then((response) => {
            setLoading(false)
            const jwt = response.data.jwtToken;
            localStorage.setItem(JWT_TOKEN, jwt);
            const decodedJWT = jwtDecode(jwt);
            dispatch(loginDispatch(decodedJWT));
            const roles = decodedJWT.roles;

            if (roles.length > 0) {
                setDialogOpen(true)
            }
        }).catch((error) => {
            if (error.response.status === 403) {
                toast.error(t("activate_account_to_login"), {
                    position: toast.POSITION.TOP_CENTER,
                });
                setLoading(false)

            } else if (error.response.status === 401) {
                toast.error(t("invalid_login_or_password"), {
                    position: toast.POSITION.TOP_CENTER,
                });
                setLoading(false)
            } else {
                toast.error(t("server_error"), {
                    position: toast.POSITION.TOP_CENTER,
                });
                setLoading(false)
            }
        })
    }
    const onResetPassword = async () => {
        navigate(Pathnames.public.resetPassword);
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
                    {
                        loading ? <CircularProgress/> :
                            <Button fullWidth sx={{mb: 2}}
                                    onClick={handleSubmit(onSubmit)} type='submit'
                                    variant='contained'>{t("sign_in")}</Button>
                    }
                </form>
                <Button onClick={onResetPassword}>
                    {t("to_reset_password")}
                </Button>
            </Paper>
            <ToastContainer/>
            <Dialog open={dialogOpen} onClose={() => {
                setDialogOpen(false)
            }}>
                <DialogTitle>{t("choose_role")}</DialogTitle>
                <DialogActions>
                    {
                        userRoles.map((role, index) => {
                            return <Button onClick={() => {
                                dispatch(changeLevel({
                                    sub: user.sub,
                                    roles: user.roles,
                                    index: index,
                                    exp: user.exp,
                                }));
                                navigate('/landing');
                                setDialogOpen(false)
                            }}>{role}</Button>
                        })
                    }
                </DialogActions>
            </Dialog>
        </div>

    );
};

export default Login;