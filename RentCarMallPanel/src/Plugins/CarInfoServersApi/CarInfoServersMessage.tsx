import { API } from 'Plugins/CommonUtils/Send/Serializable';
import {
    alertCallBack,
    backdropInitCallBack,
    InfoCallBackType,
    SimpleCallBackType
} from 'Plugins/CommonUtils/Types/ToClusterMessage';
import { ToCarInfoServersMessage } from 'Plugins/CommonUtils/Types/ToClusterMessages/ToCarInfoServersMessage';

export abstract class CarInfoServersMessage extends API {
    send(successCall: InfoCallBackType,
         failureCall: InfoCallBackType = alertCallBack,
         backdropCall: SimpleCallBackType | null = backdropInitCallBack,
    ) {
        new ToCarInfoServersMessage(JSON.stringify(this)).send(successCall, failureCall, backdropCall, undefined, this.getRoute())
    }
}
