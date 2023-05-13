import { useEffect, useState } from 'react';
import { EditChemistForm } from '../modules/accounts/EditChemistForm';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { putChemist } from '../api/mok/accountApi';

export default function EditCategory() {
    const { id } = useParams();

    const [licenseNumber, setLicenseNumber] = useState("");

    const navigate = useNavigate();
    const location = useLocation();

    

    const handleSubmit = async (event) => {
        event.preventDefault();
          const formData = new FormData();
          formData.append('id', id);
          formData.append('licenseNumber', licenseNumber);
          await putChemist(Object.fromEntries(formData));
          navigate('/accounts/{id}');
    };

      return (
        <div className="container flex center-column">
          <EditChemistForm
            handleSubmit={handleSubmit}
            setLicenseNumber={setLicenseNumber}
            licenseNumber={licenseNumber}
          />
        </div>
      );
}