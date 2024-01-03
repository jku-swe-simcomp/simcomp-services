export default function VisualizationLink(props) {

    function getURL() {
        const url = '/robot-gui/robot-gui.html';
        const params = new URLSearchParams();
        params.append('API_URL', process.env.REACT_APP_API_URL);
        params.append('sessionKey', props.session.sessionKey)

        const finalURL = url + '?' + params.toString();
        return finalURL;
    }

    const normalText = {
        "color": "inherit",
        "textDecoration": "inherit"
    }

    return (
        <a
            href={getURL()}
            target="_blank"
            rel="noreferrer"
            style={normalText}
        >
            See visualization
        </a>
    )
}