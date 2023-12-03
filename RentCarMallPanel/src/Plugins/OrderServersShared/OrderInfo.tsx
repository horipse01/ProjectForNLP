import { Serializable } from 'Plugins/CommonUtils/Send/Serializable'


export  class OrderInfo extends Serializable {
    constructor(
        public orderID:number,
        public userToken:string,
        public carID:number,
        public model:string,
        public rentDays:number,
        public rentPrice:number,
        
    ) {
        super()
    }
}
