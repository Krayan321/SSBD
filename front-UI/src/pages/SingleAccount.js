import React, { useEffect } from "react";
import { getAccountDetails } from "../api/mok/accountApi";
import { useParams } from "react-router-dom";
import { useCallback, useState } from "react";
import { Box, Typography } from "@mui/material";


function SingleAccount() {
  const { id } = useParams();
  const [account, setAccount] = useState("");
  const findAccount = useCallback(async () => {
  let response = await getAccountDetails(id);
  if (response.status === 200) {
      console.log(response.data);
      const aaa = JSON.stringify(response.data);
      setAccount(aaa);
    } else {
      console.log('Nie udało się pobrać konta');
    }

  console.log(account)
  }, []);

  useEffect(() => {
    findAccount();
  }, [findAccount]);

  // return (
  //   <Box>
  //     <Typography variant="h4" component="h4" gutterBottom>
  //       Account details
  //     </Typography>
  //     <Typography variant="h6" component="h6" gutterBottom>
  //       Actice: {account.active}
  //     </Typography>
  //     <Typography variant="h6" component="h6" gutterBottom>
  //       Confirmed: {account.confirmed}
  //     </Typography>
  //     <Typography variant="h6" component="h6" gutterBottom>
  //       Email: {account.email}
  //     </Typography>
  //     <Typography variant="h6" component="h6" gutterBottom>
  //       Login: {account.login}
  //     </Typography>
  //     <Typography variant="h6" component="h6" gutterBottom>
  //       Access level: {account}
  //     </Typography>

      
  //   </Box>

  // )
}

export default SingleAccount;
