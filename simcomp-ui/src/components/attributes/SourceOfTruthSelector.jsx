import { useContext, useEffect, useState } from "react";
import { SessionContext } from "../../contexts/Contexts";
import { Box, FormControl, InputLabel, Select, MenuItem } from "@mui/material";

export default function SourceOfTruthSelector(props) {
    const sessionKey = props.sessionKey;
    const session = useContext(SessionContext).sessions.find(s => s.sessionKey === sessionKey);
    console.log(session);
    const [selectedOption, setSelectedOption] = useState(session.acquiredSimulations[0]);
    const { sessions, setSessions } = useContext(SessionContext);

    useEffect(() => {
        let sessionsCopy = [...sessions];
        let sessionCopy = { ...session };
        sessionCopy.sourceOfTruth = selectedOption;
        sessionsCopy[sessionsCopy.findIndex(s => s.sessionKey === sessionKey)] = sessionCopy;
        setSessions(sessionsCopy);
    }, []);

    const handleChange = (event) => {
        setSelectedOption(event.target.value);
        let sessionsCopy = [...sessions];
        let sessionCopy = { ...session };
        sessionCopy.sourceOfTruth = event.target.value;
        sessionsCopy[sessionsCopy.findIndex(s => s.sessionKey === sessionKey)] = sessionCopy;
        setSessions(sessionsCopy);
    };

    return (
        <Box sx={{ mb: 5 }}>
            <FormControl sx={{ width: 300, mt: 2 }}>
                <InputLabel id="dropdown-label">Source of truth</InputLabel>
                <Select
                    labelId="dropdown-label"
                    label="Source of truth"
                    id="dropdown"
                    value={selectedOption}
                    onChange={handleChange}
                >
                    {session.acquiredSimulations.map((sim, index) => {
                        return <MenuItem key={index} value={sim}>{sim}</MenuItem>
                    })}
                </Select>
            </FormControl>
        </Box>
    );
}