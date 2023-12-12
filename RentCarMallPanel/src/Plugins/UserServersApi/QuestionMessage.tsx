import { UserServersMessage } from 'Plugins/UserServersApi/UserServersMessage'


export class QuestionMessage extends UserServersMessage {
    constructor(
        public userToken:string,
        public question:string,
    ) {
        super()
    }
}
