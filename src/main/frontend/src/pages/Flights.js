import { useState, useEffect } from 'react';
import { useLocation, useNavigate } from "react-router-dom";
import { DataGrid, useGridApiRef } from '@mui/x-data-grid';
import { Button, IconButton, Dialog, DialogTitle, Stack, Snackbar, Alert } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import DownloadingIcon from '@mui/icons-material/Downloading';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import axios from 'axios';

const axiosConfig = {
  headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      "Access-Control-Allow-Origin": "*",
  }
};

function Flights() {
  const [data, setData] = useState([]);
  const [openForm, setOpenForm] = useState(false);

  const [alertMessage, setAlertMessage] = useState(``);
  const [openAlert, setOpenAlert] = useState(false);
  const [severity, setSeverity] = useState(``);

  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const location = useLocation();
  const airplaneId = location.state.airplaneId;

  const gridApiRef = useGridApiRef();

  useEffect(() => {
    axios.get(`http://localhost:8080/airplanes/${airplaneId}/flights`)
      .then(response => setData(response.data))
      .catch(error => console.log(error));
  }, [airplaneId]);

  const handleRowDoubleClick = (params) => {
    navigate(`/flight/`, {
        state: {
            airplaneId : `${airplaneId}`,
            flightNumber : `${params.id}`
        }
    }
    );
  };

  const handleClickOpen = () => {
    setOpenForm(true);
  };

  const handleClickClose = () => {
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
      axios.post(`http://localhost:8080/airplanes/${airplaneId}/flights/calculate`, data, axiosConfig)
      .then(response => {
        handleClickClose();
        setSeverity(`success`);
        setOpenAlert(true);
        setAlertMessage(`Flight calculated!`);
        gridApiRef.current.updateRows([response.data]);
      })
      .catch(error => {
        setSeverity(`error`);
        setOpenAlert(true);
        setAlertMessage(`Flight calculation ended with error, watch log`);
        console.log(error);
      });
    }, false);

    if (file) {
      reader.readAsText(file);
    }
  }


  return (
    <div style={{ height: 400, width: '98%', margin: '1%' }}>
        <h1>Flights</h1>
        <DataGrid
            apiRef={gridApiRef}
            rows={data}
            columns={[
                { headerName: 'Number', field: 'number', width: 120 },
                { headerName: 'Waypoints Number', field: 'wayPointsNumber', width: 160 },
                { headerName: 'Time spent for flight', field: 'timeSpent', width: 160 },
                { headerName: 'Action', field: 'action',
                  renderCell: (params) => (
                    <Stack spacing={1} direction="row">
                      <IconButton aria-label='download' disabled={loading} onClick={() => {
                        setLoading(true);
                        axios.get(`http://localhost:8080/airplanes/${airplaneId}/flights/${params.row.number}`)
                          .then(response => {
                            const url = window.URL.createObjectURL(new Blob([JSON.stringify(response.data)]));
                            const link = document.createElement ('a'); 
                            link.href = url; 
                            link.setAttribute('download', `flight_${params.row.number}_for_airplane_${airplaneId}.json`);
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
                        console.log(params.row.number);
                        axios.delete(`http://localhost:8080/airplanes/${airplaneId}/flights/${params.row.number}`)
                          .then(()=> {
                            setSeverity(`success`);
                            setOpenAlert(true);
                            setAlertMessage(`Flight deleted success!`);
                            gridApiRef.current.updateRows([{ number: params.row.number, _action: 'delete' }]);       
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
            getRowId={(row) => row.number}
        />
        <Stack spacing={2} direction="column">
          <Button variant='outlined' onClick={handleClickOpen}>
            Calculate flight
          </Button>
          <Dialog open={openForm} onClose={handleClickClose}>
            <DialogTitle>Add waypoint list from file</DialogTitle>
            <IconButton variant='outlined' component="label">
              <UploadFileIcon />
              <input hidden accept="txt/*" type="file" onChange={hadleFileUpload} />
            </IconButton>
          </Dialog>

          <Button variant="outlined" onClick={() => {
            navigate(-1)
            }}>
            Back
          </Button>
        </Stack>
        <Snackbar anchorOrigin={{ vertical: 'bottom', horizontal : 'right' }} open={openAlert} autoHideDuration={5000} onClose={handleCloseAlert}>
          <Alert severity={severity}>
            {alertMessage}
          </Alert>
        </Snackbar>
    </div>
  );

}

export default Flights;