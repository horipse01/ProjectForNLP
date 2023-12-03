
import { ToClusterMessage } from 'Plugins/CommonUtils/Types/ToClusterMessage'
import { MShopPortalMessage } from 'Plugins/CommonUtils/Types/ToPortalMessages/MShopPortalMessage'
import { WShopPortalMessage } from 'Plugins/CommonUtils/Types/ToPortalMessages/WShopPortalMessage'
import { HHShopPortalMessage } from 'Plugins/CommonUtils/Types/ToPortalMessages/HHShopPortalMessage'
import { HHShop2PortalMessage } from 'Plugins/CommonUtils/Types/ToPortalMessages/HHShop2PortalMessage'
import { HHShop3PortalMessage } from 'Plugins/CommonUtils/Types/ToPortalMessages/HHShop3PortalMessage'
import { RentCarShopPortalMessage } from 'Plugins/CommonUtils/Types/ToPortalMessages/RentCarShopPortalMessage'
import { RentCarMallPortalMessage } from 'Plugins/CommonUtils/Types/ToPortalMessages/RentCarMallPortalMessage'
import { HHShop4PortalMessage } from 'Plugins/CommonUtils/Types/ToPortalMessages/HHShop4PortalMessage'
import { Student1PortalMessage } from 'Plugins/CommonUtils/Types/ToPortalMessages/Student1PortalMessage'
import { Message } from 'Plugins/CommonUtils/Send/Serializable'

export enum ToPortalType {
    toBuyACarPortal = 'toBuyACarPortal',
    toMShopPortal = 'toMShopPortal',
    toWShopPortal = 'toWShopPortal',
    toHHShopPortal = 'toHHShopPortal',
    toHHShop2Portal = 'toHHShop2Portal',
    toHHShop3Portal = 'toHHShop3Portal',
    toRentCarShopPortal = 'toRentCarShopPortal',
    toRentCarMallPortal = 'toRentCarMallPortal',
    toHHShop4Portal = 'toHHShop4Portal',
    toStudent1Portal = 'toStudent1Portal',
}

export function toPortalMessage(toClusterMessage: ToClusterMessage, toPortal: ToPortalType): Message {
    switch (toPortal) {
        case ToPortalType.toMShopPortal:
            return new MShopPortalMessage(toClusterMessage)
        case ToPortalType.toWShopPortal:
            return new WShopPortalMessage(toClusterMessage)
        case ToPortalType.toHHShopPortal:
            return new HHShopPortalMessage(toClusterMessage)
        case ToPortalType.toHHShop2Portal:
            return new HHShop2PortalMessage(toClusterMessage)
        case ToPortalType.toHHShop3Portal:
            return new HHShop3PortalMessage(toClusterMessage)
        case ToPortalType.toRentCarShopPortal:
            return new RentCarShopPortalMessage(toClusterMessage)
        case ToPortalType.toRentCarMallPortal:
            return new RentCarMallPortalMessage(toClusterMessage)
        case ToPortalType.toHHShop4Portal:
            return new HHShop4PortalMessage(toClusterMessage)
        case ToPortalType.toStudent1Portal:
            return new Student1PortalMessage(toClusterMessage)
        default:
            return new Student1PortalMessage(toClusterMessage)
    }
}
