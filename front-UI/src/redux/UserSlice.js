import {createSlice} from "@reduxjs/toolkit";

const initialState = {
    sub: "",
    roles: [],
    cur: "",
    exp: "",
};

const ACCESS_LEVEL = "currentAccessLevel";
const JWT_TOKEN = "jwtToken";

export const userSlice = createSlice({
    name: "user",
    initialState,
    reducers: {
        logout: () => {
            localStorage.removeItem(ACCESS_LEVEL);
            localStorage.removeItem(JWT_TOKEN);
            return initialState;
        },
        login: (state, action) => {
            const data = action.payload;

            if (
                !localStorage.getItem(ACCESS_LEVEL) ||
                localStorage.getItem(ACCESS_LEVEL) === ""
            ) {
                const res = {
                    sub: data.sub,
                    roles: data.roles.split(","),
                    cur: data.roles.split(",")[0],
                    exp: data.exp,
                };
                localStorage.setItem(ACCESS_LEVEL, res.cur);
                return { ...state, ...res };
            } else {
                const res = {
                    sub: data.sub,
                    roles: data.roles.split(","),
                    cur: localStorage.getItem(ACCESS_LEVEL),
                    exp: data.exp,
                };
                return { ...state, ...res };
            }
        },
        changeLevel: (state, action) => {
            const data = action.payload;
            const res = {
                sub: data.sub,
                roles: data.roles,
                cur: data.roles[data.index],
                exp: data.exp,
            };
            localStorage.setItem(ACCESS_LEVEL, res.cur);
            return { ...state, ...res };
        },
    },
});

export const { login, logout, changeLevel } = userSlice.actions;
export default userSlice.reducer;