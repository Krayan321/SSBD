import { get, post, put } from "../api";

export async function getAllCategories() {
  return await get("category");
}
