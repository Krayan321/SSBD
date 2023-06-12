import {get, del} from "../api";


export async function getSelfOrders() {
    return await get("order/self");
}

export async function getWaitingOrders() {
    return await get("order/waiting");
}

export async function getOrdersToApprove() {
    return await get("order/to-approve");
}

export async function deleteWaitingOrdersById(id) {
    return await del(`order/${id}/waiting`);
}
