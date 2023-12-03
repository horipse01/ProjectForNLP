import React, { useState } from 'react';
import { TextField, Button, Typography, Container, Snackbar } from '@mui/material';
import { useHistory } from 'react-router-dom'
import { UseRegisterMessage } from 'Plugins/UserServersApi/UseRegisterMessage'

export const UserRegisterPage = () => {
    const history = useHistory()
    const [username, setUsername] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [confirmPassword, setConfirmPassword] = useState('')
    const [errorMessage, setErrorMessage] = useState('')
    const [snackBarOpen, setSnackBarOpen] = useState(false)

    const handleUsernameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setUsername(event.target.value)
    }

    const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setEmail(event.target.value)
    }

    const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(event.target.value)
    }

    const handleConfirmPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setConfirmPassword(event.target.value)
    }

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault()
        if (password.length < 4 || password.length > 30) {
            setErrorMessage('Password length must be between 4 and 30 characters')
            setSnackBarOpen(true)
            return
        }
        if (!email.includes('@')) {
            setErrorMessage('Please enter a valid email address')
            setSnackBarOpen(true)
            return
        }
        if (password !== confirmPassword) {
            setErrorMessage('The passwords do not match')
            setSnackBarOpen(true)
            return
        }

        new UseRegisterMessage(username, email, password).send(
            () => {
                history.push('/login')
            },
            error => {
                console.log(error)
                setErrorMessage('Registration failed. Please try again later.')
                setSnackBarOpen(true)
            }
        )
    }

    const handleSnackBarClose = () => {
        setSnackBarOpen(false)
    }

    const handleGotoLogin = () => {
        history.push('/login')
    }

    return (
        <Container maxWidth="sm">
            <div>
                <Typography variant="h4" component="h1" gutterBottom>
                    Registration
                </Typography>
                <form onSubmit={handleSubmit}>
                    <TextField
                        label="UserName"
                        value={username}
                        onChange={handleUsernameChange}
                        fullWidth
                        margin="normal"
                        required
                    />
                    <TextField
                        label="Email"
                        type="email"
                        value={email}
                        onChange={handleEmailChange}
                        fullWidth
                        margin="normal"
                        required
                    />
                    <TextField
                        label="Password"
                        type="password"
                        value={password}
                        onChange={handlePasswordChange}
                        fullWidth
                        margin="normal"
                        required
                    />
                    <TextField
                        label="Confirm Password"
                        type="password"
                        value={confirmPassword}
                        onChange={handleConfirmPasswordChange}
                        fullWidth
                        margin="normal"
                        required
                    />
                    <Button variant="contained" color="primary" type="submit" fullWidth>
                        Register
                    </Button>
                </form>
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

            <Button variant="outlined" color="primary" onClick={handleGotoLogin} sx={{ marginTop: 2 }}>
                Go to Login
            </Button>
        </Container>
    )
}
