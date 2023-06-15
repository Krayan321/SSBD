import React, { useEffect, useState } from "react";
import TableContainer from "@mui/material/TableContainer";
import Paper from "@mui/material/Paper";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";
import { Box, Button, IconButton, Skeleton, Stack, TextField, useTheme } from "@mui/material";
import { toast, ToastContainer } from "react-toastify";
import PointOfSaleIcon from "@mui/icons-material/PointOfSale";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { Pathnames } from "../router/Pathnames";
import { getSelfAccountDetails } from "../api/mok/accountApi";
import { submitOrder } from "../api/moa/orderApi";
import ConfirmationDialog from "../components/ConfirmationDialog";
import ProductionQuantityLimitsIcon from "@mui/icons-material/ProductionQuantityLimits";
import axios from "axios";

export default function ShowBucket() {
    const [bucket, setBucket] = useState([]);
    const [accessLevels, setAccessLevels] = useState([]);
    const navigate = useNavigate();
    const [quantity, setQuantity] = useState();
    const theme = useTheme();
    const [loading, setLoading] = useState(false);
    const { t } = useTranslation();
    const [patientData, setPatientData] = useState();
    const [id, setId] = useState();
    const [dialogOpen, setDialogOpen] = useState(false);
    const [itemToDelete, setItemToDelete] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            const response = await getSelfAccountDetails();
            setAccessLevels(response.data.accessLevels);
            setId(response.data.id);
            let data;
            accessLevels.forEach((element) => {
                if (element.role === "PATIENT") {
                    data = element;
                }
            });
            setPatientData(data.id);
        };
        fetchData();
        if (localStorage.getItem("bucket") !== null) {
            const str = localStorage.getItem("bucket");
            if (!str) return;
            const array = JSON.parse(str);
            console.log(array);
            setBucket(array);
        } else {
            setBucket([]);
        }
    }, [localStorage]);

    const handleChange = async (medicationName, quantity) => {
        let temp_to_change = bucket.find(({ name }) => name === medicationName);

        temp_to_change.quantity = quantity;

        localStorage.setItem("bucket", JSON.stringify(bucket));
        window.location.reload();
    };

    const handleBuy = () => {
        setDialogOpen(true);
    };

    const handleConfirmation = (accepted) => {
        if (accepted) {
            sendDataToBackend();
            submitOrder(patientData)
                .then((response) => {
                    const orderState = response.data.orderState;

                    if (orderState === "IN_QUEUE") {
                        toast.success(t("order_in_queue"));
                    } else if (orderState === "TO_BE_APPROVED_BY_CHEMIST") {
                        toast.success(t("order_pending"));
                    } else {
                        toast.warning(t("order_finalized"));
                    }
                })
                .catch((error) => {
                    toast.error(t("error_occurred"));
                });
        }
        setItemToDelete(null);
        setDialogOpen(false);
    };


    const handleDelete = (medicationName) => {
        setItemToDelete(medicationName);
        setDialogOpen(true);
    };

    const sendDataToBackend = () => {
        const localStorageData = localStorage.getItem("bucket");
        const dataToSend = {
            localStorageData: JSON.parse(localStorageData),
        };

        axios.post("/order/submit", dataToSend) // Zmień "/backend-url" na odpowiedni adres URL backendu
            .then((response) => {

            })
            .catch((error) => {
                console.error(error);
            });
    };

    return (
        <div style={{ display: "flex", justifyContent: "center", alignContent: "center", flexDirection: "column" }}>
            <Box sx={{ marginBottom: "10px", textAlign: "center" }}>
                <IconButton variant="contained" onClick={() => handleBuy()}>
                    <PointOfSaleIcon />
                </IconButton>
            </Box>
            {bucket.length > 0 ? (
                <TableContainer component={Paper}>
                    <Table sx={{ minWidth: 650 }} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell>{t("medication_name")}</TableCell>
                                <TableCell align="right">{t("quantity")}</TableCell>
                                <TableCell align="right">{t("actions")}</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {bucket.map((row) => (
                                <TableRow key={row.id}>
                                    <TableCell component="th" scope="row">
                                        {row.name}
                                    </TableCell>
                                    <TableCell align="right">
                                        <TextField
                                            type="number"
                                            value={row.quantity}
                                            onChange={(e) => handleChange(row.name, e.target.value)}
                                        />
                                    </TableCell>
                                    <TableCell align="right">
                                        <Button variant="outlined" color="error" onClick={() => handleDelete(row.name)}>
                                            {t("delete")}
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            ) : (
                <Stack spacing={2} direction="row" justifyContent="center">
                    <Skeleton variant="rectangular" width={200} height={40} />
                </Stack>
            )}
            <ConfirmationDialog
                open={dialogOpen}
                title={t("confirm_order_title")}
                actions={[
                    { label: t("confirm"), handler: () => handleConfirmation(true), color: "primary" },
                    { label: t("cancel"), handler: () => handleConfirmation(false), color: "secondary" },
                ]}
                onClose={() => setDialogOpen(false)}
            />
            <ToastContainer />
        </div>
    );
}
