import React, { useState } from 'react';
import { TextField, Button, Typography, Container, Snackbar,Checkbox } from '@mui/material';
import { styled } from '@mui/system';
import { useHistory } from 'react-router-dom'
import { AddCarInfoMessage } from 'Plugins/CarInfoServersApi/AddCarInfoMessage'

const ContainerWrapper = styled(Container)({})

const Form = styled('form')({})

const SubmitButton = styled(Button)({})

export const AddCarPage: React.FC = () => {
    const history = useHistory()
    const [model, setModel] = useState('')
    const [color, setColor] = useState('')
    const [rentPrice, setRentPrice] = useState(0)
    const [availability, setAvailability] = useState(false)
    const [imageUrl, setImageUrl] = useState('')
    const [errorMessage, setErrorMessage] = useState('')
    const [snackBarOpen, setSnackBarOpen] = useState(false)

    const handleModelChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setModel(event.target.value)
    }

    const handleColorChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setColor(event.target.value)
    }

    const handleRentPriceChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setRentPrice(parseFloat(event.target.value))
    }

    const handleAvailabilityChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setAvailability(event.target.checked)
    }

    const handleImageUrlChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setImageUrl(event.target.value)
    }

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault()
        // Call the new AddCarMessage interface to add the vehicle information
        // If addition is successful, prompt the user and clean up the input data

        new AddCarInfoMessage(model, color, rentPrice, availability, imageUrl).send(
            () => {
                setErrorMessage('添加成功')
                setModel('')
                setColor('')
                setRentPrice(0)
                setAvailability(false)
                setImageUrl('')
            },
            error => {
                setErrorMessage(error)
            }
        )
    }

    const handleGoBack = () => {
        history.push('/carlist') // 根据您的路由设置，确保路径与carlist界面的路径匹配
    }

    const handleSnackBarClose = () => {
        setSnackBarOpen(false)
    }

    return (
        <ContainerWrapper maxWidth="sm">
            <div>
                <Typography variant="h4" component="h1" sx={{ marginBottom: 4 }}>
                    Add Vehicle Information
                </Typography>
                <Form onSubmit={handleSubmit}>
                    <TextField label="Model" value={model} onChange={handleModelChange} fullWidth margin="normal" />
                    <TextField label="Color" value={color} onChange={handleColorChange} fullWidth margin="normal" />
                    <TextField
                        label="Rent Price"
                        type="number"
                        value={rentPrice}
                        onChange={handleRentPriceChange}
                        fullWidth
                        margin="normal"
                    />
                    <TextField
                        label="Image Url"
                        value={imageUrl}
                        onChange={handleImageUrlChange}
                        fullWidth
                        margin="normal"
                    />
                    <Checkbox checked={availability} onChange={handleAvailabilityChange} />
                    <span> availability </span>
                    <SubmitButton variant="contained" color="primary" type="submit" fullWidth>
                        Submit1
                    </SubmitButton>
                </Form>
                <div style={{ width: '100%', height: '10px' }}></div>
                <Button variant="outlined" color="primary" onClick={handleGoBack} fullWidth>
                    back to list1
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
            </div>
        </ContainerWrapper>
    )
}