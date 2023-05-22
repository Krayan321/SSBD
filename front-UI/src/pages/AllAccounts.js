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
import { Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import LockIcon from "@mui/icons-material/Lock";
import LockOpenIcon from "@mui/icons-material/LockOpen";

export default function AllAccounts() {
  const [accounts, setAccounts] = useState([]);
  const navigate = useNavigate();

  const findAccounts = useCallback(async () => {
    let response = await getAccounts();

    if (response.status === 200) {
      setAccounts(response.data);
      console.log(response.data);
    } else {
      console.log("Nie udało się pobrać kont");
    }
  }, []);

  useEffect(() => {
    findAccounts();
  }, [findAccounts]);

  const handleAccountDetails = async (accountId) => {
    const id = accountId;
    const accountToUpdate = [...accounts];
    const indexOfAccountToEdit = accountToUpdate.findIndex(
      (account) => account.id === accountId
    );
    if (indexOfAccountToEdit !== -1) {
      setAccounts(accountToUpdate);
    }
    navigate(`/accounts/${id}/details`);
  };

  const handleAccountBlock = async (active, id) => {
    const updatedAccount = accounts.map((account) =>
      account.id === id ? { ...account, active: !active } : account
    );
    if (active) {
      await blockAccount(id);
    }
    if (!active) {
      await unblockAccount(id);
    }
    setAccounts(updatedAccount);
  };

  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>Account login</TableCell>
            <TableCell align="right">Email</TableCell>
            <TableCell align="right">Confimed</TableCell>
            <TableCell align="right">Active</TableCell>
            <TableCell align="right">Details</TableCell>
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
                <Button onClick={() => handleAccountBlock(row.active, row.id)}>
                  {row.active ? <LockOpenIcon /> : <LockIcon />}
                </Button>
              </TableCell>
              <TableCell align="right">
                <Button onClick={() => handleAccountDetails(row.id)}>
                  Details
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
