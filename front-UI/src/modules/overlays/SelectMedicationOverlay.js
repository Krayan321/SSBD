import Overlay from "../../components/Overlay";
import {Button, Grid, TextField} from "@mui/material";
import {Close} from "@mui/icons-material";
import {useTranslation} from "react-i18next";
import React, {useEffect, useState} from "react";
import {useFieldArray, useForm} from "react-hook-form";
import * as Yup from "yup";
import {yupResolver} from "@hookform/resolvers/yup";

function SelectMedicationOverlay({ open, onClose, medications, append }) {

    const searchSchema = Yup.object().shape({
        name: Yup.string()
    });

    const {
        register,
        control,
        handleSubmit,
        formState: {errors},
    } = useForm({resolver: yupResolver(searchSchema)});

    const {t} = useTranslation();

    let filteredMedications = [];
    let prevFilter = medications;
    const [maxShown, setMaxShown] = useState(2)

    useEffect(() => {
        filter('');
        console.log(medications);
    })

    const filter = function(phrase) {
        if(Array.isArray(medications)) {
            if(phrase == '') {
                filteredMedications = medications;
            } else {
                filteredMedications = medications.filter(medication =>
                    medication.name.startsWith(phrase) && !medication.chosen)
            }
        }
    }

    const onChange = function(event) {
        filter(event.target.value);
    }

    const select = function(medication) {
        const found = medications.find(med => med.name === medication.name);
        found.chosen = true;
        append({id: medication.id, name: medication.name, quantity: ''});
        onClose();
    }


    return (
        <Overlay open={open}>
            <Grid container spacing={1} sx={{mb: 2}}>
                <Grid container spacing={1}>
                    <Grid item xs={10}>
                        <h2>{t("select_medication")}</h2>
                    </Grid>
                    <Grid item xs={2}>
                        <Button onClick={onClose}><Close/>{t("close")}</Button>
                    </Grid>
                </Grid>
                <Grid item xs={12}>
                    <TextField type="text" variant='outlined'
                               color='secondary' label={t("medication_name")}
                               fullWidth onChange={e => onChange(e)}/>
                </Grid>
                {filteredMedications.map((med) => (
                    <Grid item xs={12}>
                        <Button fullWidth variant='outlined'
                                onClick={() => select(med)}>{med.name}</Button>
                    </Grid>
                ))}
                <Grid item xs={12}>
                    <Button fullWidth variant='outlined'
                            onClick={() => setMaxShown(maxShown + 2)}>
                        {t("show_more")}</Button>
                </Grid>
            </Grid>
        </Overlay>
    );
}

export default SelectMedicationOverlay;