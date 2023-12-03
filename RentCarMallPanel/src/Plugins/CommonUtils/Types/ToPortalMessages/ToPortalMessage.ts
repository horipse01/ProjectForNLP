import { Message } from 'Plugins/CommonUtils/Send/Serializable'
import { ToClusterMessage } from 'Plugins/CommonUtils/Types/ToClusterMessage'
// import {getTokenSnap} from "Plugins/UserAccountAPI/Stores/UserInfoStore";
import { getUserTokenSnap } from 'Plugins/CommonUtils/Store/UserTokenStore'

export class ToPortalMessage extends Message {
    toClusterMessage: ToClusterMessage
    userToken: string

    constructor(toClusterMessage: ToClusterMessage) {
        super()
        this.toClusterMessage = toClusterMessage
        this.userToken = getUserTokenSnap()
    }
}
