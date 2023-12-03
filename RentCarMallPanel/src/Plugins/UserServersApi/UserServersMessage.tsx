import { API } from 'Plugins/CommonUtils/Send/Serializable';
import {
    alertCallBack,
    backdropInitCallBack,
    InfoCallBackType,
    SimpleCallBackType
} from 'Plugins/CommonUtils/Types/ToClusterMessage';
import { ToUserServersMessage } from 'Plugins/CommonUtils/Types/ToClusterMessages/ToUserServersMessage';

export abstract class UserServersMessage extends API {
    send(successCall: InfoCallBackType,
         failureCall: InfoCallBackType = alertCallBack,
         backdropCall: SimpleCallBackType | null = backdropInitCallBack,
    ) {
        new ToUserServersMessage(JSON.stringify(this)).send(successCall, failureCall, backdropCall, undefined, this.getRoute())
    }
}
