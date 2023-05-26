import React, { useCallback, useEffect, useState } from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import axios from "axios";
import {
  getAccounts,
  blockAccount,
  unblockAccount,
} from "../api/mok/accountApi";
import {
  Box,
  Button,
  Dialog,
  DialogTitle,
  DialogActions,
  DialogContent,
  DialogContentText,
  IconButton,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import LockIcon from "@mui/icons-material/Lock";
import LockOpenIcon from "@mui/icons-material/LockOpen";
import { useTranslation } from "react-i18next";
import { toast, ToastContainer } from "react-toastify";
import RefreshIcon from "@mui/icons-material/Refresh";

export default function AllAccounts() {
  const [accounts, setAccounts] = useState([]);
  const navigate = useNavigate();

  const [dialogStates, setDialogStates] = useState({});
  const [refreshing, setRefreshing] = useState(false);

  // const [open, setOpen] = useState(false);
  // const [selectedId, setSelectedId] = useState(null);

  const { t } = useTranslation();

  const findAccounts = useCallback(async () => {
    setRefreshing(true);
    let response = await getAccounts();
    setRefreshing(false);
    if (response.status === 200) {
      setAccounts(response.data);
      console.log(response.data);
    } else {
      navigate("/error", { replace: true });
    }
  }, []);

  useEffect(() => {
    findAccounts();
  }, [findAccounts]);

  const handleAccountDetails = async (accountId) => {
    const id = accountId;
    navigate(`/accounts/${id}/details`);
  };

  const handleRefresh = () => {
    findAccounts();
  };

  const handleAccountBlock = async (active, accountId) => {
    if (accountId) {
      const updatedAccount = accounts.map((account) =>
        account.id === accountId ? { ...account, active: !active } : account
      );
      setAccounts(updatedAccount);
      if (active) {
        try {
          await blockAccount(accountId);
          toast.success(t("account_blocked"), { position: "top-center" });
        } catch (error) {
          if (error.response.status === 401) {
            toast.error(t("account_blocked_error"), { position: "top-center" });
          } else if (error.response.status === 404) {
            toast.error(t("account_blocked_error"), { position: "top-center" });
          } else if (error.response.status === 500) {
            toast.error(t("account_blocked_error"), { position: "top-center" });
          }
        }
      } else {
        try {
          await unblockAccount(accountId);
          toast.success(t("account_unblocked"), { position: "top-center" });
        } catch (error) {
          if (error.response.status === 401) {
            toast.error(t("account_unblocked_error"), {
              position: "top-center",
            });
          } else if (error.response.status === 403) {
            toast.error(t("account_unblocked_error"), {
              position: "top-center",
            });
          } else if (error.response.status === 500) {
            toast.error(t("account_unblocked_error"), {
              position: "top-center",
            });
          }
        }
      }
    }
    setDialogStates((prevState) => ({
      ...prevState,
      [accountId]: false,
    }));
  };

  const handleCancel = (accountId) => {
    setDialogStates((prevState) => ({
      ...prevState,
      [accountId]: false,
    }));
  };

  const handleClick = (accountId) => {
    setDialogStates((prevState) => ({
      ...prevState,
      [accountId]: true,
    }));
  };

  return (
    <div>
      <Box sx={{ marginBottom: "10px", textAlign: "center" }}>
        <IconButton
          variant="contained"
          onClick={handleRefresh}
          disabled={refreshing}
        >
          <RefreshIcon />
        </IconButton>
      </Box>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} aria-label="simple table">
          <TableHead sx={{ bgcolor: "pink" }}>
            <TableRow>
              <TableCell>Login</TableCell>
              <TableCell align="right">Email</TableCell>
              <TableCell align="right">{t("confirmed")}</TableCell>
              <TableCell align="right">{t("active")}</TableCell>
              <TableCell align="right">{t("details")}</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {accounts.map((row) => (
              <TableRow
                key={row.id}
                sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
              >
                <TableCell component="th" scope="row">
                  {row.login}
                </TableCell>
                <TableCell align="right">{row.email}</TableCell>
                <TableCell align="right">{String(row.confirmed)}</TableCell>
                <TableCell align="right">
                  {String(row.active)}
                  <Button onClick={() => handleClick(row.id)}>
                    {row.active ? <LockOpenIcon /> : <LockIcon />}
                  </Button>
                  <Dialog
                    open={dialogStates[row.id] || false}
                    onClose={() => handleCancel(row.id)}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                  >
                    {row.active ? (
                      <div>
                        <DialogTitle id="alert-dialog-title">
                          {t("block_account?")}
                        </DialogTitle>
                        <DialogContent>
                          <DialogContentText id="alert-dialog-description">
                            {t("block_account_description")}
                          </DialogContentText>
                        </DialogContent>
                        <DialogActions>
                          <Button onClick={() => handleCancel(row.id)}>
                            {t("close")}
                          </Button>
                          <Button
                            onClick={() =>
                              handleAccountBlock(row.active, row.id)
                            }
                            autoFocus
                          >
                            {t("block")}
                          </Button>
                        </DialogActions>
                      </div>
                    ) : (
                      <div>
                        <DialogTitle id="alert-dialog-title">
                          {t("unblock_account?")}
                        </DialogTitle>
                        <DialogContent>
                          <DialogContentText id="alert-dialog-description">
                            {t("unblock_account_description")}
                          </DialogContentText>
                        </DialogContent>
                        <DialogActions>
                          <Button onClick={() => handleCancel(row.id)}>
                            {t("close")}
                          </Button>
                          <Button
                            onClick={() =>
                              handleAccountBlock(row.active, row.id)
                            }
                            autoFocus
                          >
                            {t("unblock")}
                          </Button>
                        </DialogActions>
                      </div>
                    )}
                  </Dialog>
                </TableCell>
                <TableCell align="right">
                  <Button onClick={() => handleAccountDetails(row.id)}>
                    {t("details")}
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
        <ToastContainer />
      </TableContainer>
    </div>
  );
}
