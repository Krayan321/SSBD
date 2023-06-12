import React, {useCallback, useEffect, useState} from "react";
import TableContainer from "@mui/material/TableContainer";
import Paper from "@mui/material/Paper";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";
import {
    Box,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    IconButton,
    Skeleton, TextField, useTheme
} from "@mui/material";
import {ToastContainer} from "react-toastify";
import {useTranslation} from "react-i18next";
import {number} from "yup";
import {useNavigate} from "react-router-dom";
import {Pathnames} from "../router/Pathnames";


export default function ShowBucket() {
    const [bucket, setBucket] = useState([]);
    const navigate = useNavigate();
    const [quantity, setQuantity] = useState();
    const theme = useTheme();
    const [loading, setLoading] = useState(false);
    const {t} = useTranslation();

    useEffect(() => {
        if (localStorage.getItem("bucket") !== null) {
            const str = localStorage.getItem("bucket")
            if(!str) return;
            const array = JSON.parse(str);
            setBucket(array);
        } else {
            console.log("dupa duap")
            setBucket([{name:"testlek", price:5, categoryName:"przeciwbolowe", quantity:5}, {name:"testlek1", price:5, categoryName:"przeciwbolowe", quantity:5}, {name:"testlek2", price:5, categoryName:"przeciwbolowe", quantity:5}]);
        }
    }, [localStorage])

    const handleChange = async (medicationName, quantity) => {
        let temp_to_change = bucket.find(({name}) => name === medicationName);
        console.log(temp_to_change)

        temp_to_change.quantity = quantity;

        console.log(bucket)
        localStorage.setItem("bucket", JSON.stringify(bucket))
        window.location.reload();
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

    return (
        <div style={{
            display: "flex",
            justifyContent: "center",
            alignContent: "center",
            flexDirection: "column"
        }}>
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
                                            if (e.target.value <= 0) {
                                                e.target.value = "0";
                                            }
                                            if (e.target.value === "01" || e.target.value === "02" || e.target.value === "03" || e.target.value === "04" || e.target.value === "05" || e.target.value === "06" || e.target.value === "07" || e.target.value === "08" || e.target.value === "09") {
                                                e.target.value = "0";
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

                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <ToastContainer/>
            </TableContainer>
        </div>
    );
}
