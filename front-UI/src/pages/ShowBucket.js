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
    Skeleton, useTheme
} from "@mui/material";
import {ToastContainer} from "react-toastify";
import {useTranslation} from "react-i18next";


export default function ShowBucket() {
    const [bucket, setBucket] = useState([]);
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
            setBucket([]);
        }
    }, [localStorage])

    useEffect(() => {
        if(bucket){
            localStorage.setItem("bucket", bucket.toString())
        }
    }, [bucket])


    if (loading) {
        return (
            <TableContainer component={Paper}>
                <Table>
                    <TableHead sx={{backgroundColor: theme.palette.primary.main}}>
                        <TableRow>
                            <TableCell sx={{color: "white"}}>Name</TableCell>
                            <TableCell sx={{color: "white"}} align="right">Price</TableCell>
                            <TableCell sx={{color: "white"}} align="right">Category</TableCell>
                            <TableCell sx={{color: "white"}} align="right">Quantity</TableCell>
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
                            <TableCell sx={{color: "white"}}>Name</TableCell>
                            <TableCell sx={{color: "white"}} align="right">Price</TableCell>
                            <TableCell sx={{color: "white"}} align="right">Category</TableCell>
                            <TableCell sx={{color: "white"}} align="right">Quantity</TableCell>
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
                                <TableCell align="right">{row.price}</TableCell>
                                <TableCell align="right">{row.categoryName}</TableCell>
                                <TableCell align="right">{row.quantity}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <ToastContainer/>
            </TableContainer>
        </div>
    );
}
