import './App.css';
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


function App() {
  return (
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
      </Routes>
    </Router>
  );
}

export default App;
