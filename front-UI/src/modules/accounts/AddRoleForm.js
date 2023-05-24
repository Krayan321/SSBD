import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import {Button, CircularProgress, Paper, TextField,Radio, RadioGroup, FormControlLabel} from "@mui/material";
import { useTranslation } from 'react-i18next';
import 'react-toastify/dist/ReactToastify.css';

import { grantPatientRole, grantChemistRole, grantAdminRole } from '../../api/mok/accountApi';
import ConfirmationDialog from '../../components/ConfirmationDialog';

const AddRoleForm = ({ userId, currentRoles }) => {
    const [loading, setLoading] = useState(false);
    const { register, handleSubmit } = useForm();
    const { t } = useTranslation();
    const [dialogOpen, setDialogOpen] = useState(false);
    const [selectedRole, setSelectedRole] = useState('');
    const paperStyle = {padding: '30px 20px', margin: "auto", width: 400}

    const handleOpenDialog = (data) => {
        setSelectedRole(data.role);
        setDialogOpen(true);
    };

    const handleRoleChange = (event) => {
        setSelectedRole(event.target.value);
    };

    const handleAddRole = async () => {
        setLoading(true);
        let response;

        switch (selectedRole) {
            case 'PATIENT':
                response = await grantPatientRole(userId);
                break;
            case 'CHEMIST':
                response = await grantChemistRole(userId);
                break;
            case 'ADMIN':
                response = await grantAdminRole(userId);
                break;
            default:
                setLoading(false);
                return;
        }

        setLoading(false);
        setDialogOpen(false);

        if (response.success) {
            toast.success(t('role_added'));
        } else {
            toast.error(t('failed_to_add'));
        }
    };

    const onSubmit = (data) => {
        if (currentRoles.includes(data.role)) {
            toast.error(t('role_already_assigned'));
            return;
        }

        if (userId === data.userId) {
            toast.error(t('cant_assign_role_to_yourself'));
            return;
        }

        handleOpenDialog(data);
    };

    return (
        <div style={{ display: 'flex', justifyContent: 'center', alignContent: 'center', marginTop: '3rem' }}>
            <Paper elevation={20} style={paperStyle}>
                <h2 style={{ fontFamily: 'Lato' }}>{t("choose_role")}</h2>
            <form onSubmit={handleSubmit(onSubmit)}>
                <RadioGroup value={selectedRole} onChange={handleRoleChange}>
                    <FormControlLabel value="patient" control={<Radio />} label={t("patient")} />
                    <FormControlLabel value="chemist" control={<Radio />} label={t("chemist")} />
                    <FormControlLabel value="admin" control={<Radio />} label={t("admin")} />
                </RadioGroup>
                <Button fullWidth type="submit" variant="contained" disabled={loading}>
                    {t('add_role')}
                </Button>
            </form>

            <ConfirmationDialog
                open={dialogOpen}
                title={t('confirm_add_role')}
                actions={[
                    { label: t('proceed'), handler: handleAddRole, color: 'primary' },
                    { label: t('cancel'), handler: () => setDialogOpen(false), color: 'secondary' },
                ]}
                onClose={() => setDialogOpen(false)}
            />
            </Paper>
        </div>
    );
};

export default AddRoleForm;
