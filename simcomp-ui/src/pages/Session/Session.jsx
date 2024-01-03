import Navigation from "../../components/navigation/Navigation";
import CommandHistory from "./CommandHistory";
import CommandTypeSelector from "./CommandTypeSelector";
import { useParams } from "react-router-dom";
import { useState, useContext, useEffect } from "react";
import { SessionContext } from "../../contexts/Contexts";
import NotFoundComponent from "../../components/NotFound";
import { CircularProgress } from "@mui/material";
import axios from "axios";

export default function Session() {
    const { sessionKey } = useParams();
    const [possibleCommands, setPossibleCommands] = useState([]);
    const [loading, setLoading] = useState(true);
    const session = useContext(SessionContext).sessions.find(s => s.sessionKey === sessionKey);

    function getIntersectionOfArrays(arrays) {
        return arrays.reduce((accumulator, currentArray) => {
            return accumulator.filter(value => currentArray.includes(value));
        });
    }

    useEffect(() => {
        async function getPossibleCommands() {
            const res = await axios.get(process.env.REACT_APP_API_URL + '/simulation/type');
            const simulations = res.data.availableSimulations.filter(s => session.acquiredSimulations.includes(s.name));
            const allCommands = [];
            for (const sim of simulations) {
                allCommands.push([...sim.supportedActions, 'COMPOSITE_COMMAND']);
            }
            const newPossibleCommands = getIntersectionOfArrays(allCommands);
            setPossibleCommands(newPossibleCommands);
            setLoading(false);
        }
        if (session != null) {
            getPossibleCommands();
        }
    }, []); // eslint-disable-line react-hooks/exhaustive-deps


    if (loading) {
        return <CircularProgress />;
    }
    if (session == null) {
        return <NotFoundComponent />;
    } else {
        return (
            <div>
                <Navigation />
                <h4 className="m-2">Session - {sessionKey}</h4>
                <hr />
                <CommandTypeSelector possibleCommands={possibleCommands} sessionKey={sessionKey} />
                <CommandHistory sessionKey={sessionKey}></CommandHistory>
            </div>
        );
    }

}
