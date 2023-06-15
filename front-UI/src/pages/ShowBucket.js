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
import {createOrder, submitOrder} from "../api/moa/orderApi";
import ConfirmationDialog from "../components/ConfirmationDialog";
import ProductionQuantityLimitsIcon from '@mui/icons-material/ProductionQuantityLimits';
import axios from 'axios';
import {getMedication} from "../api/moa/medicationApi";
import dayjs from "dayjs";

export default function ShowBucket() {
    const [bucket, setBucket] = useState([]);
    const [accessLevels, setAccessLevels] = useState([]);
    const navigate = useNavigate();
    const [quantity, setQuantity] = useState();
    const theme = useTheme();
    const [loading, setLoading] = useState(false);
    const { t } = useTranslation();
    const [id, setId] = useState();
    const [prescriptionNumber, setPrescriptionNumber] = useState(false);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [isOnPrescription, setIsOnPrescription] = useState(false);
    const [itemToDelete, setItemToDelete] = useState(null);

    useEffect(() => {
        refreshCart();
    }, [localStorage]);


    const refreshCart = function() {
        if (localStorage.getItem("bucket") !== null) {
            const str = localStorage.getItem("bucket");
            if (!str) return;
            const array = JSON.parse(str);
            setBucket(array);
            setIsOnPrescription(false);
            for (const om of array) {
                if(om.medication.categoryDTO.isOnPrescription){
                    setIsOnPrescription(true);
                }
            }
        }
    }

    const handleChange = async (medicationName, quantity) => {
        let temp_to_change = bucket.find(({ medication }) => medication.name === medicationName);

        temp_to_change.quantity = quantity;

        localStorage.setItem("bucket", JSON.stringify(bucket));
        refreshCart();
    };

    const handleDelete = (medicationName) => {
        for (const i in bucket) {
            if(bucket[i].medication.name === medicationName){
                bucket.splice(i, 1);
                localStorage.setItem("bucket", JSON.stringify(bucket));
                refreshCart();
            }
        }
    };

    const handleBuy = () => {
        setLoading(true);
        const order = {
            orderDate: dayjs().format('YYYY-MM-DDTHH:mm:ss.SSS'),
            prescription: {
                prescriptionNumber: prescriptionNumber ? prescriptionNumber : null
            },
            orderMedications: []
        };

        const str = localStorage.getItem("bucket")
        const array = JSON.parse(str);

        const length = array.length;
        let count = 0;
        array.forEach((om) => {
            getMedication(om.medication.id).then((response) => {
                const etag = response.headers['etag'].split('"').join('');
                const version = response.data.version;
                order.orderMedications.push({
                    name: om.medication.name,
                    quantity: om.quantity,
                    version: version,
                    etag: etag,
                });
                count++;
                if(count == length) {
                    createOrder(order).then((response) => {
                        setLoading(false)
                        toast.success(t("bought_successfully"), {position: "top-center"});
                    }).catch((error) => {
                        setLoading(false)
                        toast.error(t(error.response.data.message),
                            {position: "top-center"});
                    })
                }
            }).catch((error) => {
                setLoading(false)
                toast.error(t(error.response.data.message),
                    {position: "top-center"});
            })
        })
    };



    const handleConfirmation = (accepted) => {
        if (accepted) {
            // sendDataToBackend();
        }
        setItemToDelete(null);
        setDialogOpen(false);
    };

    if (loading) {
        return (

            <TableContainer component={Paper}>
                <Table>
                    <TableHead sx={{backgroundColor: theme.palette.primary.main}}>
                        <TableRow>
                            <TableCell sx={{color: "white"}}>{t("show_bucket_name")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("show_bucket_price")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("show_bucket_category")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("show_bucket_quantity")}</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        <TableRow>
                            <TableCell>
                                <Skeleton/>
                            </TableCell>
                            <TableCell>
                                <Skeleton/>
                            </TableCell>
                            <TableCell>
                                <Skeleton/>
                            </TableCell>
                            <TableCell>
                                <Skeleton/>
                            </TableCell>
                            <TableCell>
                                <Skeleton/>
                            </TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </TableContainer>
        );
    }

    if (bucket.length === 0) {
        return (
            <div style={{textAlign: 'center'}}>
                <Paper elevation={20} className="paper">
                    <Stack justifyContent="center"
                           alignItems="center" spacing={2}>
                        <ProductionQuantityLimitsIcon style={{fontSize: 60}}/>
                        <h5 style={{fontFamily: 'Lato'}}>
                            {t("whoa_such_empty")} </h5>
                        <Button onClick={() => {
                            navigate(Pathnames.patientChemist.medications,)
                        }} variant="contained" color="primary">
                            {t("add_something_to_basket")}
                        </Button>
                    </Stack>
                </Paper>
            </div>
        );
    }

    return (
        <div style={{ display: "flex", justifyContent: "center", alignContent: "center", flexDirection: "column" }}>
            <Box sx={{ marginBottom: "10px", textAlign: "center" }}>
                <IconButton variant="contained" onClick={() => handleBuy()}>
                    <PointOfSaleIcon />
                </IconButton>
                {isOnPrescription && <TextField
                    type="text"
                    label={t("prescription_number")}
                    variant='outlined'
                    color='secondary'
                    align="right"
                    onChange={(e) => {setPrescriptionNumber(e.target.value)}}
                />}
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
                                <TableRow key={row.medication.id}>
                                    <TableCell component="th" scope="row">
                                        {row.medication.name}
                                    </TableCell>
                                    <TableCell align="right">
                                        <TextField
                                            type="number"
                                            value={row.quantity}
                                            onChange={(e) => handleChange(row.medication.name, e.target.value)}
                                        />
                                    </TableCell>
                                    <TableCell align="right">
                                        <Button variant="outlined" color="error" onClick={() => handleDelete(row.medication.name)}>
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
