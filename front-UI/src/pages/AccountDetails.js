import { Box, Button, Grid, Paper, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { ToastContainer } from 'react-toastify';
import { getSelfAccountDetails } from "../api/mok/accountApi";
import ChangePasswordForm from "../modules/accounts/ChangePasswordForm";
function AccountDetails() {
    const [account, setAccount] = useState({});
    const [accessLevels, setAccessLevels] = useState([]);
    const [etag, setEtag] = useState("")
    const { t } = useTranslation();

    const paperStyle = {
        backgroundColor: "rgba(255, 255, 255, 0.75)",
        padding: "20px 20px",
        margin: "0px auto",
        width: 400,
    };

    const [loading, setLoading] = useState(true);
    const [changePass, setChangePass] = useState(false)
    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getSelfAccountDetails();
                setAccount(response.data);
                setAccessLevels(response.data.accessLevels[0].role);
                setEtag(response.headers["etag"])
               
                setLoading(false);
            } catch (error) {
                console.error(error);
            }
        };
        fetchData();
    }, []);

    const isAdmin = accessLevels.includes("ADMIN");
    const isChemist = accessLevels.includes("CHEMIST");
    const isPatient = accessLevels.includes("PATIENT");

    console.log(account);
    console.log("isAdmin: " + isAdmin);
    console.log("isChemist: " + isChemist);
    console.log("isPatient: " + isPatient);

    if (loading) {
        return <p>Loading...</p>;
    }

    const DataDisplay = ({ label, data }) => {
        return (
            <Grid container spacing={2}>
                <Grid item xs={6}>
                    <Typography variant="subtitle1" color="textSecondary">
                        {label}
                    </Typography>
                </Grid>
                <Grid item xs={6}>
                    <Typography variant="subtitle1">{data}</Typography>
                </Grid>
            </Grid>
        );
    };
    const handleChangePassword = () =>{
       setChangePass((state) => !state)
    }
    return (
        <div
            style={{
                display: "flex",
                justifyContent: "center",
                alignContent: "center",
                marginTop: "3rem",
            }}
        >
            <Paper elevation={20} style={paperStyle}>
                <h2 style={{ fontFamily: "Lato" }}>{t("account_details")} </h2>
                <Typography
                    style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
                    variant="h6"
                    component="div"
                    sx={{ flexGrow: 1, mb: 1 }}
                    type="text"
                    fullWidth
                    InputProps={{
                        readOnly: true,
                    }}
                >
                    Email
                </Typography>
                <Typography
                    style={{ fontSize: 16 }}
                    variant="h6"
                    component="div"
                    sx={{ flexGrow: 1, mb: 2 }}
                    type="text"
                    fullWidth
                    InputProps={{
                        readOnly: true,
                    }}
                >
                    {account.email}
                </Typography>
                {
                    changePass?
                                <>
                                <ChangePasswordForm account={account} etag={etag} hideChange={setChangePass}/>
                                <Grid item xs={6}>
                                    <Button onClick={handleChangePassword}>{t("back_button")}</Button>
                                </Grid>
                                </>:
                    <Grid container spacing={2}>
                    <Grid item xs={6}>
                        <Button>Edit Email</Button>
                    </Grid>
                    <Grid item xs={6}>
                        <Button onClick={handleChangePassword}>{t("change_password_button")}</Button>
                    </Grid>
                </Grid>
                }
                
                <Typography
                    style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
                    variant="h6"
                    component="div"
                    sx={{ flexGrow: 1, mb: 1, mt: 1 }}
                    type="text"
                    fullWidth
                    InputProps={{
                        readOnly: true,
                    }}
                >
                    Login
                </Typography>
                <Typography
                    style={{ fontSize: 16 }}
                    variant="h6"
                    component="div"
                    sx={{ flexGrow: 1, mb: 2 }}
                    type="text"
                    fullWidth
                    InputProps={{
                        readOnly: true,
                    }}
                >
                    {account.login}
                </Typography>
                <Typography
                    style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
                    variant="h6"
                    component="div"
                    sx={{ flexGrow: 1, mb: 1 }}
                    type="text"
                    fullWidth
                    InputProps={{
                        readOnly: true,
                    }}
                >
                    {t("access_level")}
                </Typography>
                <Typography
                    style={{ fontSize: 16 }}
                    variant="h6"
                    component="div"
                    sx={{ flexGrow: 1, mb: 2 }}
                    type="text"
                    fullWidth
                    InputProps={{
                        readOnly: true,
                    }}
                >
                    {account.accessLevels[0].role}
                </Typography>
                {isAdmin && (
                    <Box>
                        <Typography
                            style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
                            component="div"
                            sx={{ flexGrow: 1, mb: 1 }}
                            type="text"
                            fullWidth
                            InputProps={{
                                readOnly: true,
                            }}
                        >
                            {t("work_phone_number")}
                        </Typography>
                        <Typography
                            style={{ fontSize: 16 }}
                            variant="h6"
                            component="div"
                            sx={{ flexGrow: 1, mb: 2 }}
                            type="text"
                            fullWidth
                            InputProps={{
                                readOnly: true,
                            }}
                        >
                            {String(account.accessLevels[0].workPhoneNumber)}
                        </Typography>
                    </Box>
                )}
                {isChemist && (
                    <Box>
                        <Typography
                            style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
                            component="div"
                            sx={{ flexGrow: 1, mb: 1 }}
                            type="text"
                            fullWidth
                            InputProps={{
                                readOnly: true,
                            }}
                        >
                            {t("licesne_number")}
                        </Typography>
                        <Typography
                            style={{ fontSize: 16 }}
                            variant="h6"
                            component="div"
                            sx={{ flexGrow: 1, mb: 2 }}
                            type="text"
                            fullWidth
                            InputProps={{
                                readOnly: true,
                            }}
                        >
                            {String(account.accessLevels[0].licenseNumber)}
                        </Typography>
                    </Box>
                )}
                {isPatient && (
                    <Box>
                        <Grid container spacing={2}>
                            <Grid item xs={6}>
                                <Typography
                                    style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
                                    component="div"
                                    sx={{ flexGrow: 1, mb: 1 }}
                                    type="text"
                                    fullWidth
                                    InputProps={{
                                        readOnly: true,
                                    }}
                                >
                                    {t("First name")}
                                </Typography>
                                <Typography
                                    style={{ fontSize: 16 }}
                                    variant="h6"
                                    component="div"
                                    sx={{ flexGrow: 1, mb: 2 }}
                                    type="text"
                                    fullWidth
                                    InputProps={{
                                        readOnly: true,
                                    }}
                                >
                                    {String(account.accessLevels[0].firstName)}
                                </Typography>
                            </Grid>
                            <Grid item xs={6}>
                                <Typography
                                    style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
                                    component="div"
                                    sx={{ flexGrow: 1, mb: 1 }}
                                    type="text"
                                    fullWidth
                                    InputProps={{
                                        readOnly: true,
                                    }}
                                >
                                    {t("last_name")}
                                </Typography>
                                <Typography
                                    style={{ fontSize: 16 }}
                                    variant="h6"
                                    component="div"
                                    sx={{ flexGrow: 1, mb: 2 }}
                                    type="text"
                                    fullWidth
                                    InputProps={{
                                        readOnly: true,
                                    }}
                                >
                                    {String(account.accessLevels[0].lastName)}
                                </Typography>
                            </Grid>
                        </Grid>
                        <Typography
                            style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
                            component="div"
                            sx={{ flexGrow: 1, mb: 1 }}
                            type="text"
                            fullWidth
                            InputProps={{
                                readOnly: true,
                            }}
                        >
                            {t("phone_number")}
                        </Typography>
                        <Typography
                            style={{ fontSize: 16 }}
                            variant="h6"
                            component="div"
                            sx={{ flexGrow: 1, mb: 2 }}
                            type="text"
                            fullWidth
                            InputProps={{
                                readOnly: true,
                            }}
                        >
                            {String(account.accessLevels[0].phoneNumber)}
                        </Typography>
                        <Typography
                            style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
                            component="div"
                            sx={{ flexGrow: 1, mb: 1 }}
                            type="text"
                            fullWidth
                            InputProps={{
                                readOnly: true,
                            }}
                        >
                            Pesel
                        </Typography>
                        <Typography
                            style={{ fontSize: 16 }}
                            variant="h6"
                            component="div"
                            sx={{ flexGrow: 1, mb: 2 }}
                            type="text"
                            fullWidth
                            InputProps={{
                                readOnly: true,
                            }}
                        >
                            {String(account.accessLevels[0].pesel)}
                        </Typography>
                        <Typography
                            style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
                            component="div"
                            sx={{ flexGrow: 1, mb: 1 }}
                            type="text"
                            fullWidth
                            InputProps={{
                                readOnly: true,
                            }}
                        >
                            NIP
                        </Typography>
                        <Typography
                            style={{ fontSize: 16 }}
                            variant="h6"
                            component="div"
                            sx={{ flexGrow: 1, mb: 2 }}
                            type="text"
                            fullWidth
                            InputProps={{
                                readOnly: true,
                            }}
                        >
                            {String(account.accessLevels[0].nip)}
                        </Typography>
                    </Box>
                )}
                <Button>Edit Account Details</Button>
            </Paper>
            <ToastContainer/>
        </div>
    );
}

export default AccountDetails;