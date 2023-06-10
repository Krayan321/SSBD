import {get} from "../api";

export async function getAllMedications() {
    return await get("medication");
}