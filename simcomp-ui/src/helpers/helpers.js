import axios from "axios";

export async function deleteAllInstances() {
    const res = await axios.get(process.env.REACT_APP_API_URL + '/simulation/instance', {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors'
    });
    for (const instance of res.data) {
        axios.delete(process.env.REACT_APP_API_URL + '/simulation/' + instance.simulationType + '/instance/' + instance.instanceId);
    }
}

export function radiansToDegrees(radians) {
    return (radians * (180 / Math.PI)).toFixed(2);
}