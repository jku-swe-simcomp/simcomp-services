import React, { useState, useContext } from 'react';
import { Grid, MenuItem, Select, TextField, Button } from '@mui/material';
import axios from 'axios';
import { SessionContext } from '../../../contexts/Contexts';

export default function AdjustJointAngleForm(props) {
    const [selectedAxis, setSelectedAxis] = useState('1st');
    const [degrees, setDegrees] = useState(0);
    const partOfCompositeCommand = props.composite;
    const sessionKey = props.sessionKey;
    const { addCommand } = useContext(SessionContext);

    function handleAxisSelect(axis) {
        setSelectedAxis(axis);
    };

    function handleDegreesChange(e) {
        setDegrees(e.target.value);
    };

    function degreesToRadians(deg) {
        return deg * (Math.PI / 180);
    }

    async function executeCommand() {
        let axis;
        switch (selectedAxis) {
            case '1st': axis = 'AXIS_1';
                break;
            case '2nd': axis = 'AXIS_2';
                break;
            case '3rd': axis = 'AXIS_3';
                break;
            case '4th': axis = 'AXIS_4';
                break;
            case '5th': axis = 'AXIS_5';
                break;
            case '6th': axis = 'AXIS_6';
                break;
            default:
                return;
        }
        const body = {
            type: "ADJUST_JOINT_ANGLE",
            jointAngleAdjustment: {
                joint: axis,
                byRadians: degreesToRadians(degrees)
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
                    <Select
                        value={selectedAxis}
                        onChange={(event) => handleAxisSelect(event.target.value)}
                        variant="outlined"
                        fullWidth
                    >
                        <MenuItem value="1st">1st Axis</MenuItem>
                        <MenuItem value="2nd">2nd Axis</MenuItem>
                        <MenuItem value="3rd">3rd Axis</MenuItem>
                        <MenuItem value="4th">4th Axis</MenuItem>
                        <MenuItem value="5th">5th Axis</MenuItem>
                        <MenuItem value="6th">6th Axis</MenuItem>
                    </Select>
                </Grid>
                <Grid item xs={12} md={4}>
                    <TextField
                        id="degreesInput"
                        type="number"
                        label="Enter degrees"
                        placeholder="Enter degrees"
                        value={degrees}
                        onChange={handleDegreesChange}
                        variant="outlined"
                        fullWidth
                        InputProps={{
                            endAdornment: <span>&deg;</span>,
                        }}
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
