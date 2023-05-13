import axios from "axios";
const BASE_URL = "http://localhost:8080/api/account";


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
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        withCredentials: true,
      });
      return response.status;
    } catch (error) {
      console.error(error);
    }
};

export async function get(stringUrl, params) {
    const url = new URL(stringUrl, BASE_URL);
    if (params) {
      url.search = new URLSearchParams(params).toString();
    }
  
    try {
      const response = await axios.get(url, {
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        withCredentials: true,
      });
      return response.status;
    } catch (error) {
      console.error(error);
    }
};

export async function post(stringUrl, body) {
    const url = new URL(stringUrl, BASE_URL);
  
    try {
      const response = await axios.post(url, body, {
        headers: defaultHeaders,
        withCredentials: false,
      });
      return response.status;
    } catch (error) {
      console.error(error);
    }
};

export async function put(stringUrl, body) {
    const url = new URL(stringUrl, BASE_URL);
  
    try {
      const response = await axios.put(url, body, {
        headers: defaultHeaders,
        withCredentials: false,
      });
      return response.status;
    } catch (error) {
      console.error(error);
    }
};

export async function del(stringUrl) {
    const url = new URL(stringUrl, BASE_URL);
  
    try {
      const response = await axios.delete(url, {
        headers: defaultHeaders,
        withCredentials: true,
      });
      return response.status;
    } catch (error) {
      console.error(error);
    }
};


