import { createContext } from 'react';

export const InstanceContext = createContext({
    instances: [],
    setInstances: () => { }
});

export const SessionContext = createContext({
    sessions: [],
    setSessions: () => { },
    addCommand: () => { }
});
