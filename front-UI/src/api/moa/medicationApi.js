import { get } from "../api";

export async function getAllMedications() {
  return await get("medication");
}

export async function getMedicationById(id) {
  return await get(`medication/${id}`);
}
