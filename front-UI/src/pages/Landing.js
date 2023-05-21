import React from "react";
import axios from "axios";
import {useTranslation} from "react-i18next";
import {Paper} from "@mui/material";
import {useSelector} from "react-redux";

function Landing() {

    const user = useSelector((state) => state.user);
    const {t} = useTranslation();
    const paperStyle = {padding: '30px 20px', margin: "auto", width: 400}

    return (
        <div style={{display: 'flex', justifyContent: 'center', alignContent: 'center', marginTop: '3rem'}}>
            <Paper elevation={20} style={paperStyle}>
                <h2 style={{fontFamily: 'Lato'}}>
                    {t("welcome_back")} {user.sub} </h2>
            </Paper>
        </div>
    );
}

export default Landing;