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

export async function createOrder() {
    return await put("order/${id}/submit")
}

export async function deleteWaitingOrdersById(id) {
    return await del(`order/${id}/waiting`);
}
