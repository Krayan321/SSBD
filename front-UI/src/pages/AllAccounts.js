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
  Button,
  Dialog,
  DialogTitle,
  DialogActions,
  DialogContent,
  DialogContentText,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import LockIcon from "@mui/icons-material/Lock";
import LockOpenIcon from "@mui/icons-material/LockOpen";
import { useTranslation } from "react-i18next";
import { toast, ToastContainer } from "react-toastify";

export default function AllAccounts() {
  const [accounts, setAccounts] = useState([]);
  const navigate = useNavigate();

  const [dialogStates, setDialogStates] = useState({});

  // const [open, setOpen] = useState(false);
  // const [selectedId, setSelectedId] = useState(null);

  const { t } = useTranslation();

  const findAccounts = useCallback(async () => {
    let response = await getAccounts();

    if (response.status === 200) {
      setAccounts(response.data);
      console.log(response.data);
    } else {
      return <div>Nothing to show</div>;
    }
  }, []);

  useEffect(() => {
    findAccounts();
  }, [findAccounts]);

  const handleAccountDetails = async (accountId) => {
    const id = accountId;
    navigate(`/accounts/${id}/details`);
  };

  const handleAccountBlock = async (active, accountId) => {
    if (accountId) {
      const updatedAccount = accounts.map((account) =>
        account.id === accountId ? { ...account, active: !active } : account
      );
      setAccounts(updatedAccount);
      if (active) {
        await blockAccount(accountId)
          .then(() => {
            toast.success(t("account_blocked"), {
              position: "top-center",
            });
          })
          .catch((error) => {
            if (error.response.status === 401) {
              toast.error(t("account_blocked_error"), {
                position: "top-center",
              });
            } else if (error.response.status === 404) {
              toast.error(t("account_blocked_error"), {
                position: "top-center",
              });
            } else if (error.response.status === 500) {
              toast.error(t("account_blocked_error"), {
                position: "top-center",
              });
            }
          });
      } else {
        await unblockAccount(accountId)
          .then(() => {
            toast.success(t("account_unblocked"), {
              position: "top-center",
            });
          })
          .catch((error) => {
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
          });
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
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
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
                          onClick={() => handleAccountBlock(row.active, row.id)}
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
                          onClick={() => handleAccountBlock(row.active, row.id)}
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
  );
}
