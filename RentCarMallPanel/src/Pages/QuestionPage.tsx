import React, {useEffect, useState} from 'react';
import { TextField, styled , Button, Typography, Container, Snackbar } from '@mui/material';
import { useHistory } from 'react-router-dom'
import { UseRegisterMessage } from 'Plugins/UserServersApi/UseRegisterMessage'
import { QuestionMessage } from 'Plugins/UserServersApi/QuestionMessage'
import {getUserTokenSnap} from "../Plugins/CommonUtils/Store/UserTokenStore";
import {GetOrderListMessage} from "../Plugins/OrderServersApi/GetOrderListMessage";
import {OrderInfo} from "../Plugins/OrderServersShared/OrderInfo";
const Form = styled('form')({})
//定义一个蓝色的SubmitButton
const SubmitButton = styled(Button)({
    backgroundColor: '#1976d2',
    color: 'white',
    '&:hover': {
        backgroundColor: '#115293',
    },
})
//写一个网页，有一个输入框，输入问题，点击提交后，发送“QuestionMessage”到后端，开始写
export const QuestionPage = () => {
    const history = useHistory()
    const [question, setQuestion] = useState('')
    const [errorMessage, setErrorMessage] = useState('')
    const [snackBarOpen, setSnackBarOpen] = useState(false)
    const [userToken, setUserToken] = useState('')
    const [username, setUsername] = useState('')

    useEffect(() => {
        const userToken = getUserTokenSnap()
        if (!userToken) {
            //转到登录界面
            history.push('/login')
        }
        //否则，setUserToken
        setUserToken(userToken)
    }, [])

    const handleQuestionChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setQuestion(event.target.value)
    }

    //写一个handleSnackBarClose
    const handleSnackBarClose = () => {
        setSnackBarOpen(false)
    }

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault()
        new QuestionMessage(userToken,question).send(
            data => {
                if(data == 'success') {
                    //TODO: 应该调用API来获得答案
                    alert("Success")
                }else {
                    alert("Failed")
                }
            },
            error => {
                console.log(error)
                setErrorMessage(error)
                setSnackBarOpen(true)
            }
        )
    }

    //写一个页面，有一个输入框，输入问题，点击提交后，调用handleSubmit
    return (
        <Container maxWidth="sm">
            <Typography variant="h4" component="h1" align="center">
                提问
            </Typography>
            <Form onSubmit={handleSubmit}>
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    id="question"
                    label="问题"
                    name="question"
                    autoComplete="question"
                    autoFocus
                    value={question}
                    onChange={handleQuestionChange}
                />
                <SubmitButton
                    type="submit"
                    fullWidth
                    variant="contained"
                    sx={{ mt: 3, mb: 2 }}
                >
                    提交
                </SubmitButton>
            </Form>
            <Snackbar
                anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
                open={snackBarOpen}
                onClose={handleSnackBarClose}
                message={errorMessage}
                autoHideDuration={3000}
            />
        </Container>
    )

}