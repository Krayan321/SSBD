import { useCallback, useEffect, useState } from "react";
import {
  changeSelfAccountPassword,
  grantAdminRole,
  grantPateintRole,
  grantChemistRole,
  getAccounts,
  getSelfAccountDetails,
  signInAccount,
} from "../api/mok/accountApi";
import { useNavigate } from "react-router-dom";
import axios from "axios";

export function useAccount() {
  const [account, setAccount] = useState();
  const [accountRole, setAccountRole] = useState();
  const navigate = useNavigate();

  const logInClient = useCallback(
    async (username, password) => {
      const credentials = {
        username: username,
        password: password,
      };

      try {
        const response = await axios.post(
          "http://localhost:8080/api/auth/login",
          credentials
        );
        if (response[1] === 200) {
          const account = response[0];
          setAccount(account);
          setAccount(response[0]);
          navigate("/");
          window.location.reload(false);
          return true;
        } else {
          return false;
        }
      } catch (error) {
        console.log(error);
        return false;
      }
    },
    [navigate]
  );

  const getSelf = useCallback(async () => {
    const response = await getSelfAccountDetails();

    // if (response[1] === 200) {
    //   setAccount(response[0]);
    // } else {
    //   //przenieś na stronę logowania
    // }
  }, []);

  const getAllAccounts = useCallback(async () => {
    const response = await getAccounts();

    if (response[1] === 200) {
      return response[0].map((client) => ({
        username: client.username.username,
        email: client.email.email,
        role: client.role,
      }));
    }
  }, []);

  const changePassword = useCallback(async (password) => {
    const response = await changeSelfAccountPassword(password);

    return response[1] === 200;
  }, []);

  useEffect(() => {
    getSelf();
  }, [getSelf]);

  useEffect(() => {
    if (account !== undefined) {
      setAccountRole(account.role);
    }
  }, [account]);

  return {
    account,
    logInClient,
    //getSelf,
    changePassword,
    getAllAccounts,
  };
}
