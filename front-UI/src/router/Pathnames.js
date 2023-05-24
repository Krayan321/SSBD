export const Pathnames = {
    public: {
        login: '/login',
        signup: '/sign-up',
        home: '/',
        confirmAccount: '/confirm-account/:token',
        resetPassword: '/accounts/reset-password',
        setResetPassword: '/accounts/new-password',
    },
    auth: {
        landing: '/landing',
        self: '/accounts/self',
    },
    admin: {
        accounts: '/accounts',
        editSingleAccount: '/accounts/edit/:id',
        editChemist: '/edit-chemist/:id',
        details: '/accounts/:id/details',
        account: '/accounts/:id',
        createAccount: '/create-account',
        addPatient: "/add-patient",
        addChemist: "/add-chemist",
        addAdministrator: "/add-administrator",
    },
    patient: {

    },
    chemist: {

    }
}