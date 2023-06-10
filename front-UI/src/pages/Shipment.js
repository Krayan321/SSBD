import {useFieldArray, useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import React, {useCallback, useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import {addChemist} from "../api/mok/accountApi";
import {toast, ToastContainer} from "react-toastify";
import {Pathnames} from "../router/Pathnames";
import {Button, CircularProgress, Grid, Paper, TextField} from "@mui/material";
import List from "@mui/icons-material/List";
import VisibilityIcon from "@mui/icons-material/Visibility";
import * as Yup from "yup";
import {Add, Close} from "@mui/icons-material";
import SelectMedicationOverlay from "../modules/overlays/SelectMedicationOverlay";
import AddMedicationOverlay from "../modules/overlays/AddMedicationOverlay";
import {getAllMedications} from "../api/moa/medicationApi";
import {createShipment} from "../api/moa/shipmentApi";
import {DatePicker} from "@mui/x-date-pickers";
import dayjs from "dayjs";

export function Shipment() {
    const {t} = useTranslation();

    const addShipmentSchema = Yup.object().shape({
        orderMedications: Yup.array().of(
            Yup.object().shape({
                id: Yup.string(),
                name: Yup.string(),
                quantity: Yup.string()
                    .min(1, t("quantity_min"))
            })
        ).min(1, "at least one shipment"),
    });

    const {
        register,
        control,
        handleSubmit,
        formState: {errors},
    } = useForm({resolver: yupResolver(addShipmentSchema)});

    const paperStyle = {padding: '20px 20px', margin: "0px auto", width: "80%"}
    const [loading, setLoading] = useState(false);
    const [selectMedication, setSelectMedication] = useState(false);
    const [createMedication, setCreateMedication] = useState(false);
    const [shipmentDate, setShipmentDate] = useState(dayjs());
    const [medications, setMedications] = useState(false);
    const {fields, append, remove} = useFieldArray({ name: 'orderMedications', control });
    const navigate = useNavigate();

    const findAllMedications = useCallback(async () => {
        setLoading(true);
        getAllMedications().then((response) => {
            setLoading(false)
            setMedications(response.data);
        }).catch((error) => {
            setLoading(false)
            toast.error(t(error.response.data.message), {position: "top-center"});
        });
    }, []);

    useEffect(() => {
        findAllMedications()
    }, [findAllMedications]);

    const onSubmit = function(data) {
        const body = {
            shipmentDate: shipmentDate.format('YYYY-MM-DDTHH:mm:ss.SSS'),
            shipmentMedications: []}
        data.orderMedications.forEach((om) => {
            body.shipmentMedications.push({
                quantity: Math.floor(om.quantity),
                medication: {id: om.id}});
        })
        console.log(body);
        createShipment(body).then(
            () => {
                setLoading(false)
                toast.success(t("shipment_created"), {position: "top-center"})
                navigate(Pathnames.auth.landing);
            }
        ).catch(error => {
            setLoading(false)
            toast.error(t(error.response.data.message), {position: "top-center"})
        })
    }

    return (
        <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignContent: 'center',
            marginTop: '2rem'
        }}>
            <Paper elevation={10} style={paperStyle}>
                <h2 style={{fontFamily: 'Lato'}}>{t("shipment")}</h2>
                <form onSubmit={handleSubmit(onSubmit)}>
                    <Grid container spacing={1} sx={{mb: 2}}>
                        <Grid item xs={6}>
                            <Button fullWidth variant="outlined" color="success"
                                    onClick={() => setSelectMedication(true)}>
                                <List/>{t("select_medication")}</Button>
                        </Grid>
                        <Grid item xs={6}>
                            <Button fullWidth variant="outlined" color="secondary"
                                    onClick={() => setCreateMedication(true)}>
                                <Add/>{t("create_medication")}</Button>
                        </Grid>
                    </Grid>
                    <Grid item xs={12} sx={{mb: 2}}>
                        <DatePicker
                            label={t("shipment_date")}
                            value={shipmentDate}
                            onChange={(v) => setShipmentDate(v)}
                            format="DD/MM/YYYY"
                            slotProps={{ textField: { fullWidth: true } }}
                        />
                    </Grid>
                    {fields.map((om, i) => (
                        <Grid container spacing={1} sx={{mb: 2}} key={"om-" + i}>
                            <Grid item xs={6}>
                                <TextField type="text" variant='standard'
                                           color='secondary' label={t("name")}
                                           fullWidth InputProps={{readOnly: true}}
                                           size="small"
                                           {...register(`orderMedications.${i}.name`)}/>
                            </Grid>
                            <Grid item xs={5}>
                                <TextField type="text" variant='outlined'
                                           color='secondary' label={t("medication_quantity")}
                                           fullWidth required
                                           error={errors.orderMedications?.[i]?.quantity}
                                           helperText={t(errors.orderMedications?.[i]?.quantity?.message)}
                                           size="small"
                                           {...register(`orderMedications.${i}.quantity`)}/>
                            </Grid>
                            <Grid item xs={1}>
                                <Button fullWidth size="large"
                                        onClick={() => {
                                            const found = medications.find(med => med.name === om.name);
                                            found.chosen = false;
                                            remove(i)
                                        }}
                                        variant='outlined' color="error"><Close/></Button>
                            </Grid>
                        </Grid>
                    ))}


                    {
                        loading ? <CircularProgress style={{marginRight: "auto", marginLeft: "auto"}}/> :
                            <Button fullWidth type='submit' variant='contained' sx={{mt: 2}}>{t("submit")}</Button>
                    }
                </form>
            </Paper>
            <ToastContainer/>
            <SelectMedicationOverlay open={selectMedication} medications={medications}
                                     append={append}
                                     onClose={() => setSelectMedication(false)}/>
            <AddMedicationOverlay open={createMedication}
                                  onClose={() => setCreateMedication(false)}
                                  append={append}/>
        </div>
    )
}