import { OrderServersMessage } from 'Plugins/OrderServersApi/OrderServersMessage'


export class GetOrderListMessage extends OrderServersMessage {
    constructor(
      public userToken:string,
    ) {
      super()
    }
}
