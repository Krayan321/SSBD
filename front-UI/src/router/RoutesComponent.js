import {Navigate, Route, Routes} from "react-router-dom";
import {Pathnames} from "./Pathnames";
import {AdminRoutes, AuthRoutes, ChemistRoutes, PatientRoutes, publicRoutes} from "./Routes";
import Navbar from "../components/Navbar";
import AuthNavbar from "../components/AuthNavbar";
import jwtDecode from "jwt-decode";
import {login as loginDispatch} from "../redux/UserSlice";
import Error from "../pages/Error";
import {useDispatch, useSelector} from "react-redux";

export const RoutesComponent = () => {

    const JWT_TOKEN = "jwtToken";
    const user = useSelector((state) => state.user);

    const dispatch = useDispatch();
    const token = localStorage.getItem(JWT_TOKEN);
    try {
        if (user.exp === "" && token) {
            const decoded_token = jwtDecode(token);

            if (decoded_token) {
                dispatch(loginDispatch(decoded_token));
            }
        }
    } catch (error) {
        console.error(error);
    }


    return (
        <Routes>
            {publicRoutes.map(({path, Component}) => (
                <Route
                    key={path}
                    path={path}
                    element={
                        <>
                            <Navbar/>
                            <Component/>
                        </>
                    }
                />
            ))}
            {
                user.cur === "PATIENT" &&
                PatientRoutes.map(({path, Component}) => (
                    <Route
                        key={path}
                        path={path}
                        element={
                            <>
                                <AuthNavbar/>
                                <Component/>
                            </>
                        }/>
                ))}
            {
                user.cur === "ADMIN" &&
                AdminRoutes.map(({path, Component}) => (
                    <Route
                        key={path}
                        path={path}
                        element={
                            <>
                                <AuthNavbar/>
                                <Component/>
                            </>
                        }/>
                ))}
            {
                user.cur === "CHEMIST" &&
                ChemistRoutes.map(({path, Component}) => (
                    <Route
                        key={path}
                        path={path}
                        element={
                            <>
                                <AuthNavbar/>
                                <Component/>
                            </>
                        }/>
                ))}
            {
                (user.cur === "ADMIN" || user.cur === "PATIENT" || user.cur === "CHEMIST") &&
                AuthRoutes.map(({path, Component}) => (
                    <Route
                        key={path}
                        path={path}
                        element={
                            <>
                                <AuthNavbar/>
                                <Component/>
                            </>
                        }/>
                ))}

            <Route path="*" element={
                <>
                    <Navbar/>
                    <Error/>
                </>
            }/>
        </Routes>
    )
}