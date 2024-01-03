import SimcompAlert from "./SimcompAlert";

export default function AlertList(props) {
    const alerts = props.alerts;

    return (
        <div>
            {alerts.map((a, index) => (
                <SimcompAlert
                    key={index}
                    dismissible={a.dismissible}
                    severity={a.severity}
                    header={a.header}
                    message={a.message}
                    status={a.status}
                />
            ))}
        </div>
    )
}