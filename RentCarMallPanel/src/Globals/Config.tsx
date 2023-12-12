export function readConfig() {
    const outcome = {} as any
    try {
        //env = window.location.host.split(".")[1]
        outcome.hubURL = 'http://127.0.0.1:6080'
    } catch (error) {
        alert('alerting:' + error)
    }
    return outcome
}

export const config = readConfig()
