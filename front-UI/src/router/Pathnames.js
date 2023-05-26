export const Pathnames = {
    public: {
        login: '/login',
        signup: '/sign-up',
        home: '/',
        confirmAccount: '/confirm-account/:token',
        resetPassword: '/reset-password',
        setResetPassword: '/new-password',
    },
    auth: {
        landing: '/home',
        self: '/home/self',
    },
    admin: {
        accounts: '/accounts',
        editSingleAccount: '/accounts/:id/edit',
        editChemist: '/accounts/:id/edit/chemist',
        details: '/accounts/:id/details',
        account: '/accounts/:id',
        createAccount: '/accounts/create-account',
        addPatient: "/accounts/add-patient",
        addChemist: "/accounts/add-chemist",
        addAdministrator: "/accounts/add-administrator",
    },
    patient: {

    },
    chemist: {

    }
}