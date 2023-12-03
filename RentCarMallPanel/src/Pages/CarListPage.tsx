import React, { useEffect, useState } from 'react'
import { GetCarInfoMessage } from 'Plugins/CarInfoServersApi/GetCarInfoMessage'
import { CarInfo } from 'Plugins/CarInfoServersShared/CarInfo'
import { Button, Dialog, DialogActions, DialogContent, DialogTitle, Grid, Snackbar } from '@mui/material'
import { RentCarOrderMessage } from 'Plugins/OrderServersApi/RentCarOrderMessage'
import { getUserTokenSnap } from 'Plugins/CommonUtils/Store/UserTokenStore'
import { useHistory } from 'react-router-dom'

export const CarListPage: React.FC = () => {
    const [vehicleList, setVehicleList] = useState<CarInfo[]>([])
    const [isLoginDialogOpen, setIsLoginDialogOpen] = useState(false)
    const history = useHistory()
    const [snackBarOpen, setSnackBarOpen] = useState(false)
    const [errorMessage, setErrorMessage] = useState('')

    useEffect(() => {
        new GetCarInfoMessage().send(
            data => {
                //define a String s here
                const parsedData: CarInfo[] = JSON.parse(data)
                setVehicleList(parsedData)
            },
            error => {
                console.error(error)
            }
        )
    }, [])

    const isMobile = window.innerWidth <= 600

    const handleRentButtonClicked = (carInfo: CarInfo) => {
        const userToken = getUserTokenSnap()
        const { carID, model, rentPrice } = carInfo
        const rentDays = 7 as number
        /*CarInfo
              public carID:number,
        public model:string,
        public color:string,
        public rentPrice:number,
        public availability:boolean,
        public imageUrl:string,
      */

        /*
            public userToken:string,
      public carID:number,
      public model:string,
      public rentDays:number,
      public rentPrice:number,
      */

     if (userToken) {
         const rentCarOrderMessage = new RentCarOrderMessage(userToken, carID, model, rentDays, rentPrice)
         rentCarOrderMessage.send(
             response => {
                 setSnackBarOpen(true)
                 setErrorMessage(response)
             },
             error => {
                 setErrorMessage(error)
             }
         )
     } else {
         setIsLoginDialogOpen(true)
     }
    }
    const handleLoginDialogClose = (proceedToLogin: boolean) => {
        setIsLoginDialogOpen(false)
        if (proceedToLogin) {
            history.push('/login')
        }
    }
    const handleViewOrderList = () => {
        history.push('/orderlist')
    }

    const handleSnackBarClose = () => {
        setSnackBarOpen(false)
    }

    const renderCarInfo = (carInfo: CarInfo) => {
        // const [rentDays, setRentDays] = useState(7) // Default value of 7 days

        // const handleRentDaysChange = (event: any) => {
        //     const value = event.target.value
        //     setRentDays(value)
        // }

        return (
            <div
                style={{
                    display: 'flex',
                    alignItems: 'center',
                    height: '100px',
                    marginBottom: '10px',
                    backgroundColor: '#f5f5f5',
                }}
            >
                <img src={carInfo.imageUrl} alt="Car" style={{ width: '135px', height: '85px', margin: '5px' }} />
                <div
                    style={{
                        display: 'flex',
                        flexDirection: isMobile ? 'column' : 'row',
                        justifyContent: 'space-between',
                        marginLeft: '10px',
                        flexGrow: 1,
                    }}
                >
                    <div>
                        <span style={{ fontWeight: 'bold' }}>Model:</span> {carInfo.model}
                    </div>
                    <div>
                        <span style={{ fontWeight: 'bold' }}>Color:</span> {carInfo.color}
                    </div>
                    <div>
                        <span style={{ fontWeight: 'bold' }}>Rent Price:</span> ${carInfo.rentPrice}
                    </div>
                    {/* <input type="number" value={rentDays} onChange={handleRentDaysChange} /> */}

                    <Button variant="contained" color="primary" onClick={() => handleRentButtonClicked(carInfo)}>
                        Rent
                    </Button>
                </div>
            </div>
        )
    }

    return (
        <Grid container justifyContent="center">
            <Grid item xs={12} sm={10} md={8} lg={6}>
                {vehicleList.map((carInfo, index) => (
                    <div key={index}>{renderCarInfo(carInfo)}</div>
                ))}
            </Grid>
            <Dialog open={isLoginDialogOpen} onClose={() => handleLoginDialogClose(false)}>
                <DialogTitle>Login Required</DialogTitle>
                <DialogContent>
                    <div>
                        You need to login before proceeding with the rent. Do you want to proceed to the login page?
                    </div>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => handleLoginDialogClose(false)}>Cancel</Button>
                    <Button onClick={() => handleLoginDialogClose(true)} autoFocus>
                        Go to Login
                    </Button>
                </DialogActions>
            </Dialog>
            <Button variant="outlined" color="primary" onClick={handleViewOrderList}>
                View Order List
            </Button>
            <Snackbar
                open={snackBarOpen}
                onClose={handleSnackBarClose}
                message={errorMessage}
                autoHideDuration={3000}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'center',
                }}
            />
        </Grid>
    )
}