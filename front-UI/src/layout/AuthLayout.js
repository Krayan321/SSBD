import React from 'react';
import AuthNavbar from "../components/AuthNavbar";

function AuthLayout({children}) {
    return (
        <>
            <AuthNavbar/>
            {children}
        </>
    );
}

export default AuthLayout;