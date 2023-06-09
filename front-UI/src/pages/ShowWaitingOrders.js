import React, {useEffect, useState} from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import {Skeleton, useTheme,} from "@mui/material";
import {getWaitingOrders} from "../api/moa/orderApi";
import moment from "moment";
import {useTranslation} from "react-i18next";
import IconButton from "@mui/material/IconButton";
import DeleteIcon from '@mui/icons-material/Delete';

export default function ShowWaitingOrders() {

    const [waitingOrders, setWaitingOrders] = useState([]);
    const [loading, setLoading] = useState(false);
    const {t} = useTranslation();
    const theme = useTheme();
    useEffect(() => {
        setLoading(true)

        getWaitingOrders().then((response) => {

            setWaitingOrders(response.data);
            console.log(response.data)
            setLoading(false)
        }).catch((error) => {
            console.log(error);
            setLoading(false)
        });

    }, [])


    const formatOrderDate = (dateString) => {
        const trimmedDate = dateString.slice(0, -5);
        const date = moment(trimmedDate);
        return date.format('DD-MM-YYYY HH:mm:ss');
    };

    if (loading) {
        return (
            <TableContainer component={Paper}>
                <Table>
                    <TableHead sx={{backgroundColor: theme.palette.primary.main}}>
                        <TableRow>
                            <TableCell sx={{color: "white"}}>{t("is_order_in_queue")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("order_date")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("medication_name")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("medication_price")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("medication_quantity")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("order_total_price")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("prescription_number")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("delete_order")}</TableCell>
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

    return (

        <div style={{
            display: "flex",
            justifyContent: "center",
            alignContent: "center",
            flexDirection: "column"
        }}>
            <TableContainer sx={{maxWidth: "800px", margin: "auto"}} component={Paper}>
                <Table>
                    <TableHead sx={{backgroundColor: theme.palette.primary.main}}>
                        <TableRow>
                            <TableCell sx={{color: "white"}}>{t("is_order_in_queue")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("order_date")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("medication_name")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("medication_price")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("medication_quantity")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("order_total_price")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("prescription_number")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("delete_order")}</TableCell>

                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {waitingOrders.map((item, index) => {
                            const totalPrice = item.orderMedication.reduce((sum, medication) => {
                                return sum + (medication.medication.price * medication.quantity);
                            }, 0);

                            return (
                                <React.Fragment key={index}>
                                    {item.orderMedication.map((medication, medicationIndex) => (
                                        <TableRow key={`${index}-${medicationIndex}`}>
                                            {medicationIndex === 0 && (
                                                <>
                                                    <TableCell
                                                        rowSpan={item.orderMedication.length}>{item.inQueue.toString() === "true" ? t("yes") : t("no")}</TableCell>
                                                    <TableCell
                                                        rowSpan={item.orderMedication.length}>{formatOrderDate(item.orderDate)}</TableCell>
                                                    <TableCell>{medication.medication.name}</TableCell>
                                                    <TableCell>{medication.medication.price}</TableCell>
                                                    <TableCell>{medication.quantity}</TableCell>
                                                    {medicationIndex === 0 && (
                                                        <TableCell
                                                            rowSpan={item.orderMedication.length}>{totalPrice}</TableCell>
                                                    )}
                                                    {item.prescription && medicationIndex === 0 ? (
                                                        <TableCell
                                                            rowSpan={item.orderMedication.length}>{item.prescription.prescriptionNumber}</TableCell>
                                                    ) : (
                                                        <TableCell rowSpan={item.orderMedication.length}>-</TableCell>
                                                    )}
                                                </>
                                            )}
                                            {medicationIndex > 0 && (
                                                <>
                                                    <TableCell>{medication.medication.name}</TableCell>
                                                    <TableCell>{medication.medication.price}</TableCell>
                                                    <TableCell>{medication.quantity}</TableCell>
                                                </>
                                            )}
                                            <TableCell>
                                                <IconButton
                                                    color="error"
                                                    aria-label={t("delete_order")}
                                                >
                                                    <DeleteIcon />
                                                </IconButton>
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </React.Fragment>
                            );
                        })}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    )
        ;
}

