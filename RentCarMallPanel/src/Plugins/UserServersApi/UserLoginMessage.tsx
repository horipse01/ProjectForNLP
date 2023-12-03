import { UserServersMessage } from 'Plugins/UserServersApi/UserServersMessage'


export class UserLoginMessage extends UserServersMessage {
    constructor(
      public userName:string,
      public password:string,
    ) {
      super()
    }
}
