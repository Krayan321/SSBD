import {Pathnames} from "./Pathnames";
import Home from "../pages/Home";
import Login from "../pages/Login";
import SignUp from "../pages/SignUp";
import SingleAccount from "../pages/SingleAccount";
import AccountInfo from "../pages/AccountInfo";
import Landing from "../pages/Landing";
import EditSingleAccount from "../pages/EditSingleAccount";
import EditChemist from "../pages/EditChemist";
import AllAccounts from "../pages/AllAccounts";


export const publicRoutes = [
    {
        path: Pathnames.public.home,
        Component: Home,
    },
    {
        path: Pathnames.public.login,
        Component: Login,
    },
    {
        path: Pathnames.public.signup,
        Component: SignUp,
    },

]

export const AuthRoutes = [
    {
        path: Pathnames.auth.self,
        Component: AccountInfo
    },
    {
        path: Pathnames.auth.landing,
        Component: Landing
    },

]

export const PatientRoutes = []

export const AdminRoutes = [
    {
        path: Pathnames.admin.account,
        Component: SingleAccount
    },
    {
        path: Pathnames.admin.editSingleAccount,
        Component: EditSingleAccount
    },
    {
        path: Pathnames.admin.editChemist,
        Component: EditChemist
    },
    {
        path: Pathnames.admin.details,
        Component: SingleAccount
    },
    {
        path: Pathnames.admin.accounts,
        Component: AllAccounts
    },
]

export const ChemistRoutes = []
