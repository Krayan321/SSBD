import React, {useState} from 'react';
import MenuItem from "@mui/material/MenuItem";
import {ROLES} from "../constants/Constants";
import {Divider, Icon, useTheme} from "@mui/material";
import LocalShipping from '@mui/icons-material/LocalShipping';
import {useDispatch, useSelector} from "react-redux";
import {Sidebar, Menu} from "react-pro-sidebar";
import PeopleIcon from '@mui/icons-material/People';
import {
    Logout, Quiz,
} from "@mui/icons-material";
import IconButton from "@mui/material/IconButton";
import {Pathnames} from "../router/Pathnames";
import {useNavigate} from "react-router-dom";
import {logout} from "../redux/UserSlice";
import ConfirmationDialog from "./ConfirmationDialog";
import {useTranslation} from "react-i18next";
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import {Box} from "@mui/system";
import Typography from "@mui/material/Typography";
import ShoppingBasketIcon from '@mui/icons-material/ShoppingBasket';
import AlarmIcon from '@mui/icons-material/Alarm';


function AuthSidebar() {

    const userRole = useSelector((state) => state.user.cur);
    const login = useSelector((state) => state.user.sub);
    const navigate = useNavigate();
    const [dialogOpen, setDialogOpen] = useState(false);
    const {t} = useTranslation();
    const dispatch = useDispatch();
    const theme = useTheme();

    const accept = () => {
        dispatch(logout());
        navigate(Pathnames.public.login);
    };

    const reject = () => {
        setDialogOpen(false);
    };

    return (
        <>
            <Sidebar className="text-white" backgroundColor={theme.palette.primary.main}>
                <Menu iconShape="square">
                    <Box mb="25px">
                        <Box textAlign="center">
                            <Typography
                                variant="h5"
                                fontWeight="bold"
                                sx={{m: "20px 0 0 0"}}
                            >
                                {login}
                            </Typography>
                            <Typography variant="h6">
                                {userRole}
                            </Typography>
                        </Box>
                    </Box>
                    <Divider variant="middle" sx={{bgcolor: "white"}}/>
                    <Box ml="25px">
                        {
                            userRole === ROLES.ADMIN &&
                                <>
                                    <MenuItem
                                        onClick={() => {
                                            navigate(Pathnames.admin.createAccount);
                                        }}
                                    >
                                        <IconButton
                                            color="inherit"
                                        >
                                            <PersonAddIcon/>
                                        </IconButton>
                                        {t("create_account")}
                                    </MenuItem>
                                    <MenuItem
                                        onClick={() => {
                                            navigate(Pathnames.admin.accounts);
                                        }}
                                    >
                                        <IconButton
                                            color="inherit"
                                        >
                                            <PeopleIcon/>
                                        </IconButton>
                                        {t("accounts")}
                                    </MenuItem>
                                </>
                        }
                        {
                            userRole === ROLES.CHEMIST &&
                            <>
                                <MenuItem
                                    onClick={() => {
                                        navigate(Pathnames.chemist.shipment);
                                    }}
                                >
                                    <IconButton
                                        color="inherit"
                                    >
                                        <LocalShipping/>
                                    </IconButton>
                                    {t("shipment")}
                                </MenuItem>
                                <MenuItem
                                    onClick={() => {
                                        navigate(Pathnames.chemist.waitingOrders);
                                    }}
                                >
                                    <IconButton
                                        color="inherit"
                                    >
                                        <AlarmIcon/>
                                    </IconButton>
                                    {t("waitingOrders")}
                                </MenuItem>
                                <MenuItem
                                    onClick={() => {
                                        navigate(Pathnames.chemist.ordersToApprove);
                                    }}
                                >
                                    <IconButton
                                        color="inherit"
                                    >
                                        <Quiz/>
                                    </IconButton>
                                    {t("ordersToApprove")}
                                </MenuItem>
                            </>
                        }
                        {
                            userRole === ROLES.PATIENT &&
                            <>
                                <MenuItem
                                    onClick={() => {
                                        navigate(Pathnames.patient.selfOrders);
                                    }}
                                >
                                    <IconButton
                                        color="inherit"
                                    >
                                        <ShoppingBasketIcon/>
                                    </IconButton>
                                    {t("orders")}
                                </MenuItem>
                                <MenuItem
                                    onClick={() => {
                                        navigate(Pathnames.patient.showBucket);
                                    }}
                                >
                                    <IconButton
                                        color="inherit"
                                    >
                                        <ShoppingBasketIcon/>
                                    </IconButton>
                                    {t("bucket")}
                                </MenuItem>
                            </>
                        }
                        {
                            (userRole === ROLES.ADMIN || userRole === ROLES.PATIENT || userRole === ROLES.CHEMIST) &&
                            <>
                                <MenuItem
                                    onClick={() => {
                                        setDialogOpen(true)
                                    }}
                                >
                                    <IconButton
                                        color="inherit"
                                    >
                                        <Logout/>
                                    </IconButton>

                                    {t("logout")}
                                </MenuItem>
                            </>
                        }
                    </Box>
                </Menu>
                <ConfirmationDialog
                    open={dialogOpen}
                    title={t("confirm_logout")}
                    actions={[
                        {label: t("logout"), handler: accept, color: "primary"},
                        {label: t("cancel"), handler: reject, color: "secondary"},
                    ]}
                    onClose={() => setDialogOpen(false)}
                />
            </Sidebar>
        </>
    );
}

export default AuthSidebar;