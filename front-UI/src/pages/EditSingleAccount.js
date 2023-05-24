import { yupResolver } from "@hookform/resolvers/yup";
import { Box, Button, CircularProgress, Paper, TextField } from "@mui/material";
import { default as React, useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router-dom";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import * as Yup from "yup";
import { editOtherAdminData, editOtherChemistData, editOtherPatientData, getAccountDetails, getAdminData, getChemistData, getPatientData } from "../api/mok/accountApi";
import ConfirmationDialog from "../components/ConfirmationDialog";
const paperStyle = {
  backgroundColor: "rgba(255, 255, 255, 0.75)",
  padding: "20px 20px",
  margin: "0px auto",
  width: 400,
};

const patientSchema = Yup.object().shape({
  name: Yup.string()
      .min(2, 'first_name_length_min')
      .max(50, 'first_name_length_max')
      .required('first_name_required'),
  lastName: Yup.string()
      .min(2, 'last_name_lenght_min')
      .max(50, 'last_name_length_max')
      .required('last_name_required'),
  phoneNumber: Yup.string()
      .min(9, 'phone_number_length')
      .max(9, 'phone_number_length')
      .matches(/^[0-9]+$/, "phone_number_only_digits")
      .required('phone_number_required'),
  pesel: Yup.string()
      .min(11, 'pesel_length')
      .max(11, 'pesel_length')
      .matches(/^[0-9]+$/, "pesel_only_digits")
      .required('pesel_required'),
  nip: Yup.string()
      .min(10, 'nip_length')
      .matches(/^[0-9]+$/, "nip_only_digits")
      .max(10, 'nip_length')
      .required('nip_required')
});

const chemistSchema = Yup.object().shape({
 
  licenceNumber: Yup.string()
      .matches(
          /^\d{6}$/,
          "licence_number_invalid"
      )
      .required('licence_number_required'),
});

const adminSchema = Yup.object().shape({
 
  workPhoneNumber: Yup.string()
      .matches(
          /^\d{9}$/,
          "work_phone_number_invalid"
      )
      .required('work_phone_number_required'),
});
function EditSingleAccount() {

  const [dialogOpen, setDialogOpen] = useState(false);
  const [etag, setEtag] = useState('')
  const [name, setName] = useState('');
  const [lastName, setLastName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [pesel, setPesel] = useState('');
  const [nip, setNip] = useState('');
  const [workPhoneNumber, setWorkPhoneNumber] = useState('')
  const [licenseNumber, setLicenseNumber] = useState('')
  const [loading, setLoading] = useState(false)
  const { id } = useParams();
  const [account, setAccount] = useState({});
  const [accessLevels, setAccessLevels] = useState([]);
  const { t } = useTranslation();
  const navigate = useNavigate();
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await getAccountDetails(id);
        setAccount(response.data);
       
       
        setAccessLevels(response.data.accessLevels[0].role);
        if (response.data.accessLevels[0].role.includes("PATIENT")){
          const etag_response = await getPatientData(id) 
          setEtag(etag_response.headers["etag"])
          
        }
        if (response.data.accessLevels[0].role.includes("CHEMIST")){
          const etag_response = await getChemistData(id) 
          setEtag(etag_response.headers["etag"])
         
        }
        if (response.data.accessLevels[0].role.includes("ADMIN")){
          const etag_response = await getAdminData(id) 
          setEtag(etag_response.headers["etag"])
         
        }
      
      } catch (error) {
        console.error(error);
      }
    };

    fetchData();
  }, []);

  const isAdmin = accessLevels.includes("ADMIN");
  const isChemist = accessLevels.includes("CHEMIST");
  const isPatient = accessLevels.includes("PATIENT");

 
  let selectedSchema;
  if (isAdmin) {
    selectedSchema = adminSchema;
  } else if (isChemist) {
    selectedSchema = chemistSchema;
  } else {
    selectedSchema = patientSchema;
  }
  const {
    register,
    handleSubmit,
    formState: {errors},
} = useForm({
    resolver: yupResolver(selectedSchema),
});
  const handleEditPatient = () =>{
    setLoading((true))
    setDialogOpen(false)
    const tag = etag.split('"').join("")
    if (isPatient){
    const body = {
        id: id,
        role: accessLevels,
        firstName: name,
        lastName: lastName,
        phoneNumber: phoneNumber,
        pesel: pesel,
        nip: nip,
        version: account.accessLevels[0].version
      };

      editOtherPatientData(id, body, tag).then((res)=>{
        setLoading((state)=> !state)
        navigate(`/accounts/${id}/details`);
        toast.success(t("success"), {
            position: "top-center",
        })
      }).catch(error => {
        setLoading((state)=> !state)

            if (error.response.status === 500) {
                toast.error(t("server_error"), {
                    position: "top-center",
                })
            } else   {
                toast.error(t(error.response.data.message), {
                    position: "top-center",
                })
            } 
        })
      }else if (isChemist){
        const body = {
          id: id,
          licenseNumber: licenseNumber,
          active: account.active,
          role: accessLevels,
          version: account.accessLevels[0].version
        }

        editOtherChemistData(id, body, tag).then((res)=>{
          setLoading((state)=> !state)
          navigate(`/accounts/${id}/details`);
          toast.success(t("success"), {
              position: "top-center",
          })
        }).catch(error => {
          setLoading((state)=> !state)
  
              if (error.response.status === 500) {
                  toast.error(t("server_error"), {
                      position: "top-center",
                  })
              } else   {
                  toast.error(t(error.response.data.message), {
                      position: "top-center",
                  })
              } 
          })

      }else{
        const body = {
          id: id,
          workPhoneNumber: workPhoneNumber,
          active: account.active,
          role: accessLevels,
          version: account.accessLevels[0].version
        }

        editOtherAdminData(id, body, tag).then((res)=>{
          setLoading((state)=> !state)
          navigate(`/accounts/${id}/details`);
          toast.success(t("success"), {
              position: "top-center",
          })
        }).catch(error => {
          setLoading((state)=> !state)
  
              if (error.response.status === 500) {
                  toast.error(t("server_error"), {
                      position: "top-center",
                  })
              } else   {
                  toast.error(t(error.response.data.message), {
                      position: "top-center",
                  })
              } 
          })
      }
  }

  const proceed = (data) =>{
    if (isAdmin) {
      console.log("admin")
      setWorkPhoneNumber(data.workPhoneNumber)
    } else if (isChemist) {
      console.log("chemist")
      setLicenseNumber(data.licenceNumber)
    } else {
      console.log("else")
      setName(data.name);
      setLastName(data.lastName);
      setPhoneNumber(data.phoneNumber);
      setPesel(data.pesel);
      setNip(data.nip);

    }
    setDialogOpen(true)
}
  return <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignContent: "center",
        marginTop: "3rem",
      }}
    >
      <Paper elevation={20} style={paperStyle}>
       <h2 style={{ fontFamily: "Lato" }}>{t("edit_account_details")} </h2>


      {isAdmin && (
          <Box sx={{marginBottom: 4}}>
             <TextField
                            {...register("workPhoneNumber")}
                            type="text"
                            variant='outlined'
                            color='secondary'
                            label={t("work_phone_number")}
                            fullWidth
                            required
                            error={errors.workPhoneNumber}
                            helperText={t(errors.workPhoneNumber?.message)}
                            defaultValue={account.accessLevels[0].workPhoneNumber}
                        />
                        <Box sx={{ marginBottom: 2 }} />

                        {
                loading ? <CircularProgress style={{marginRight: "auto", marginLeft: "auto"}}/> :
                <Button fullWidth
                onClick={handleSubmit(proceed)} type='submit' variant='contained'>{t("edit_data")}</Button>
              }
          </Box>
        )}
        {isChemist && (
          <Box sx={{marginBottom: 4}}>
           <TextField
                            {...register("licenceNumber")}
                            type="text"
                            variant='outlined'
                            color='secondary'
                            label={t("licesne_number")}
                            fullWidth
                            required
                            error={errors.licenceNumber}
                            helperText={t(errors.licenceNumber?.message)}
                            defaultValue={account.accessLevels[0].licenseNumber}
                        />
                        <Box sx={{ marginBottom: 2 }} />

                        {
                loading ? <CircularProgress style={{marginRight: "auto", marginLeft: "auto"}}/> :
                <Button fullWidth
                onClick={handleSubmit(proceed)} type='submit' variant='contained'>{t("edit_data")}</Button>
              }
          </Box>
        )}
        {isPatient && (
          <Box  sx={{marginBottom: 4}}>
            <form>
            <TextField
                            {...register("name")}
                            type="text"
                            variant='outlined'
                            color='secondary'
                            label={t("name")}
                            fullWidth
                            required
                            error={errors.name}
                            helperText={t(errors.name?.message)}
                            defaultValue={account.accessLevels[0].firstName}
                        />
            <Box sx={{ marginBottom: 2 }} />
            <TextField
                            {...register("lastName")}
                            type="text"
                            variant='outlined'
                            color='secondary'
                            label={t("last_name")}
                            fullWidth
                            required
                            error={errors.last_name}
                            helperText={t(errors.last_name?.message)}
                            defaultValue={account.accessLevels[0].lastName}
                        />
                        <Box sx={{ marginBottom: 2 }} />
              <TextField
                            {...register("phoneNumber")}
                            type="text"
                            variant='outlined'
                            color='secondary'
                            label={t("phone_number")}
                            fullWidth
                            required
                            error={errors.phone_number}
                            helperText={t(errors.phone_number?.message)}
                            defaultValue={account.accessLevels[0].phoneNumber}
                        />
                        <Box sx={{ marginBottom: 2 }} />
             <TextField
                            {...register("pesel")}
                            type="text"
                            variant='outlined'
                            color='secondary'
                            label={t("Pesel")}
                            fullWidth
                            required
                            error={errors.pesel}
                            helperText={t(errors.pesel?.message)}
                            defaultValue={account.accessLevels[0].pesel}
                        />
                        <Box sx={{ marginBottom: 2 }} />
             <TextField
                            {...register("nip")}
                            type="text"
                            variant='outlined'
                            color='secondary'
                            label={t("NIP")}
                            fullWidth
                            required
                            error={errors.nip}
                            helperText={t(errors.pesel?.nip)}
                            defaultValue={account.accessLevels[0].nip}
                        />

                        <Box sx={{ marginBottom: 4 }} />


              {
                loading ? <CircularProgress style={{marginRight: "auto", marginLeft: "auto"}}/> :
                <Button fullWidth
                onClick={handleSubmit(proceed)} type='submit' variant='contained'>{t("edit_data")}</Button>
              }
              </form>

              </Box>
        )}
        <ConfirmationDialog
        open={dialogOpen}
        title={t("confirm_edit_account_data")}
        actions={[
          {label: t("proceed"), handler:handleEditPatient, color: 'primary'},
          {label: t("cancel"), handler: () => setDialogOpen(false), color: 'secondary'},
        ]}
        onClose={() => setDialogOpen(false)}
        />
        <ToastContainer/>
        </Paper>
        </div>
}

export default EditSingleAccount;
