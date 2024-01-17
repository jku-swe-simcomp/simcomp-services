import React, { useState, useContext } from 'react';
import { Grid, TextField, Button } from '@mui/material';
import axios from 'axios';
import { SessionContext } from '../../../contexts/Contexts';

export default function PoseCommandForm(props) {
    const [x, setX] = useState(0);
    const [y, setY] = useState(0);
    const [z, setZ] = useState(0);
    const { addCommand } = useContext(SessionContext);
    const partOfCompositeCommand = props.composite;
    const sessionKey = props.sessionKey;

    async function executeCommand() {
        const body = {
            type: "POSE",
            position: {
                x: x,
                y: y,
                z: z
            },
            orientation: {
                x: 0,
                y: 0,
                z: 1
            }
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
            <Grid container spacing={2} alignItems="center" marginBottom={3}>
                <Grid item xs={12} md={4}>
                    <TextField
                        id="xInput"
                        type="number"
                        label="Enter X coordinate"
                        placeholder="Enter X coordinate"
                        value={x}
                        onChange={(e) => setX(e.target.value)}
                        variant="outlined"
                        fullWidth
                    />
                </Grid>
                <Grid item xs={12} md={4}>
                    <TextField
                        id="yInput"
                        type="number"
                        label="Enter Y coordinate"
                        placeholder="Enter Y coordinate"
                        value={y}
                        onChange={(e) => setY(e.target.value)}
                        variant="outlined"
                        fullWidth
                    />
                </Grid>
                <Grid item xs={12} md={4}>
                    <TextField
                        id="zInput"
                        type="number"
                        label="Enter Z coordinate"
                        placeholder="Enter Z coordinate"
                        value={z}
                        onChange={(e) => setZ(e.target.value)}
                        variant="outlined"
                        fullWidth
                    />
                </Grid>
            </Grid>
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
