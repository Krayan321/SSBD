import { useEffect, useState } from 'react';
import { EditAdminForm } from '../modules/accounts/EditAdminForm';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { putAdmin } from '../api/mok/accountApi';

export default function EditCategory() {
    const { id } = useParams();

    const [workPhoneNumber, setWorkPhoneNumber] = useState("");

    const navigate = useNavigate();
    const location = useLocation();

    

    const handleSubmit = async (event) => {
        event.preventDefault();
          const formData = new FormData();
          formData.append('id', id);
          formData.append('workPhoneNumber', workPhoneNumber);
          await putAdmin(Object.fromEntries(formData));
          navigate('/accounts/{id}');
    };

      return (
        <div className="container flex center-column">
          <EditAdminForm
            handleSubmit={handleSubmit}
            setWorkPhoneNumber={setWorkPhoneNumber}
            licenseWorkPhoneNumber={workPhoneNumber}
          />
        </div>
      );
}