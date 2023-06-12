import React, {useEffect, useState} from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import {Skeleton, useTheme,} from "@mui/material";
import {getSelfOrders} from "../api/moa/orderApi";
import moment from "moment";
import {useTranslation} from "react-i18next";

export default function ShowSelfOrders() {

    const [selfOrders, setSelfOrders] = useState([]);
    const [loading, setLoading] = useState(false);
    const {t} = useTranslation();
    const theme = useTheme();
    useEffect(() => {
        setLoading(true)

        getSelfOrders().then((response) => {

            setSelfOrders(response.data);
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
                            <TableCell sx={{color: "white"}}>{t("order_state")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("order_date")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("medication_name")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("medication_price")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("medication_quantity")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("order_total_price")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("prescription_number")}</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {selfOrders.map((item, index) => {
                            const totalPrice = item.orderMedication.reduce((sum, medication) => {
                                return sum + (medication.medication.currentPrice * medication.quantity);
                            }, 0);

                            return (
                                <React.Fragment key={index}>
                                    {item.orderMedication.map((medication, medicationIndex) => (
                                        <TableRow key={`${index}-${medicationIndex}`}>
                                            {medicationIndex === 0 && (
                                                <>
                                                    <TableCell
                                                        rowSpan={item.orderMedication.length}>{t(item.orderState)}</TableCell>
                                                    <TableCell
                                                        rowSpan={item.orderMedication.length}>{formatOrderDate(item.orderDate)}</TableCell>
                                                    <TableCell>{medication.medication.name}</TableCell>
                                                    <TableCell>{medication.medication.currentPrice}</TableCell>
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
                                                    <TableCell>{medication.medication.currentPrice}</TableCell>
                                                    <TableCell>{medication.quantity}</TableCell>
                                                </>
                                            )}
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
