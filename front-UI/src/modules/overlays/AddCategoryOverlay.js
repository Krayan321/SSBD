import React from "react";
import Overlay from "../../components/Overlay";
import { Button } from "@mui/material";
import { useTranslation } from "react-i18next";
import { Close } from "@mui/icons-material";

export default function AddCategoryOverlay({ open, onClose }) {
  const { t } = useTranslation();
  return (
    <Overlay open={open} onClose={onClose}>
      <Button onClick={onClose}>
        <Close />
        {t("close")}
      </Button>
      <div>AddCategoryOverlay</div>
    </Overlay>
  );
}
