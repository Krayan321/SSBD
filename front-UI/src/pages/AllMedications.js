import React, {useCallback, useEffect, useState} from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import {getAllMedications} from "../api/moa/medicationApi";
import {Box, Button, IconButton, Skeleton, useTheme} from "@mui/material";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {toast, ToastContainer} from "react-toastify";
import RefreshIcon from "@mui/icons-material/Refresh";
import ShoppingBasketIcon from "@mui/icons-material/ShoppingBasket";
import ConfirmationDialog from "../components/ConfirmationDialog";
import {Pathnames} from "../router/Pathnames";

export default function AllMedications() {
    const [medications, setMedications] = useState([]);
    const navigate = useNavigate();
    const theme = useTheme();
    const [loading, setLoading] = useState(false);
    const [refreshing, setRefreshing] = useState(false);
    const [bucket, setBucket] = useState([]);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [itemToAdd, setItemToAdd] = useState(null);

    const {t} = useTranslation();

    const isMedicationInBucket = (medication) => {
        return bucket.some((item) => item.name === medication.name && item.categoryName === medication.categoryDTO.name);
    };

    const handleAddToBucket = async (name, price, categoryName, id) => {
        const toAdd = {name: name, price: price, categoryName: categoryName, quantity: 1, id: id};
        setItemToAdd(toAdd);
        setDialogOpen(true);
    };

    const handleConfirmation = (accepted) => {
        if (accepted) {
            let flag = false;
            for (let o of bucket) {
                if (o.name === itemToAdd.name) {
                    flag = true;
                }
            }
            if (!flag) {
                bucket.push(itemToAdd);
                localStorage.setItem("bucket", JSON.stringify(bucket))
            }
            toast.success(t("medication_added_to_basket"), {position: "top-center"});
        }
        setItemToAdd(null);
        setDialogOpen(false);

    };

    const findMedications = useCallback(async () => {
        setLoading(true);
        setRefreshing(true);
        if (localStorage.getItem("bucket") !== null) {
            const str = localStorage.getItem("bucket");
            if (!str) return;
            const array = JSON.parse(str);
            setBucket(array);
        } else {
            setBucket([]);
        }
        getAllMedications()
            .then((response) => {
                setLoading(false);
                setMedications(response.data);
                console.log(response.data);
            })
            .catch((error) => {
                setLoading(false);
                toast.error(t(error.response.data.message), {position: "top-center"});
            });
        setRefreshing(false);
    }, []);

    useEffect(() => {
        findMedications();
    }, [findMedications]);

    const handleMedicationDetails = async (medicationId) => {
        const id = medicationId;
        navigate(`/medications/${id}`);
    };

    const handleRefresh = () => {
        findMedications();
    };

    if (loading) {
        return (
            <TableContainer component={Paper}>
                <Table>
                    <TableHead sx={{backgroundColor: theme.palette.primary.main}}>
                        <TableRow>
                            <TableCell sx={{color: "white"}}>{t("name")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">
                                {t("price")}
                            </TableCell>
                            <TableCell sx={{color: "white"}} align="right">
                                {t("stock")}
                            </TableCell>
                            <TableCell sx={{color: "white"}} align="right">
                                {t("category_name")}
                            </TableCell>
                            <TableCell sx={{color: "white"}} align="right">
                                {t("on_prescription")}
                            </TableCell>
                            <TableCell sx={{color: "white"}} align="right">
                                {t("details")}
                            </TableCell>
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
        <div
            style={{
                display: "flex",
                justifyContent: "center",
                alignContent: "center",
                flexDirection: "column",
            }}
        >
            <Box sx={{marginBottom: "10px", textAlign: "center"}}>
                <IconButton
                    variant="contained"
                    onClick={handleRefresh}
                    disabled={refreshing}
                >
                    <RefreshIcon/>
                </IconButton>
            </Box>
            <TableContainer
                sx={{maxWidth: "800px", margin: "auto"}}
                component={Paper}
            >
                <Table aria-label="simple table">
                    <TableHead sx={{backgroundColor: theme.palette.primary.main}}>
                        <TableRow sx={{color: "white"}}>
                            <TableCell sx={{color: "white"}}>{t("name")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">
                                {t("price")}
                            </TableCell>
                            <TableCell sx={{color: "white"}} align="right">
                                {t("stock")}
                            </TableCell>
                            {/* <TableCell sx={{ color: "white" }} align="right">
                {t("category_name")}
              </TableCell> */}
                            <TableCell sx={{color: "white"}} align="right">
                                {t("on_prescription")}
                            </TableCell>
                            <TableCell sx={{color: "white"}} align="right">
                                {t("details")}
                            </TableCell>
                            <TableCell sx={{color: "white"}} align="right"></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {medications.map((row) => (
                            <TableRow
                                key={row.id}
                                sx={{"&:last-child td, &:last-child th": {border: 0}}}
                            >
                                <TableCell component="th" scope="row">
                                    {row.name}
                                </TableCell>
                                <TableCell align="right">{row.currentPrice}</TableCell>
                                <TableCell align="right">{String(row.stock)}</TableCell>
                                {/* <TableCell align="right">
                  {String(row.categoryDTO.name)}
                </TableCell> */}
                                <TableCell align="right">
                                    {row.categoryDTO.isOnPrescription ? t("yes") : t("no")}
                                </TableCell>
                                <TableCell align="right">
                                    <Button onClick={() => handleMedicationDetails(row.id)}>
                                        {t("details")}
                                    </Button>
                                </TableCell>
                                <TableCell align="right">
                                    {!isMedicationInBucket(row) ? (
                                        <Button
                                            onClick={() => handleAddToBucket(row.name, row.currentPrice, row.categoryDTO.name, row.id)}>
                                            {t('dodaj')}
                                        </Button>
                                    ) : (
                                        <>
                                            <IconButton onClick={() => navigate(Pathnames.patient.showBucket)}
                                                        color="inherit">
                                                <ShoppingBasketIcon/>
                                            </IconButton>
                                        </>
                                    )}
                                </TableCell>

                            </TableRow>
                        ))}
                    </TableBody>
                    <ConfirmationDialog
                        open={dialogOpen}
                        title={t("confirm_add_medication_to_basket")}
                        actions={[
                            {label: t("add"), handler: () => handleConfirmation(true), color: "primary"},
                            {label: t("cancel"), handler: () => handleConfirmation(false), color: "secondary"},
                        ]}
                        onClose={() => setDialogOpen(false)}
                    />
                </Table>
                <ToastContainer/>
            </TableContainer>
        </div>
    );
}
