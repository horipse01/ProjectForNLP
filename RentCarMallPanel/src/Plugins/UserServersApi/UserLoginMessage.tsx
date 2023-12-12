import { UserServersMessage } from 'Plugins/UserServersApi/UserServersMessage'


export class UserLoginMessage extends UserServersMessage {
    constructor(
      public studentId:string,
      public password:string,
    ) {
      super()
    }
}
