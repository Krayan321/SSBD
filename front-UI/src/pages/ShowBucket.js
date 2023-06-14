import React, {useEffect, useState} from "react";
import TableContainer from "@mui/material/TableContainer";
import Paper from "@mui/material/Paper";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";
import {Box, Button, IconButton, Skeleton, Stack, TextField, useTheme} from "@mui/material";
import {toast, ToastContainer} from "react-toastify";
import PointOfSaleIcon from '@mui/icons-material/PointOfSale';
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import {Pathnames} from "../router/Pathnames";
import {getSelfAccountDetails} from "../api/mok/accountApi";
import {createOrder} from "../api/moa/orderApi";
import ConfirmationDialog from "../components/ConfirmationDialog";
import ProductionQuantityLimitsIcon from '@mui/icons-material/ProductionQuantityLimits';
import axios from 'axios';
import {getMedication} from "../api/moa/medicationApi";

export default function ShowBucket() {
    const [bucket, setBucket] = useState([]);
    const [accessLevels, setAccessLevels] = useState([]);
    const navigate = useNavigate();
    const [quantity, setQuantity] = useState();
    const theme = useTheme();
    const [loading, setLoading] = useState(false);
    const {t} = useTranslation();
    const [prescriptionNumber, setPrescriptionNumber] = useState();
    const [dialogOpen, setDialogOpen] = useState(false);
    const [itemToDelete, setItemToDelete] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            const response = await getSelfAccountDetails();
            setAccessLevels(response.data.accessLevels);
            let data;
            accessLevels.forEach((element) => {
                if (element.role === "PATIENT") {
                    data = element;
                }
            })

        };
        fetchData();
        if (localStorage.getItem("bucket") !== null) {
            const str = localStorage.getItem("bucket")
            if (!str) return;
            const array = JSON.parse(str);
            console.log(array);
            setBucket(array);
        } else {
            setBucket([]);
        }
    }, [localStorage])

    const handleChange = async (medicationName, quantity) => {
        let temp_to_change = bucket.find(({name}) => name === medicationName);

        temp_to_change.quantity = quantity;

        localStorage.setItem("bucket", JSON.stringify(bucket))
        window.location.reload();
    };

    const handleBuy = () => {
        if (prescriptionNumber < 100000000 || prescriptionNumber > 999999999) {
            toast.error(t("bad_prescription_number"))
            return
        }

        let order_medication = [];

        const order_date = Date.now();
        const prescription = {
            prescriptionNumber: Math.floor(100000000 + Math.random() * 900000000)
        }
        const str = localStorage.getItem("bucket")
        const array = JSON.parse(str);

        array.forEach((element) => {
            getMedication(element.id).then((response) => {
                const etag = response.headers['etag'].split('"').join('');
                const version = response.data.version;
                order_medication.push({
                    name: element.name,
                    quantity: element.quantity,
                    version: version,
                    etag: etag,
                    signablePayload: element.name + "." + version,
                });
            }).catch((error) => {
                setLoading(false)
                toast.error(t(error.response.data.message),
                    {position: "top-center"});
            })
        })

        const to_send = {
            orderDate: order_date,
            orderMedications: order_medication,
            prescription: prescription
        }

        console.log(to_send)
        createOrder(to_send);
    };

    const handleDelete = (medicationName) => {
        setItemToDelete(medicationName);
        setDialogOpen(true);
    };


    const handleConfirmation = (accepted) => {
        if (accepted) {
            const updatedBucket = bucket.filter(({name}) => name !== itemToDelete);
            setBucket(updatedBucket);
            localStorage.setItem("bucket", JSON.stringify(updatedBucket));
            toast.success(t("medication_removed_from_bucket"), {position: "top-center"});
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
        <div style={{
            display: "flex",
            justifyContent: "center",
            alignContent: "center",
            flexDirection: "column"
        }}>
            <Box sx={{marginBottom: "10px", textAlign: "center"}}>
                <IconButton
                    variant="contained"
                    onClick={() => handleBuy()}
                >
                    <PointOfSaleIcon/>
                </IconButton>
                <TextField
                    type="number"
                    variant='outlined'
                    color='secondary'
                    align="right"
                    inputMode="numeric"
                    onChange={(e) => {
                        setPrescriptionNumber(e.target.value);
                    }
                    }
                />
            </Box>
            <TableContainer sx={{maxWidth: "800px", margin: "auto"}} component={Paper}>
                <Table aria-label="simple table">
                    <TableHead sx={{backgroundColor: theme.palette.primary.main}}>
                        <TableRow sx={{color: "white"}}>
                            <TableCell sx={{color: "white"}}>{t("show_bucket_name")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("show_bucket_price")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("show_bucket_category")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("show_bucket_quantity")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("show_bucket_input")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right"></TableCell>
                            <TableCell sx={{color: "white"}} align="right"></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {bucket.map((row) => (
                            <TableRow
                                key={row.id}
                                sx={{"&:last-child td, &:last-child th": {border: 0}}}
                            >
                                <TableCell component="th" scope="row">
                                    {row.name}
                                </TableCell>
                                <TableCell align="right">{row.price + " zł"}</TableCell>
                                <TableCell align="right">{row.categoryName}</TableCell>
                                <TableCell align="right">{row.quantity}</TableCell>
                                <TableCell align="right">
                                    <TextField
                                        type="number"
                                        variant='outlined'
                                        color='secondary'
                                        align="right"
                                        inputMode="numeric"
                                        onChange={(e) => {
                                            if (e.target.value > 99) {
                                                e.target.value = "99";
                                            }
                                            if (e.target.value < 0) {
                                                e.target.value = "1";
                                            }
                                            if (e.target.value === "01" || e.target.value === "02" || e.target.value === "03" || e.target.value === "04" || e.target.value === "05" || e.target.value === "06" || e.target.value === "07" || e.target.value === "08" || e.target.value === "09") {
                                                e.target.value = "1";
                                            }

                                            if (e.target.value === "0") {
                                                const index = bucket.indexOf(row);
                                                if (index > -1) { // only splice array when item is found
                                                    bucket.splice(index, 1); // 2nd parameter means remove one item only
                                                    localStorage.setItem("bucket", JSON.stringify(bucket))
                                                    window.location.reload();
                                                }
                                                setBucket(bucket);
                                            }

                                            setQuantity(e.target.value);
                                        }
                                        }
                                    />
                                </TableCell>
                                <TableCell align="right">
                                    <Button onClick={() => handleChange(row.name, quantity)}>
                                        {t("show_bucket_button")}
                                    </Button>
                                </TableCell>
                                <TableCell align="right">
                                    <Button onClick={() => handleDelete(row.name)}>
                                        {t("delete")}
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <ToastContainer/>
                <ConfirmationDialog
                    open={dialogOpen}
                    title={t("confirm_delete_medication_from_basket")}
                    actions={[
                        {label: t("delete"), handler: () => handleConfirmation(true), color: "primary"},
                        {label: t("cancel"), handler: () => handleConfirmation(false), color: "secondary"},
                    ]}
                    onClose={() => setDialogOpen(false)}
                />
            </TableContainer>
        </div>
    );
}
