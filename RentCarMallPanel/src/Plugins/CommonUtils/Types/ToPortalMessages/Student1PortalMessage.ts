
import { ToClusterMessage } from 'Plugins/CommonUtils/Types/ToClusterMessage'
import { getUserTokenSnap } from 'Plugins/CommonUtils/Store/UserTokenStore'
import {Message} from "Plugins/CommonUtils/Send/Serializable";

export class Student1PortalMessage extends Message {
  toClusterMessage: ToClusterMessage
  userToken: string
  constructor(toClusterMessage: ToClusterMessage) {
    super()
    this.toClusterMessage = toClusterMessage
    this.userToken = getUserTokenSnap()
  }
}
