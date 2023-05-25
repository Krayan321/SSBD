import React, {useState, useEffect} from "react";
import axios from "axios";
import {useTranslation} from "react-i18next";
import { Paper, Button } from "@mui/material";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import i18n from "i18next";
import {getSelfAccountDetails} from "../api/mok/accountApi";


function Landing() {
    const user = useSelector((state) => state.user);
    const { t } = useTranslation();
    const navigate = useNavigate();
    const isAdmin = user.roles.includes("ADMIN");

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getSelfAccountDetails();
                if (response.data.language === "en") {
                    i18n.changeLanguage("en");
                } else if (response.data.language === "pl") {
                    i18n.changeLanguage("pl");
                } else if (response.data.language === "cs") {
                    i18n.changeLanguage("cs");
                } else {
                    i18n.changeLanguage("en");
                }

            } catch (error) {
                console.error(error);
            }
        };
        fetchData();
    }, []);


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
