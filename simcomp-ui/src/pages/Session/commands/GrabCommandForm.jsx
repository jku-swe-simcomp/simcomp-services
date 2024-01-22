import React, { useContext } from 'react';
import { Grid, Button } from '@mui/material';
import axios from 'axios';
import { SessionContext } from '../../../contexts/Contexts';

export default function GrabCommandForm(props) {
    const partOfCompositeCommand = props.composite;
    const sessionKey = props.sessionKey;
    const { addCommand } = useContext(SessionContext);

    async function executeCommand() {
        const body = {
            type: "GRAB",
        };
        if (partOfCompositeCommand) {
            props.addToComposite(body);
        } else {
            const res = await axios.post(process.env.REACT_APP_API_URL + '/session/' + sessionKey + '/execution', body);
            addCommand(sessionKey, res.data);
        }
    };

    return (
        <div>
            {partOfCompositeCommand ?
                <Grid container>
                    <Grid item xs='auto'>
                        <Button variant="contained" color="primary" onClick={executeCommand} fullWidth>
                            Add to Composite
                        </Button>
                    </Grid>
                </Grid>
                :
                <Grid container>
                    <Grid item xs='auto'>
                        <Button variant="contained" color="primary" onClick={executeCommand} fullWidth>
                            Execute
                        </Button>
                    </Grid>
                </Grid>
            }
        </div>
    );
};
