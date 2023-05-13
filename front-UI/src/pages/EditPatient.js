import { useEffect, useState } from 'react';
import { EditPatientForm } from '../modules/accounts/EditPatientForm';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { putPatient } from '../api/mok/accountApi';

export default function EditCategory() {
    const { id } = useParams();

    const [pesel, setPesel] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [nip, setNip] = useState("");

    const navigate = useNavigate();
    const location = useLocation();

    const handleSubmit = async (event) => {
        event.preventDefault();
          const formData = new FormData();
            formData.append('id', id);
            formData.append('pesel', pesel);
            formData.append('firstName', firstName);
            formData.append('lastName', lastName);
            formData.append('phoneNumber', phoneNumber);
            formData.append('nip', nip);
          await putPatient(Object.fromEntries(formData));
          navigate('/accounts/{id}');
    };

      return (
        <div className="container flex center-column">
          <EditPatientForm
            handleSubmit={handleSubmit}
            pesel={pesel}
            setPesel={setPesel}
            firstName={firstName}
            setFirstName={setFirstName}
            lastName={lastName}
            setLastName={setLastName}
            phoneNumber={phoneNumber}
            setPhoneNumber={setPhoneNumber}
            nip={nip}
            setNip={setNip}
          />
        </div>
      );
}