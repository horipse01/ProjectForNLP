import { CarInfoServersMessage } from 'Plugins/CarInfoServersApi/CarInfoServersMessage'


export class AddCarInfoMessage extends CarInfoServersMessage {
    constructor(
      public model:string,
      public color:string,
      public rentPrice:number,
      public availability:boolean,
      public imageUrl:string,
    ) {
      super()
    }
}
