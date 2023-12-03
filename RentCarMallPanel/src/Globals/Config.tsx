export function readConfig() {
    const outcome = {} as any
    try {
        //env = window.location.host.split(".")[1]
        outcome.hubURL = `http://rentcarmallportal.test2.services.functor.cn:32311`
    } catch (error) {
        alert('alerting:' + error)
    }
    return outcome
}

export const config = readConfig()
