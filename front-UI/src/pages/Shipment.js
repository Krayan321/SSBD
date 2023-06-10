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

export function Shipment() {

    const addShipmentSchema = Yup.object().shape({
        orderMedications: Yup.array().of(
            Yup.object().shape({
                id: Yup.string(),
                name: Yup.string()
                    .required('Name is required'),
                quantity: Yup.number()
                    .min(1, "Quantity cannot be less than 0")
                    .integer("Must be an integer")
            })
        )
    });

    const {
        register,
        control,
        reset,
        handleSubmit,
        watch,
        formState: {errors},
    } = useForm({resolver: yupResolver(addShipmentSchema)});

    const paperStyle = {padding: '20px 20px', margin: "0px auto", width: "50%"}
    const {t} = useTranslation();
    const [loading, setLoading] = useState(false);
    const [selectMedication, setSelectMedication] = useState(false);
    const [createMedication, setCreateMedication] = useState(false);
    const [medications, setMedications] = useState(false);
    const { fields, append, remove } = useFieldArray({ name: 'orderMedications', control });

    const findAllMedications = useCallback(async () => {
        setLoading(true);
        getAllMedications().then((response) => {
            setLoading(false)
            setMedications(response.data);
            console.log(response.data);
        }).catch((error) => {
            setLoading(false)
            toast.error(t(error.response.data.message), {position: "top-center"});
        });
    }, []);

    useEffect(() => {
        findAllMedications()
    }, [findAllMedications]);



    const onSubmit = function() {

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
                <form>
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

                    {fields.map((om, i) => (
                        <Grid container spacing={1} sx={{mb: 2}}>
                            <Grid item xs={6}>
                                <TextField type="text" variant='standard'
                                           color='secondary' label={t("name")}
                                           fullWidth required
                                           InputProps={{readOnly: true,}}
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
                                <Button fullWidth fullHeitht size="large"
                                        onClick={() => remove(i)}
                                        variant='outlined' color="error"><Close/></Button>
                            </Grid>
                        </Grid>
                    ))}

                    <Button fullWidth
                            onClick={() => append({name: 'name', id: '1', quantity: ''})}
                            variant='contained'>add</Button>
                    {
                        loading ? <CircularProgress style={{marginRight: "auto", marginLeft: "auto"}}/> :
                            <Button fullWidth
                                    onClick={onSubmit} type='submit' variant='contained'>{t("submit")}</Button>
                    }
                </form>
            </Paper>
            <SelectMedicationOverlay open={selectMedication}
                                     onClose={() => setSelectMedication(false)}
                                     append={append}/>
            <AddMedicationOverlay open={createMedication}
                                  onClose={() => setCreateMedication(false)}
                                  append={append}/>
            <ToastContainer/>
        </div>



    )
}