import {
  Box,
  Button,
  Grid,
  Paper,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import LinearProgress from "@mui/material/LinearProgress";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router-dom";
import { getAccountDetails } from "../api/mok/accountApi";
import AddRoleForm from "../modules/accounts/AddRoleForm";
import ChangeOtherEmailForm from "../modules/accounts/ChangeOtherEmailForm";
import ChangeOtherPasswordForm from "../modules/accounts/ChangeOtherPasswordForm";

function SingleAccount() {
  const { id } = useParams();
  const [account, setAccount] = useState({});
  const [accessLevels, setAccessLevels] = useState([]);
  const [addRole, setAddRole] = useState(false);
  const { t } = useTranslation();
  const [changePass, setChangePass] = useState(false);
  const [changeEmail, setChangeEmail] = useState(false);
  const [etag, setEtag] = useState("");
  const navigate = useNavigate();
  const paperStyle = {
    backgroundColor: "rgba(255, 255, 255, 0.75)",
    padding: "20px 20px",
    margin: "0px auto",
    width: 400,
  };
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      const response = await getAccountDetails(id);
      if (response.status === 200) {
        setAccount(response.data);
        setAccessLevels(response.data.accessLevels);
        setEtag(response.headers["etag"]);
        setLoading(false);
      } else {
        navigate("/error", { replace: true });
      }
    };

    fetchData();
  }, []);

  const isAdmin =
    accessLevels && accessLevels.some((level) => level.role === "ADMIN");
  const isChemist =
    accessLevels && accessLevels.some((level) => level.role === "CHEMIST");
  const isPatient =
    accessLevels && accessLevels.some((level) => level.role === "PATIENT");

  if (loading) {
    return (
      <div>
        <LinearProgress />
      </div>
    );
  }

  const handleChangePassword = () => {
    setChangePass((state) => !state);
  };

  const handleEditAccountDetails = () => {
    navigate(`/accounts/edit/${id}`);
  };

  const handleAddRole = () => {
    setAddRole((state) => !state);
  };

  const handleChangeEmail = () => {
    setChangeEmail((state) => !state);
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
        {
          changeEmail ? (
              <>
                <ChangeOtherEmailForm account={account} etag={etag} hideChange={setChangeEmail}/>
                <Grid item xs={6}>
                  <Button onClick={handleChangeEmail}>{t("back_button")}</Button>
                </Grid>
              </>
          ) : (
              <Grid container spacing={2}>
                <Grid item xs={6}>
                  <Button onClick={handleChangeEmail}>{t("change_email")}</Button>
                </Grid>
              </Grid>

          )
        }
        {changePass ? (
          <>
            <ChangeOtherPasswordForm
              account={account}
              etag={etag}
              hideChange={setChangePass}
            />
            <Grid item xs={6}>
              <Button onClick={handleChangePassword}>{t("back_button")}</Button>
            </Grid>
          </>
        ) : (
          <Grid container spacing={2}>

            <Grid item xs={6}>
              <Button onClick={handleChangePassword}>
                {t("change_password_button")}
              </Button>
            </Grid>
          </Grid>
        )}
        {addRole ? (
          <>
            <AddRoleForm
              account={account}
              etag={etag}
              hideChange={setAddRole}
            />
            <Grid item xs={6}>
              <Button onClick={handleAddRole}>{t("back_button")}</Button>
            </Grid>
          </>
        ) : (
          <Grid container spacing={2}>
            <Grid item xs={6}>
              <Button onClick={handleAddRole}>{t("add_role")}</Button>
            </Grid>
          </Grid>
        )}

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
          {accessLevels && accessLevels.map((level) => level.role).join(", ")}
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
            {accessLevels.map((level) =>
              level.workPhoneNumber ? (
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
                  {String(level.workPhoneNumber)}
                </Typography>
              ) : null
            )}
          </Box>
        )}
        {isChemist && (
          <Box>
            {accessLevels.map((level) =>
              level.licenseNumber ? (
                <Box>
                  <Typography
                    style={{
                      fontFamily: "Lato",
                      fontSize: 18,
                      fontWeight: 400,
                    }}
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
                    {String(level.licenseNumber)}
                  </Typography>
                </Box>
              ) : null
            )}
          </Box>
        )}
        {isPatient && (
          <Box>
            {accessLevels.map((level) =>
              level.name ? (
                <Box>
                  <Typography
                    style={{
                      fontFamily: "Lato",
                      fontSize: 18,
                      fontWeight: 400,
                    }}
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
                    {String(level.name)}
                  </Typography>
                </Box>
              ) : null
            )}
            {accessLevels.map((level) =>
              level.lastName ? (
                <Box>
                  <Typography
                    style={{
                      fontFamily: "Lato",
                      fontSize: 18,
                      fontWeight: 400,
                    }}
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
                    {String(level.lastName)}
                  </Typography>
                </Box>
              ) : null
            )}
            {accessLevels.map((level) =>
              level.phoneNumber ? (
                <Box>
                  <Typography
                    style={{
                      fontFamily: "Lato",
                      fontSize: 18,
                      fontWeight: 400,
                    }}
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
                    {String(level.phoneNumber)}
                  </Typography>
                </Box>
              ) : null
            )}
            {accessLevels.map((level) =>
              level.pesel ? (
                <Box>
                  <Typography
                    style={{
                      fontFamily: "Lato",
                      fontSize: 18,
                      fontWeight: 400,
                    }}
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
                    {String(level.pesel)}
                  </Typography>
                </Box>
              ) : null
            )}
            {accessLevels.map((level) =>
              level.NIP ? (
                <Box>
                  <Typography
                    style={{
                      fontFamily: "Lato",
                      fontSize: 18,
                      fontWeight: 400,
                    }}
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
                    {String(level.NIP)}
                  </Typography>
                </Box>
              ) : null
            )}
          </Box>
        )}
        <Button onClick={handleEditAccountDetails}>
          {t("edit_account_details")}
        </Button>
      </Paper>
    </div>
  );
}

export default SingleAccount;
