import React, {useState, useEffect} from "react";
import axios from "axios";
import {useTranslation} from "react-i18next";
import { Paper, Button } from "@mui/material";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";


function Landing() {
    const user = useSelector((state) => state.user);
    const { t } = useTranslation();
    const navigate = useNavigate();
    const isAdmin = user.roles.includes("ADMIN");



    const handleCreateAccount = () => {
        navigate("/create-account");
    };

    return (
        <div className="wrapper">
            <Paper elevation={20} className="paper">
                <h2>{t("welcome_back")} {user.sub} </h2>
                {isAdmin  && (
                    <Button variant="contained" onClick={handleCreateAccount}>
                        {t("create_account")}
                    </Button>)}

            </Paper>
        </div>
    );
}

export default Landing;
