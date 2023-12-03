import { Serializable } from 'Plugins/CommonUtils/Send/Serializable'


export  class CarInfo extends Serializable {
    constructor(
        public carID:number,
        public model:string,
        public color:string,
        public rentPrice:number,
        public availability:boolean,
        public imageUrl:string,
        
    ) {
        super()
    }
}
