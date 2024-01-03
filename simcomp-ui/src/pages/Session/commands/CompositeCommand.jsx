import React, { useState, useContext } from 'react';
import { Grid, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';
import axios from 'axios';
import { SessionContext } from '../../../contexts/Contexts';
import CommandTypeSelector from '../CommandTypeSelector';
import { RemoveCircle } from '@mui/icons-material';

export default function CompositeCommand(props) {
    const [bodies, setBodies] = useState([]);
    const sessionKey = props.sessionKey;
    const { addCommand } = useContext(SessionContext);

    function addToComposite(body) {
        setBodies([...bodies, body]);
    };

    function removeFromComposite(indexToRemove) {
        setBodies(bodies.filter((_, index) => index !== indexToRemove))
    }

    async function executeCommand() {
        console.log(bodies);
        const res = await axios.post(process.env.REACT_APP_API_URL + '/session/' + sessionKey + '/execution', {
            type: "COMPOSITE",
            commands: bodies
        });
        addCommand(sessionKey, res.data);
    };

    return (
        <div>
            <CommandTypeSelector addToComposite={addToComposite} composite={true} command={props.possibleCommands?.[0]} possibleCommands={props.possibleCommands} />
            <Grid>
                <Grid item xs={12} md={4}>
                    <TableContainer component={Paper}>
                        <Table sx={{ minWidth: 650 }} aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell>#</TableCell>
                                    <TableCell align="right">Type</TableCell>
                                    <TableCell align="right">Remove</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {bodies.map((b, index) => (
                                    <TableRow
                                        key={index}
                                        sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                    >
                                        <TableCell component="th" scope="row">
                                            {index + 1}
                                        </TableCell>
                                        <TableCell align="right">{b.type}</TableCell>
                                        <TableCell align="right" onClick={() => removeFromComposite(index)}><RemoveCircle /></TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Grid>
            </Grid>
            <Grid container sx={{ my: 2 }}>
                <Grid item xs='auto'>
                    <Button variant="contained" color="primary" onClick={executeCommand} fullWidth>
                        Execute
                    </Button>
                </Grid>
            </Grid>
        </div>
    );
};
