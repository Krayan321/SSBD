import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import React, {useState} from "react";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import {addChemist} from "../api/mok/accountApi";
import {toast, ToastContainer} from "react-toastify";
import {Pathnames} from "../router/Pathnames";
import {Button, CircularProgress, Grid, Paper, TextField} from "@mui/material";
import List from "@mui/icons-material/List";
import VisibilityIcon from "@mui/icons-material/Visibility";
import * as Yup from "yup";
import {Add} from "@mui/icons-material";
import SelectMedicationOverlay from "../modules/overlays/SelectMedicationOverlay";
import AddMedicationOverlay from "../modules/overlays/AddMedicationOverlay";

const addChemistSchema = Yup.object().shape({
    login: Yup.string()
        .min(5, 'login_length_min')
        .max(50, 'login_length_max')
        .required('login_required'),
    email: Yup.string()
        .email('email_valid')
        .required('email_required'),
    password: Yup.string()
        .min(8, 'password_length_min')
        .max(50, 'password_length_max')
        .matches(
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/,
            "password_invalid"
        )
        .required('Password is required'),
    confirmPassword: Yup.string()
        .oneOf([Yup.ref('password'), null], 'passwords_not_match')
        .required('confirm_password_required'),
    licenseNumber: Yup.string()
        .matches(/^[0-9]{6}$/, "license_invalid")
        .required('license_required')
});

export function Shipment() {

    const {
        register,
        handleSubmit,
        formState: {errors},
    } = useForm({
        resolver: yupResolver(addChemistSchema),
    });

    const paperStyle = {padding: '20px 20px', margin: "0px auto", width: "80%"}
    const {t} = useTranslation();
    const [loading, setLoading] = useState(false);
    const [selectMedication, setSelectMedication] = useState(false);
    const [createMedication, setCreateMedication] = useState(false);

    const onSubmit = function() {

    }

    return (
        <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignContent: 'center',
            marginTop: '2rem'
        }}>

            <Paper elevation={10} style={paperStyle}>
                <h2 style={{fontFamily: 'Lato'}}>{t("shipment")}</h2>
                <form>
                    <Grid container spacing={1}>
                        <Grid item xs={6}>
                            <Button fullWidth variant="outlined" color="success"
                                    onClick={() => setSelectMedication(true)}>
                                <List/>{t("select_medication")}</Button>
                        </Grid>
                        <Grid item xs={6}>
                            <Button fullWidth variant="outlined" color="secondary"
                                    onClick={() => setCreateMedication(true)}>
                                <Add/>{t("create_medication")}</Button>
                        </Grid>
                    </Grid>

                    {
                        loading ? <CircularProgress style={{marginRight: "auto", marginLeft: "auto"}}/> :
                            <Button fullWidth
                                    onClick={onSubmit} type='submit' variant='contained'>{t("submit")}</Button>
                    }
                </form>

            </Paper>
            <SelectMedicationOverlay open={selectMedication} onClose={() => setSelectMedication(false)}/>
            <AddMedicationOverlay open={createMedication} onClose={() => setCreateMedication(false)}/>
            <ToastContainer/>
        </div>
    )
}