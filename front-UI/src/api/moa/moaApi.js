import {get} from "../api";


export async function getSelfOrders() {
    return await get("order/self");
}