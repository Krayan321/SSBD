import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { toast } from "react-toastify";
import {
  Button,
  CircularProgress,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import { useTranslation } from "react-i18next";
import "react-toastify/dist/ReactToastify.css";

import {
  grantAdminRole,
  grantChemistRole,
  grantPatientRole,
} from "../../api/mok/accountApi";
import ConfirmationDialog from "../../components/ConfirmationDialog";
import { useNavigate } from "react-router-dom";

function AddRoleForm({ account, etag, hideChange }) {
  const [dialogOpen, setDialogOpen] = useState(false);
  const navigate = useNavigate();
  const { t } = useTranslation();
  const [loading, setLoading] = useState(false);
  const [selectedRole, setSelectedRole] = useState("");
  const [showAdditionalForm, setShowAdditionalForm] = useState(false);
  const { handleSubmit, register } = useForm();

  const handleRoleChange = (event) => {
    setSelectedRole(event.target.value);
    setShowAdditionalForm(true);
  };

  const handleAdditionalFormSubmit = (data) => {
    setLoading(true);
    const tag = typeof etag === "string" ? etag.split('"').join("") : etag;
    let body = {};

    if (selectedRole === "admin") {
      body = {
        login: account.login,
        workPhoneNumber: data.workPhoneNumber,
        version: account.version,
      };
      grantAdminRole(account.id, body, tag)
        .then((res) => {
          setLoading(false);
          hideChange(false);
          navigate("/accounts");
        })
        .catch((error) => {
          setLoading(false);
          toast.error(t(error.response.data.message));
        });
    } else if (selectedRole === "chemist") {
      body = {
        login: account.login,
        licenseNumber: data.licenseNumber,
        version: account.version,
      };
      grantChemistRole(account.id, body, tag)
        .then((res) => {
          setLoading(false);
          hideChange(false);
          navigate("/accounts");
        })
        .catch((error) => {
          setLoading(false);
          toast.error(t(error.response.data.message));
        });
    } else if (selectedRole === "patient") {
      body = {
        login: account.login,
        name: data.name,
        lastName: data.lastName,
        phoneNumber: data.phoneNumber,
        pesel: data.pesel,
        NIP: data.NIP,
        version: account.version,
      };
      grantPatientRole(account.id, body, tag)
        .then((res) => {
          setLoading(false);
          hideChange(false);
          navigate("/accounts");
        })
        .catch((error) => {
          setLoading(false);
          toast.error(t(error.response.data.message));
        });
    }
  };

  const handleReset = async () => {
    setLoading(true);
    const tag = typeof etag === "string" ? etag.split('"').join("") : etag;
    const bodyAdmin = {
      login: account.login,
      workPhoneNumber: "",
      version: account.version,
    };
    const bodyChemist = {
      login: account.login,
      licenseNumber: "",
      version: account.version,
    };
    const bodyPatient = {
      login: account.login,
      name: "",
      lastName: "",
      phoneNumber: "",
      pesel: "",
      NIP: "",
      version: account.version,
    };

    Promise.all([
      grantAdminRole(account.id, bodyAdmin, tag),
      grantChemistRole(account.id, bodyChemist, tag),
      grantPatientRole(account.id, bodyPatient, tag),
    ])
      .then((res) => {
        setLoading(false);
        hideChange(false);
        navigate("/accounts");
      })
      .catch((error) => {
        setLoading(false);
        toast.error(t(error.response.data.message));
      });
  };

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignContent: "center",
        marginTop: "3rem",
      }}
    >
      <form onSubmit={handleSubmit(handleAdditionalFormSubmit)}>
        <FormControl variant="outlined" fullWidth>
          <InputLabel>Select Role</InputLabel>
          <Select
            value={selectedRole}
            onChange={handleRoleChange}
            label="Select Role"
          >
            <MenuItem value="admin">Admin</MenuItem>
            <MenuItem value="chemist">Chemist</MenuItem>
            <MenuItem value="patient">Patient</MenuItem>
          </Select>
        </FormControl>

        {showAdditionalForm && selectedRole === "admin" && (
          <>
            <TextField
              label="Work Phone Number"
              variant="outlined"
              fullWidth
              margin="normal"
              {...register("workPhoneNumber")}
            />
          </>
        )}

        {showAdditionalForm && selectedRole === "chemist" && (
          <>
            <TextField
              label="License Number"
              variant="outlined"
              fullWidth
              margin="normal"
              {...register("licenseNumber")}
            />
          </>
        )}

        {showAdditionalForm && selectedRole === "patient" && (
          <>
            <TextField
              label="Name"
              variant="outlined"
              fullWidth
              margin="normal"
              {...register("name")}
            />
            <TextField
              label="Last Name"
              variant="outlined"
              fullWidth
              margin="normal"
              {...register("lastName")}
            />
            <TextField
              label="Phone Number"
              variant="outlined"
              fullWidth
              margin="normal"
              {...register("phoneNumber")}
            />
            <TextField
              label="PESEL"
              variant="outlined"
              fullWidth
              margin="normal"
              {...register("pesel")}
            />
            <TextField
              label="NIP"
              variant="outlined"
              fullWidth
              margin="normal"
              {...register("NIP")}
            />
          </>
        )}

        {showAdditionalForm && (
          <Button
            type="submit"
            variant="contained"
            color="primary"
            disabled={loading}
          >
            {loading ? <CircularProgress size={24} /> : "Submit"}
          </Button>
        )}
      </form>

      <ConfirmationDialog
        open={dialogOpen}
        title={t("confirm_change_password")}
        actions={[
          { label: t("proceed"), handler: handleReset, color: "primary" },
          {
            label: t("cancel"),
            handler: () => setDialogOpen(false),
            color: "secondary",
          },
        ]}
        onClose={() => setDialogOpen(false)}
      />
    </div>
  );
}

export default AddRoleForm;
