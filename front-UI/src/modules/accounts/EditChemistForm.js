import React from 'react'
import { Box, TextField } from '@mui/material'
import { Button } from '@mui/material'


export function EditChemistForm({
  handleSubmit,
  licenseNumber,
  setLicenseNumber
}) {
  return (
    <Box>
      <form className="form-container">

<label>License Number</label>
<TextField
  defaultValue={licenseNumber}
  onChange={(e) => {setLicenseNumber(e.target.value);
    console.log(`e.target.value: ${e.target.value}`)}}
/>
<Button
  sx={{ width: '40' }}
  variant="contained"
  onClick={handleSubmit}
  className="search-btn"
>
  Submit
</Button>
</form>
    </Box>
  )
};
