import {get} from "../api";


export async function getSelfOrders() {
    return await get("order/self");
}

export async function getWaitingOrders() {
    return await get("order/waiting");
}

export async function getOrdersToApprove() {
    return await get("order/to-approve");
}