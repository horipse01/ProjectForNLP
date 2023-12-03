import React, { useEffect, useState } from 'react';
import { Button, Grid, Typography } from '@mui/material'
import { GetOrderListMessage } from 'Plugins/OrderServersApi/GetOrderListMessage'
import { OrderInfo } from 'Plugins/OrderServersShared/OrderInfo'
import { getUserTokenSnap, setUserToken } from 'Plugins/CommonUtils/Store/UserTokenStore'
import { useHistory } from 'react-router'

export const OrderListPage: React.FC = () => {
    const [orderList, setOrderList] = useState<OrderInfo[]>([])
    const history = useHistory()

    useEffect(() => {
        const userToken = getUserTokenSnap()
        if (userToken) {
            new GetOrderListMessage(userToken).send(
                data => {
                    console.log('data----', data)
                    const parsedData: OrderInfo[] = JSON.parse(data)
                    setOrderList(parsedData)
                },
                error => {
                    console.error(error)
                }
            )
        }
    }, [])

    const renderOrderInfo = (order: OrderInfo) => {
        return (
            <div
                style={{
                    display: 'flex',
                    alignItems: 'center',
                    height: '100px',
                    marginBottom: '10px',
                    backgroundColor: '#f5f5f5',
                    padding: '10px',
                }}
            >
                <div style={{ flex: 1 }}>
                    <Typography variant="h6">Order ID: {order.orderID}</Typography>
                    <div style={{ display: 'flex', justifyContent: 'flex-start' }}>
                        <Typography style={{ marginRight: '20px' }}>Model: {order.model}</Typography>
                        <Typography style={{ marginRight: '20px' }}>Rent Days: {order.rentDays}</Typography>
                        <Typography style={{ marginRight: '20px' }}>Rent Price: ${order.rentPrice}</Typography>
                    </div>
                </div>
            </div>
        )
    }

    const handleLoginOut = () => {
        setUserToken('')
        history.push('/login')
    }
    const handlebacktoList = () => {
        history.push('/carlist')
    }

    return (
        <div>
            <Grid container justifyContent="center" style={{ minHeight: '100vh' }}>
                <Grid item xs={12} sm={10} md={8} lg={6}>
                    {orderList.length > 0 ? (
                        orderList.map((order, index) => <div key={index}>{renderOrderInfo(order)}</div>)
                    ) : (
                        <Typography variant="h6">No orders found.</Typography>
                    )}
                </Grid>
            </Grid>
            <div
                style={{
                    display: 'flex',
                    justifyContent: 'center',
                    marginBottom: '20px',
                    position: 'fixed',
                    bottom: 0,
                    left: 0,
                    right: 0,
                    margin: 'auto',
                    width: '100%',
                }}
            >
                <div style={{ width: '50%', marginRight: '5px' }}>
                    <Button variant="contained" color="primary" onClick={handlebacktoList} fullWidth>
                        Back to List
                    </Button>
                </div>
                <div style={{ width: '50%', marginLeft: '5px' }}>
                    <Button variant="outlined" color="primary" onClick={handleLoginOut} fullWidth>
                        Logout
                    </Button>
                </div>
            </div>
        </div>
    )
}
