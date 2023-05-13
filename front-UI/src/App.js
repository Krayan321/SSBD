import './App.css';
import Login from './pages/Login';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from './pages/Home';
import Navbar from './components/Navbar'
import SingUp from './pages/SignUp';
import AllAccounts from './pages/AllAccounts';
import SingleAccount from './pages/SingleAccount';
import AccountInfo from './pages/AccountInfo';
import EditSingleAccount from './pages/EditSingleAccount';
import React, {Suspense} from "react";
import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import LanguageDetector from 'i18next-browser-languagedetector';
import HttpApi from 'i18next-http-backend';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.js';
import LinearProgress from '@mui/material/LinearProgress';
import EditChemist from './pages/EditChemist';


i18n
  .use(initReactI18next) // passes i18n down to react-i18next
  .use(LanguageDetector)
  .use(HttpApi)
  .init({
    supportedLngs: ['en', 'pl'],
    backend: {
        loadPath: '/assets/locales/{{lng}}/translation.json',
    }, 
    fallbackLng: "en",
    detection: {
      order: ['htmlTag', 'cookie', 'localStorage', 'sessionStorage', 'navigator', 'path', 'subdomain'],
      caches: ['cookie']
    },
    interpolation: {
      escapeValue: false // react already safes from xss => https://www.i18next.com/translation-function/interpolation#unescape
    }
  });

const loading = (
  <div>
    <LinearProgress />
  </div>
)

function App() {
  return (
    <Suspense fallback={loading}>
    <Router>
      <Navbar />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<Home />} />
        <Route path="/sign-up" element={<SingUp />} />
        <Route path="/accounts" element={<AllAccounts />} />
        <Route path="/accounts:id" element={<SingleAccount />} />
        <Route path="/accounts/self" element={<AccountInfo />} />
        <Route path="/accounts/edit/:id" element={<EditSingleAccount />} />
        <Route path="*" element={<h1>Not Found</h1>} />
        <Route path="/edit-chemist/:id" element={<EditChemist />} />
        <Route path="/edit-admin/:id" element={<EditChemist />} />
        <Route path="/edit-patient/:id" element={<EditChemist />} />
      </Routes>
    </Router>
    </Suspense>
  );
}



export default App;
