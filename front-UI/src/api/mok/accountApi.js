import {post, put, get} from "../api";

export async function signUpAccount(name, lastName, login, email, password, phoneNumber, pesel, nip) {
    const body = {
        name: name,
        lastName: lastName,
        login: login,
        email: email,
        password: password,
        phoneNumber: phoneNumber,
        pesel: pesel,
        nip: nip
    };

    return await post("account/register", body);
}

export async function signInAccount(login, password) {
    const body = {
        login: login,
        password: password
    };

    return await post("api/account/login", body);
}

export async function getAccount(id) {
    return await get(`api/account/${id}`);
}

export async function putPatient(id, name, lastName, login, email, phoneNumber, pesel, nip) {
    const body = {
        id: id,
        name: name,
        lastName: lastName,
        login: login,
        email: email,
        phoneNumber: phoneNumber,
        pesel: pesel,
        nip: nip
    };

    return await put("api/account/{id}/patient", body);
}

export async function putChemist(id, licenseNumber) {
    const body = {
        id: id,
        licenseNumber: licenseNumber
    };

    return await put("api/account/{id}/chemist", body);
}

export async function putAdmin(id, workPhoneNumber) {
    const body = {
        id: id,
        workPhoneNumber: workPhoneNumber
    };

    return await put("api/account/{id}/admin", body);
}

