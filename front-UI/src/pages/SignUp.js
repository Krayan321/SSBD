import React, {useState} from 'react';
import {Grid, Paper, TextField, Button, Box} from '@mui/material';
import {useTranslation} from "react-i18next";
import {signUpAccount} from "../api/mok/accountApi";
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import * as Yup from "yup";
import {yupResolver} from "@hookform/resolvers/yup";
import {useForm} from "react-hook-form";
import Typography from '@mui/material/Typography';
import {Container, Stack} from '@mui/material';


const signUpSchema = Yup.object().shape({
    name: Yup.string()
        .min(2, 'Too Short!')
        .max(50, 'Too Long!')
        .required('Name is required'),
    lastName: Yup.string()
        .min(2, 'Too Short!')
        .max(50, 'Too Long!')
        .required('Last name is required'),
    login: Yup.string()
        .min(2, 'Too Short!')
        .max(50, 'Too Long!')
        .required('Login is required'),
    email: Yup.string()
        .email('Invalid email')
        .required('Email is required'),
    password: Yup.string()
        .min(8, 'Too Short!')
        .max(50, 'Too Long!')
        .matches(
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/,
            "Must Contain 8 Characters, One Uppercase, One Lowercase and One Number"
        )
        .required('Password is required'),
    phoneNumber: Yup.string()
        .min(9, 'Too Short!')
        .max(9, 'Too Long!')
        .matches(/^[0-9]+$/, "Must be only digits")
        .required('Phone number is required'),
    pesel: Yup.string()
        .min(11, 'Too Short!')
        .max(11, 'Too Long!')
        .matches(/^[0-9]+$/, "Must be only digits")
        .required('Pesel is required'),
    nip: Yup.string()
        .min(10, 'Too Short!')
        .matches(/^[0-9]+$/, "Must be only digits")
        .max(10, 'Too Long!')
        .required('Nip is required'),
});

function SignUp() {

    const {
        register,
        handleSubmit,
        formState: {errors},
    } = useForm({
        resolver: yupResolver(signUpSchema),
    });

    const onSubmit = handleSubmit(({name, lastName, login, email, password, phoneNumber, pesel, nip}) => {
        signUpAccount(name, lastName, login, email, password, phoneNumber, pesel, nip)
    })

    const paperStyle = {padding: '30px 20px', margin: "0px auto", width: 400}
    const headerStyle = {margin: 0}
    const [passwordShown, setPasswordShown] = useState(false);
    const {t} = useTranslation();

    return (
        // <Grid container spacing={2}>
        <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignContent: 'center',
            maxHeight: '100vh',
            marginTop: '3rem'
        }}>
            <Paper elevation={20} style={paperStyle}>
                <h2 style={{fontFamily: 'Lato'}}>
                    {t("sign_up")} </h2>
                <form>
                    <Stack spacing={2} direction="row" sx={{marginBottom: 4}}>
                        <TextField
                            {...register("name")}
                            type="text"
                            variant='outlined'
                            color='secondary'
                            label="First Name"
                            fullWidth
                            required
                            error={errors.name}
                            helperText={errors.name?.message}
                        />
                        <TextField
                            type="text"
                            variant='outlined'
                            color='secondary'
                            label="Last Name"
                            fullWidth
                            required
                            error={errors.lastName}
                            helperText={errors.lastName?.message}
                            {...register("lastName")}
                        />
                    </Stack>
                    <TextField
                        type="email"
                        variant='outlined'
                        color='secondary'
                        label="Email"
                        fullWidth
                        required
                        sx={{mb: 4}}
                        error={errors.email}
                        helperText={errors.email?.message}
                        {...register("email")}
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
                    <TextField
                        type="text"
                        variant='outlined'
                        color='secondary'
                        label="Phone Number"
                        required
                        fullWidth
                        error={errors.phoneNumber}
                        helperText={errors.phoneNumber?.message}
                        sx={{mb: 4}}
                        {...register("PhoneNumber")}
                    />
                    <TextField
                        type="text"
                        variant='outlined'
                        color='secondary'
                        label="Pesel"
                        required
                        fullWidth
                        error={errors.pesel}
                        helperText={errors.pesel?.message}
                        sx={{mb: 4}}
                        {...register("pesel")}
                    />
                    <TextField
                        type="text"
                        variant='outlined'
                        color='secondary'
                        label="Nip"
                        required
                        fullWidth
                        error={errors.nip}
                        helperText={errors.nip?.message}
                        sx={{mb: 4}}
                        {...register("nip")}
                    />
                    <Button fullWidth
                            onClick={onSubmit} type='submit' variant='contained' color='secondary'>Sign up</Button>
                </form>
            </Paper>
        </div>
    )
}

export default SignUp;