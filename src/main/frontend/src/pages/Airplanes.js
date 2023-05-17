import { useState, useEffect } from 'react';
import axios from 'axios';
import { DataGrid, useGridApiRef } from '@mui/x-data-grid';
import { Tooltip, Button, IconButton, Dialog, DialogTitle, Stack, Snackbar, Alert } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import DownloadingIcon from '@mui/icons-material/Downloading';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import { useNavigate } from 'react-router-dom';
import airplaneLogo from './textures/airplane.png';
import positionLogo from './textures/position.png';

const axiosConfig = {
  headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      "Access-Control-Allow-Origin": "*",
  }
};

function Airplanes() {
  const [data, setData] = useState([]);
  const [openForm, setOpenForm] = useState(false);
  
  const [alertMessage, setAlertMessage] = useState(``);
  const [openAlert, setOpenAlert] = useState(false);
  const [severity, setSeverity] = useState(``);

  const [loading, setLoading] = useState(false);
  
  const navigate = useNavigate();
  const gridApiRef = useGridApiRef();

  useEffect(() => {
    axios.get('http://localhost:8080/airplanes')
      .then(response => setData(response.data))
      .catch(error => console.log(error));
  }, []);

  const handleRowDoubleClick = (params) => {
    navigate(`/flights/`, {
        state: {
            airplaneId : `${params.id}`
        }
    }
    );
  };

  const handleClickOpenForm = () => {
    setOpenForm(true);
  };

  const handleClickCloseForm = () => {
    setOpenForm(false);
  };

  const handleCloseAlert = () => {
    setOpenAlert(false);
  }

  const hadleFileUpload = (event) => {
    const reader = new FileReader();
    const file = event.target.files[0];
    reader.addEventListener('load', () => {
      const data = reader.result;
      axios.post('http://localhost:8080/airplanes', data, axiosConfig)
      .then(response => {
        handleClickCloseForm();
        setSeverity(`success`);
        setOpenAlert(true);
        setAlertMessage(`Airplane added!`);
        gridApiRef.current.updateRows([response.data]);
      })
      .catch(error => {
        setSeverity(`error`);
        setOpenAlert(true);
        setAlertMessage(`Airplane adding ended with error, watch log`);
        console.log(error);
      });
    }, false);

    if (file) {
      reader.readAsText(file);
    }
  }

  return (
    <div style={{ height: 400, width: '98%', margin: '1%' }}>
      <h1>Airplanes</h1>
      <DataGrid
        apiRef={gridApiRef}
        rows={data}
        columns={[
          { headerName: 'ID', field: 'id', width: 90 },
          { headerName: 'Airplane characteristics', field: 'airplaneCharacteristics', width: 180,
            renderCell: (params) => (
                <Tooltip title={<span style={{ whiteSpace: 'pre-line' }}>
                    { `max v: ${params.row.airplaneCharacteristics.maxSpeed}` 
                        + `\nmax a: ${params.row.airplaneCharacteristics.maxAcceleration}`
                        + `\naltitude change: ${params.row.airplaneCharacteristics.rateAltitudeChange}`
                        + `\ncourse change: ${params.row.airplaneCharacteristics.rateCourseChange}` 
                    }
                    </span>} >
                    <img src={airplaneLogo} style={{ width: '50%', height: '100%' }}/>
                </Tooltip>
            ),
          },
          { headerName: 'Current Position', field: 'position', width: 150, 
            renderCell: (params) => (
                <Tooltip title={<span style={{ whiteSpace: 'pre-line'}}>
                  { `x: ${params.row.position.latitude}`
                      + `\ny: ${params.row.position.longitude}`
                      + `\nz: ${params.row.position.flightAltitude}`
                      + `\nv: ${params.row.position.flightSpeed}`
                      + `\ncourse: ${params.row.position.course}`}
                  </span>} >
                  <img src={positionLogo} style={{ width: '30%', height: '70%' }}/>
                </Tooltip>
            ),
          },
          { headerName: 'Number of Flights', field: 'flightsNumber', width: 140 },
          { headerName: 'Action', 
            field: 'action',
            renderCell: (params) => (
              <Stack spacing={1} direction="row">
                <IconButton aria-label='download' disabled={loading} onClick={() => {
                  setLoading(true);
                  axios.get(`http://localhost:8080/airplanes/${params.row.id}`)
                  .then(response => {
                    const url = window.URL.createObjectURL(new Blob([JSON.stringify(response.data)]));
                    const link = document.createElement ('a'); 
                    link.href = url; 
                    link.setAttribute('download', `airplane_${params.row.id}.json`);
                    document.body.appendChild(link);
                    link.click();
                    link.parentNode.removeChild(link);
                    setLoading(false);
                  })
                  .catch(error => {
                    console.log(error);
                    setLoading(false);
                    setSeverity(`error`);
                    setOpenAlert(true);
                    setAlertMessage(`Download error, watch log`);
                  });
                }}>
                  {(loading) ? <DownloadingIcon/> : <FileDownloadIcon/>}
                </IconButton>
                <IconButton aria-label="delete" onClick={() => {
                  axios.delete(`http://localhost:8080/airplanes/${params.row.id}`)
                    .then(() => {
                      setSeverity(`success`);
                      setOpenAlert(true);
                      setAlertMessage(`Airplane deleted success!`);
                      gridApiRef.current.updateRows([{ id: params.row.id, _action: 'delete' }]);
                    })
                    .catch(error => {
                      console.log(error);
                      setSeverity(`error`);
                      setOpenAlert(true);
                      setAlertMessage(`Deleting error, watch log`);
                    });
                }}>
                  <DeleteIcon />
                </IconButton>
              </Stack>
            ),
          },
        ]}
        onRowDoubleClick={handleRowDoubleClick}
      />
      <Stack direction="column">
        <Button variant='outlined' onClick={handleClickOpenForm}>
          Add airplane
        </Button>
        <Dialog open={openForm} onClose={handleClickCloseForm}>
          <DialogTitle>Add airplane from file</DialogTitle>
          <IconButton variant='outlined' component="label">
            <UploadFileIcon />
            <input hidden accept="txt/*" type="file" onChange={hadleFileUpload} />
          </IconButton>
        </Dialog>
      </Stack>
      <Snackbar anchorOrigin={{ vertical: 'bottom', horizontal : 'right' }} open={openAlert} autoHideDuration={5000} onClose={handleCloseAlert}>
        <Alert severity={severity}>
          {alertMessage}
        </Alert>
      </Snackbar>
    </div>
  );
}

export default Airplanes;