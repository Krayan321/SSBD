import Overlay from "../../components/Overlay";
import {Button} from "@mui/material";
import {Close} from "@mui/icons-material";
import {useTranslation} from "react-i18next";

function SelectMedicationOverlay({ open, onClose, append }) {

    const {t} = useTranslation();

    return (
        <Overlay open={open}>
            <Button onClick={onClose}><Close/>{t("close")}</Button>
            <h2>{t("select_medication")}</h2>

        </Overlay>
    );
}

export default SelectMedicationOverlay;