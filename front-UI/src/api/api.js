import axios from "axios";
import { JWT_TOKEN } from "../constants/Constants";

const BASE_URL = window.location.href.includes("lodz.pl")
  ? "https://team-1.proj-sum.it.p.lodz.pl/api/account"
  : "http://localhost:8080/api/account";

const defaultHeaders = {
  "Content-Type": "application/json",
  Accept: "application/json",
};

export async function getNoResponse(stringUrl, params) {
  const url = new URL(stringUrl, BASE_URL);
  if (params) {
    url.search = new URLSearchParams(params).toString();
  }

  try {
    const response = await axios.get(url, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem(JWT_TOKEN),
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      withCredentials: true,
    });
    return response;
  } catch (error) {
    console.error(error);
  }
}

export async function get(stringUrl, params) {
  const url = new URL(stringUrl, BASE_URL);
  if (params) {
    url.search = new URLSearchParams(params).toString();
  }

  try {
    const response = await axios.get(url, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem(JWT_TOKEN),
        "Content-Type": "application/json",
        Accept: "application/json",
      },
    });
    return response;
  } catch (error) {
    console.error(error);
  }
}

export async function post(stringUrl, body) {
  const url = new URL(stringUrl, BASE_URL);

  // try {
  const response = await axios.post(url, body, {
    headers: defaultHeaders,
    withCredentials: true,
  });
  return response;
  // } catch (error) {
  //   console.error(error);
  // }
}

export async function put(stringUrl, body) {
  const url = new URL(stringUrl, BASE_URL);

  // try {
  const response = await axios.put(url, body, {
    headers: {
      Authorization: "Bearer " + localStorage.getItem(JWT_TOKEN),
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    withCredentials: true,
  });
  return response;
  // } catch (error) {
  //   console.error(error);
  // }
}

export async function putWithEtag(stringUrl, body, etag) {
  const url = new URL(stringUrl, BASE_URL);

  // try {
  const response = await axios.put(url, body, {
    headers: {
      Authorization: "Bearer " + localStorage.getItem(JWT_TOKEN),
      "Content-Type": "application/json",
      Accept: "application/json",
      "If-Match": etag,
    },
  });
  return response;
  // } catch (error) {
  //   console.error(error);
  // }
}

export async function postWithEtag(stringUrl, body, etag) {
  const url = new URL(stringUrl, BASE_URL);

  // try {
  const response = await axios.put(url, body, {
    headers: {
      Authorization: "Bearer " + localStorage.getItem(JWT_TOKEN),
      "Content-Type": "application/json",
      Accept: "application/json",
      "If-Match": etag,
    },
  });
  return response;
  // } catch (error) {
  //   console.error(error);
  // }
}

export async function del(stringUrl) {
  const url = new URL(stringUrl, BASE_URL);

  try {
    const response = await axios.delete(url, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem(JWT_TOKEN),
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      withCredentials: true,
    });
    return response;
  } catch (error) {
    console.error(error);
  }
}
