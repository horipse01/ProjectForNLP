import { API } from 'Plugins/CommonUtils/Send/Serializable';
import {
    alertCallBack,
    backdropInitCallBack,
    InfoCallBackType,
    SimpleCallBackType
} from 'Plugins/CommonUtils/Types/ToClusterMessage';
import { ToOrderServersMessage } from 'Plugins/CommonUtils/Types/ToClusterMessages/ToOrderServersMessage';

export abstract class OrderServersMessage extends API {
    send(successCall: InfoCallBackType,
         failureCall: InfoCallBackType = alertCallBack,
         backdropCall: SimpleCallBackType | null = backdropInitCallBack,
    ) {
        new ToOrderServersMessage(JSON.stringify(this)).send(successCall, failureCall, backdropCall, undefined, this.getRoute())
    }
}
