import { useState } from "react";
import { SessionContext, InstanceContext } from "./contexts/Contexts";

export default function Wrapper(props) {
    const [sessions, setSessions] = useState([]);
    const sessionsValue = { sessions, setSessions, addCommand };
    const [instances, setInstances] = useState([]);
    const instancesValue = { instances, setInstances };

    function addCommand(sessionKey, newCommand) {
        setSessions(sessions => {
            return sessions.map(session => {
                if (session.sessionKey === sessionKey) {
                    const commands = session.commands ? [...session.commands, newCommand] : [newCommand]
                    return { ...session, commands: commands };
                }
                return session;
            });
        });
    };

    return (
        <SessionContext.Provider value={sessionsValue}>
            <InstanceContext.Provider value={instancesValue}>
                {props.children}
            </InstanceContext.Provider >
        </SessionContext.Provider>
    )
}
