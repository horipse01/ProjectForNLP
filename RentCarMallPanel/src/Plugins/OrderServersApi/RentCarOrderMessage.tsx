import { OrderServersMessage } from 'Plugins/OrderServersApi/OrderServersMessage'


export class RentCarOrderMessage extends OrderServersMessage {
    constructor(
      public userToken:string,
      public carID:number,
      public model:string,
      public rentDays:number,
      public rentPrice:number,
    ) {
      super()
    }
}
