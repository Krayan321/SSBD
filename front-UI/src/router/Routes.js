import {Pathnames} from "./Pathnames";
import Home from "../pages/Home";
import Login from "../pages/Login";
import SignUp from "../pages/SignUp";
import ResetPassword from "../pages/ResetPassword";
import SetResetPassword from "../pages/SetResetPassword";
import SingleAccount from "../pages/SingleAccount";
import Landing from "../pages/Landing";
import EditSingleAccount from "../pages/EditSingleAccount";
import EditChemist from "../pages/EditChemist";
import AllAccounts from "../pages/AllAccounts";
import AccountDetails from "../pages/AccountDetails";
import ConfirmAccount from "../pages/ConfirmAccount";
import RoleSelectionForm from "../pages/RoleSelection";
import AddPatient from "../pages/AddPatient";
import AddChemist from "../pages/AddChemist";
import AddAdministrator from "../pages/AddAdministrator";
import ShowSelfOrders from "../pages/ShowSelfOrders";
import ShowWaitingOrders from "../pages/ShowWaitingOrders";
import OrdersToApprove from "../pages/OrdersToApprove";

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
    {
        path: Pathnames.public.resetPassword,
        Component: ResetPassword,
    },
    {
        path: Pathnames.public.setResetPassword,
        Component: SetResetPassword,
    },
    {
        path: Pathnames.public.confirmAccount,
        Component: ConfirmAccount,
    },
];

export const AuthRoutes = [
    {
        path: Pathnames.auth.self,
        Component: AccountDetails,
    },
    {
        path: Pathnames.auth.landing,
        Component: Landing,
    },
];

export const PatientRoutes = [
    {
        path: Pathnames.patient.selfOrders,
        Component: ShowSelfOrders,
    }
];

export const AdminRoutes = [
    {
        path: Pathnames.admin.account,
        Component: SingleAccount,
    },
    {
        path: Pathnames.admin.editSingleAccount,
        Component: EditSingleAccount,
    },
    {
        path: Pathnames.admin.editChemist,
        Component: EditChemist,
    },
    {
        path: Pathnames.admin.details,
        Component: SingleAccount,
    },
    {
        path: Pathnames.admin.accounts,
        Component: AllAccounts,
    },
    {
        path: Pathnames.admin.createAccount,
        Component: RoleSelectionForm,
    },
    {
        path: Pathnames.admin.addPatient,
        Component: AddPatient,
    },
    {
        path: Pathnames.admin.addChemist,
        Component: AddChemist,
    },
    {
        path: Pathnames.admin.addAdministrator,
        Component: AddAdministrator,
    },
];

export const ChemistRoutes = [
    {
        path:Pathnames.chemist.waitingOrders,
        Component: ShowWaitingOrders,
    },
    {
        path:Pathnames.chemist.ordersToApprove,
        Component: OrdersToApprove,
    }
];
