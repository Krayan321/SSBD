import React from "react";
import axios from "axios";
import {useTranslation} from "react-i18next";
import {Paper} from "@mui/material";
import {useSelector} from "react-redux";

function Landing() {

    const user = useSelector((state) => state.user);
    const {t} = useTranslation();

    return (
        <div className="wrapper">
            <Paper elevation={20} className="paper">
                <h2>
                    {t("welcome_back")} {user.sub} </h2>
            </Paper>
        </div>
    );
}

export default Landing;
