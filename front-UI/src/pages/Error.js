import React, {useEffect} from 'react';
import {Paper} from "@mui/material";
import {useTranslation} from "react-i18next";
import {useDispatch} from "react-redux";
import {logout} from "../redux/UserSlice";

const Error = () => {

    const dispatch = useDispatch();
    const {t} = useTranslation();
    const paperStyle = {padding: '30px 20px', margin: "auto", width: 400}

    useEffect(() => {
        dispatch(logout());
    }, [])


    return (
        <div style={{display: 'flex', justifyContent: 'center', alignContent: 'center', marginTop: '3rem'}}>
            <Paper elevation={20} style={paperStyle}>
                <h2 style={{fontFamily: 'Lato'}}>
                    {t("page_not_found_or_access_denied")} </h2>
            </Paper>
        </div>
    );
};

export default Error;