import React, {useEffect} from 'react';
import AuthNavbar from "../components/AuthNavbar";
import {useLocation, useNavigate} from "react-router-dom";
import jwtDecode from "jwt-decode";
import {useTranslation} from "react-i18next";
import {useDispatch} from "react-redux";
import {toast, ToastContainer} from "react-toastify";
import {Pathnames} from "../router/Pathnames";
import {logout} from "../redux/UserSlice";
import {login as loginDispatch} from "../redux/UserSlice";
import {ACCESS_LEVEL, JWT_TOKEN} from "../constants/Constants";

function AuthLayout({children}) {

    const location = useLocation();
    const token = localStorage.getItem(JWT_TOKEN);
    const navigate = useNavigate();
    const {t} = useTranslation();
    const dispatch = useDispatch();

    useEffect(() => {
        try {
            setTimeout(() => {

                if (!localStorage.getItem(JWT_TOKEN) && !localStorage.getItem(ACCESS_LEVEL)) {
                    navigate(Pathnames.public.error);
                } else {
                    const decoded_token = jwtDecode(token);
                    if (decoded_token.exp < Date.now() / 1000) {
                        toast.error(t("session_expired_login_again"))
                        dispatch(logout());
                        navigate(Pathnames.public.login)
                    } else {
                        dispatch(loginDispatch(decoded_token));
                    }
                }

            }, 50);

        } catch (error) {
            console.error(error);
        }

    }, [location.pathname])

    return (
        <>
            <AuthNavbar/>
            {children}
            <ToastContainer/>
        </>
    );
}

export default AuthLayout;