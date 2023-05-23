import { get, post, put, putWithEtag } from "../api";

export async function getAccounts() {
  return await get("account");
}

export async function getAccount(id) {
  return await get(`account/${id}`);
}

export async function confirmAccount(token) {

  return await post(`account/confirm/${token}`);
}

export async function confirmEmailChange(token) {
  const body = {
    token: token,
  };
  return await post("account/confirm-email-change", body);
}

export async function getSelfAccountDetails() {
  return await get("account/details");
}

export async function getAccountDetails(id) {
  return await get(`account/${id}/details`);
}

export async function signUpAccount(
  name,
  lastName,
  login,
  email,
  password,
  phoneNumber,
  pesel,
  nip
) {
  const body = {
    name: name,
    lastName: lastName,
    login: login,
    email: email,
    password: password,
    phoneNumber: phoneNumber,
    pesel: pesel,
    nip: nip,
  };

  return await post("account/register", body);
}

export async function blockAccount(id) {
  return await put(`account/${id}/block`);
}

export async function unblockAccount(id) {
  return await put(`account/${id}/unblock`);
}

export async function activateAccount(id) {
  return await put(`account/${id}/activate`);
}

export async function changeAccountPassword(id, body, etag) {

  return await putWithEtag(`account/${id}/change-user-password`, body, etag);
}

export async function changeSelfAccountPassword(body, etag) {


  return await putWithEtag("account/change-password", body, etag);
}

///implementacja change email

export async function resetPassword(email) {
  const body = {
    email: email,
  };

  return await put("account/reset-password", body);
}

export async function setResetPassword(token, newPassword) {
  const body = {
    token: token,
    newPassword: newPassword,
  };
  return await post("account/new-password", body);
}

export async function setNewPassword(token, password) {
  const body = {
    token: token,
    password: password,
  };

  return await post("account/set-new-password", body);
}

//dodanie pacjenta, aptekarza, admina

export async function addPatient(body) {
  return await post("account/patient", body);
}

export async function addChemist(body) {
  return await post("account/chemist", body);
}

export async function addAdmin(body) {
  return await post("account/admin", body);
}

//nadanie roli pacjenta, aptekarza, admina

export async function grantPatientRole(id) {
  return await put(`account/${id}/patient`);
}

export async function grantChemistRole(id) {
  return await put(`account/${id}/chemist`);
}

export async function grantAdminRole(id) {
  return await put(`account/${id}/admin`);
}

//pobranie danych pacjenta, aptekarza, admina

export async function getPatientData(id) {
  return await get(`account/${id}/patient`);
}

export async function getChemistData(id) {
  return await get(`account/${id}/chemist`);
}

export async function getAdminData(id) {
  return await get(`account/${id}/admin`);
}

//edycja danych pacjenta, aptekarza, admina

export async function editPatientData(id, body) {
  return await put(`account/${id}/patient`, body);
}

export async function editChemistData(id, body) {
  return await put(`account/${id}/chemist`, body);
}

export async function editAdminData(id, body) {
  return await put(`account/${id}/admin`, body);
}

//zarządzanie blokadą ról

export async function blockRoleAdmin(id) {
  return await put(`account/${id}/admin/block`);
}

export async function blockRoleChemist(id) {
  return await put(`account/${id}/chemist/block`);
}

export async function blockRolePatient(id) {
  return await put(`account/${id}/patient/block`);
}

export async function unblockRoleAdmin(id) {
  return await put(`account/${id}/admin/unblock`);
}

export async function unblockRoleChemist(id) {
  return await put(`account/${id}/chemist/unblock`);
}

export async function unblockRolePatient(id) {
  return await put(`account/${id}/patient/unblock`);
}

//login

export async function signInAccount(login, password) {
  const body = {
    login: login,
    password: password,
  };

  return await post("auth/login", body);
}

//na razie inne register

export async function putPatient(
  id,
  name,
  lastName,
  login,
  email,
  phoneNumber,
  pesel,
  nip
) {
  const body = {
    id: id,
    name: name,
    lastName: lastName,
    login: login,
    email: email,
    phoneNumber: phoneNumber,
    pesel: pesel,
    nip: nip,
  };

  return await put("account/{id}/patient", body);
}

//tymczasowa edycja danych

export async function putChemist(id, licenseNumber) {
  const body = {
    id: id,
    licenseNumber: licenseNumber,
  };

  return await put("account/{id}/chemist", body);
}

export async function putAdmin(id, workPhoneNumber) {
  const body = {
    id: id,
    workPhoneNumber: workPhoneNumber,
  };

  return await put("account/{id}/admin", body);
}
