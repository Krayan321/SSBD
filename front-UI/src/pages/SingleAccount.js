import { getAccountDetails } from "../api/mok/accountApi";
import { useParams } from "react-router-dom";
import { Grid, Box, Paper, Typography, TextField, Button } from "@mui/material";
import { useCallback, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Stack } from "@mui/material";

function SingleAccount() {
  const { id } = useParams();
  const [account, setAccount] = useState({});
  const [accessLevels, setAccessLevels] = useState([]);
  const { t } = useTranslation();

  const paperStyle = {
    backgroundColor: "rgba(255, 255, 255, 0.75)",
    padding: "20px 20px",
    margin: "0px auto",
    width: 400,
  };
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await getAccountDetails(id);
        setAccount(response.data);
        setAccessLevels(response.data.accessLevels[0].role);
        setLoading(false);
      } catch (error) {
        console.error(error);
      }
    };

    fetchData();
  }, []);

  // const fetchAccount = useCallback(async () => {
  //   const response = await getAccountDetails(id);
  //   setAccount(response.data);
  //   setAccessLevels(response.data.accessLevels[0].role)
  // }
  // , [id]);

  // useEffect(() => {
  //   fetchAccount();
  // }
  // , [fetchAccount]);

  const isAdmin = accessLevels.includes("ADMIN");
  const isChemist = accessLevels.includes("CHEMIST");
  const isPatient = accessLevels.includes("PATIENT");

  console.log(account);
  console.log("isAdmin: " + isAdmin);
  console.log("isChemist: " + isChemist);
  console.log("isPatient: " + isPatient);

  if (loading) {
    return <p>Loading...</p>;
  }

  const DataDisplay = ({ label, data }) => {
    return (
      <Grid container spacing={2}>
        <Grid item xs={6}>
          <Typography variant="subtitle1" color="textSecondary">
            {label}
          </Typography>
        </Grid>
        <Grid item xs={6}>
          <Typography variant="subtitle1">{data}</Typography>
        </Grid>
      </Grid>
    );
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
      <Paper elevation={20} style={paperStyle}>
        <h2 style={{ fontFamily: "Lato" }}>{t("account_details")} </h2>
        <Stack spacing={2} direction="row" sx={{ marginBottom: 4 }}>
          <TextField
            variant="outlined"
            color="secondary"
            value={account.confirmed}
            label={t("confirmed")}
            fullWidth
            InputProps={{
              readOnly: true,
            }}
          />
          <TextField
            type="text"
            variant="outlined"
            color="secondary"
            value={account.active}
            label={t("active")}
            fullWidth
            InputProps={{
              readOnly: true,
            }}
          />
        </Stack>
        <Typography
          style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
          variant="h6"
          component="div"
          sx={{ flexGrow: 1, mb: 1 }}
          type="text"
          fullWidth
          InputProps={{
            readOnly: true,
          }}
        >
          Email
        </Typography>
        <Typography
          style={{ fontSize: 16 }}
          variant="h6"
          component="div"
          sx={{ flexGrow: 1, mb: 2 }}
          type="text"
          fullWidth
          InputProps={{
            readOnly: true,
          }}
        >
          {account.email}
        </Typography>
        <Grid container spacing={2}>
          <Grid item xs={6}>
            <Button>Edit Email</Button>
          </Grid>
          <Grid item xs={6}>
            <Button>Change Password</Button>
          </Grid>
        </Grid>
        <Typography
          style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
          variant="h6"
          component="div"
          sx={{ flexGrow: 1, mb: 1, mt: 1 }}
          type="text"
          fullWidth
          InputProps={{
            readOnly: true,
          }}
        >
          Login
        </Typography>
        <Typography
          style={{ fontSize: 16 }}
          variant="h6"
          component="div"
          sx={{ flexGrow: 1, mb: 2 }}
          type="text"
          fullWidth
          InputProps={{
            readOnly: true,
          }}
        >
          {account.login}
        </Typography>
        <Typography
          style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
          variant="h6"
          component="div"
          sx={{ flexGrow: 1, mb: 1 }}
          type="text"
          fullWidth
          InputProps={{
            readOnly: true,
          }}
        >
          {t("access_level")}
        </Typography>
        <Typography
          style={{ fontSize: 16 }}
          variant="h6"
          component="div"
          sx={{ flexGrow: 1, mb: 2 }}
          type="text"
          fullWidth
          InputProps={{
            readOnly: true,
          }}
        >
          {account.accessLevels[0].role}
        </Typography>
        {isAdmin && (
          <Box>
            <Typography
              style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
              component="div"
              sx={{ flexGrow: 1, mb: 1 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              {t("work_phone_number")}
            </Typography>
            <Typography
              style={{ fontSize: 16 }}
              variant="h6"
              component="div"
              sx={{ flexGrow: 1, mb: 2 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              {String(account.accessLevels[0].workPhoneNumber)}
            </Typography>
          </Box>
        )}
        {isChemist && (
          <Box>
            <Typography
              style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
              component="div"
              sx={{ flexGrow: 1, mb: 1 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              {t("licesne_number")}
            </Typography>
            <Typography
              style={{ fontSize: 16 }}
              variant="h6"
              component="div"
              sx={{ flexGrow: 1, mb: 2 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              {String(account.accessLevels[0].licenseNumber)}
            </Typography>
          </Box>
        )}
        {isPatient && (
          <Box>
            <Typography
              style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
              component="div"
              sx={{ flexGrow: 1, mb: 1 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              {t("name")}
            </Typography>
            <Typography
              style={{ fontSize: 16 }}
              variant="h6"
              component="div"
              sx={{ flexGrow: 1, mb: 2 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              {String(account.accessLevels[0].name)}
            </Typography>
            <Typography
              style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
              component="div"
              sx={{ flexGrow: 1, mb: 1 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              {t("last_name")}
            </Typography>
            <Typography
              style={{ fontSize: 16 }}
              variant="h6"
              component="div"
              sx={{ flexGrow: 1, mb: 2 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              {String(account.accessLevels[0].lastName)}
            </Typography>
            <Typography
              style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
              component="div"
              sx={{ flexGrow: 1, mb: 1 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              {t("phone_number")}
            </Typography>
            <Typography
              style={{ fontSize: 16 }}
              variant="h6"
              component="div"
              sx={{ flexGrow: 1, mb: 2 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              {String(account.accessLevels[0].phoneNumber)}
            </Typography>
            <Typography
              style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
              component="div"
              sx={{ flexGrow: 1, mb: 1 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              Pesel
            </Typography>
            <Typography
              style={{ fontSize: 16 }}
              variant="h6"
              component="div"
              sx={{ flexGrow: 1, mb: 2 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              {String(account.accessLevels[0].pesel)}
            </Typography>
            <Typography
              style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
              component="div"
              sx={{ flexGrow: 1, mb: 1 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              NIP
            </Typography>
            <Typography
              style={{ fontSize: 16 }}
              variant="h6"
              component="div"
              sx={{ flexGrow: 1, mb: 2 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              {String(account.accessLevels[0].NIP)}
            </Typography>
          </Box>
        )}
        <Button>Edit Account Details</Button>
      </Paper>
    </div>
  );
}

export default SingleAccount;
