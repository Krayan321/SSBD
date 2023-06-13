import Overlay from "../../components/Overlay";
import { Button, TextField, Box, CircularProgress, Stack } from "@mui/material";
import { Close } from "@mui/icons-material";
import { useTranslation } from "react-i18next";
import React from "react";
import { useState } from "react";
import { addMedication } from "../../api/moa/medicationApi";
import { toast } from "react-toastify";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { medicationSchema } from "../../utils/Validations";

function AddMedicationOverlay({ open, onClose }) {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(medicationSchema),
  });

  const { t } = useTranslation();
  const [loading, setLoading] = useState(false);

  const onSubmit = handleSubmit(({ name, stock, price, categoryName }) => {
    setLoading(true);

    addMedication(name, stock, price, categoryName)
      .then(() => {
        setLoading(false);
        toast.success(t("medication_created"), {
          position: "top-center",
        });
      })
      .catch((error) => {
        setLoading(false);

        if (error.response.status === 400) {
          toast.error(t("invalid_medication_data"), {
            position: "top-center",
          });
        } else if (error.response.status === 409) {
          toast.error(t(error.response.data.message), {
            position: "top-center",
          });
        } else {
          toast.error(t("server_error"), {
            position: "top-center",
          });
        }
      });
  });

  return (
    <Overlay open={open}>
      <Button onClick={onClose}>
        <Close />
        {t("close")}
      </Button>
      <h2>{t("create_medication")}</h2>
      <Stack spacing={4}>
        <TextField
          {...register("name")}
          type="text"
          variant="outlined"
          color="secondary"
          label={t("name")}
          fullWidth
          required
          error={errors.name}
          helperText={t(errors.name?.message)}
        />
        <Stack spacing={2} direction="row">
          <TextField
            type="text"
            variant="outlined"
            color="secondary"
            label={t("stock")}
            fullWidth
            required
            error={errors.stock}
            helperText={t(errors.stock?.message)}
            {...register("stock")}
          />
          <TextField
            type="text"
            variant="outlined"
            color="secondary"
            label={t("price")}
            fullWidth
            required
            sx={{ mb: 4 }}
            error={errors.price}
            helperText={t(errors.price?.message)}
            {...register("price")}
          />
        </Stack>
        <TextField
          type="email"
          variant="outlined"
          color="secondary"
          label={t("category_name")}
          fullWidth
          required
          sx={{ mb: 4 }}
          error={errors.categoryName}
          helperText={t(errors.categoryName?.message)}
          {...register("categoryName")}
        />
        <Box
          sx={{ mb: 2 }}
          display="flex"
          justifyContent="center"
          alignItems="center"
        >
          {loading ? (
            <CircularProgress />
          ) : (
            <Button
              fullWidth
              onClick={handleSubmit(onSubmit)}
              type="submit"
              variant="contained"
            >
              {t("add_medication")}
            </Button>
          )}
        </Box>
      </Stack>
    </Overlay>
  );
}

export default AddMedicationOverlay;
