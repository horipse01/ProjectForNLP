import React from 'react'
import {render} from 'react-dom'
import {HashRouter, Route, Switch} from 'react-router-dom'
import {CarListPage} from 'Pages/CarListPage'
import {OrderListPage} from 'Pages/OrderListPage'
import {UserLoginPage} from 'Pages/UserLoginPage'
import {UserRegisterPage} from 'Pages/UserRegisterPage'

/**这个函数为路由跳转，可以将其单独放置在一个JSX文件中*/
const Layout = () => {
    return (
        <HashRouter>
            <Switch>
                <Route path="/" exact component={UserLoginPage} />
                <Route path="/login" exact component={UserLoginPage} />
                <Route path="/register" exact component={UserRegisterPage} />
                <Route path="/carlist" exact component={CarListPage} />
                <Route path="/orderlist" exact component={OrderListPage} />
            </Switch>
        </HashRouter>
    )
}

render(<Layout />, document.getElementById('root'))
