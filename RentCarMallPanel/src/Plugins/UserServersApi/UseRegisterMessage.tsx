import { UserServersMessage } from 'Plugins/UserServersApi/UserServersMessage'


export class UseRegisterMessage extends UserServersMessage {
    constructor(
      public userName:string,
      public email:string,
      public password:string,
    ) {
      super()
    }
}
