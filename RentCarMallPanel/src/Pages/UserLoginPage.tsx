import React, { useState } from 'react';
import { TextField, Button, Typography, Container, Snackbar } from '@mui/material';
import { styled } from '@mui/system';
import { useHistory } from 'react-router-dom'
import { UserLoginMessage } from 'Plugins/UserServersApi/UserLoginMessage'
import { setUserToken } from 'Plugins/CommonUtils/Store/UserTokenStore'

const ContainerWrapper = styled(Container)({
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
})

const Form = styled('form')({})

const SubmitButton = styled(Button)({})

// const ErrorMessage = styled(Typography)({
//     color: 'red',
//     marginBottom: '16px',
// })

export const UserLoginPage: React.FC = () => {
    const history = useHistory()
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    // const [errorMessage, setErrorMessage] = useState('')
    const [snackBarOpen, setSnackBarOpen] = useState(false)

    const handleUsernameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setUsername(event.target.value)
    }

    const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(event.target.value)
    }

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault()
        new UserLoginMessage(username, password).send(
            data => {
                setUserToken(data)
                history.push('/carlist')
            },
            error => {
                console.log('error', error)
                // setErrorMessage(error)
                setSnackBarOpen(true)
            }
        )
    }

    const handleSnackBarClose = () => {
        setSnackBarOpen(false)
    }

    const handleGotoRegister = () => {
        history.push('/register')
    }

    return (
        <ContainerWrapper maxWidth="sm">
            <div>
                <Typography variant="h4" component="h1" sx={{ marginBottom: 4 }}>
                    Login
                </Typography>
                <Form onSubmit={handleSubmit}>
                    <TextField
                        label="UserName"
                        value={username}
                        onChange={handleUsernameChange}
                        fullWidth
                        margin="normal"
                    />
                    <TextField
                        label="Password"
                        type="password"
                        value={password}
                        onChange={handlePasswordChange}
                        fullWidth
                        margin="normal"
                    />
                    <SubmitButton variant="contained" color="primary" type="submit" fullWidth>
                        Login
                    </SubmitButton>
                </Form>
                {/* {errorMessage && <ErrorMessage>{errorMessage}</ErrorMessage>} */}
                <Snackbar
                    open={snackBarOpen}
                    onClose={handleSnackBarClose}
                    message="Login failed. Please check your account or password."
                    autoHideDuration={3000}
                    anchorOrigin={{
                        vertical: 'top',
                        horizontal: 'center',
                    }}
                />
            </div>

            <Button variant="outlined" color="primary" onClick={handleGotoRegister} sx={{ marginTop: 2 }}>
                Go to Register
            </Button>
        </ContainerWrapper>
    )
}
