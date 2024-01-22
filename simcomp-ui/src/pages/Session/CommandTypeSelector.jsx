import { Grid, MenuItem, Select } from '@mui/material';
import { useState } from 'react';
import AdjustJointAngleForm from './commands/AdjustJointAngleForm';
import CompositeCommandForm from './commands/CompositeCommandForm';
import SetJointPositionForm from './commands/SetJointPositionForm';
import PoseCommandForm from './commands/PoseCommandForm';
import ResetToHomeForm from './commands/ResetToHomeForm';
import OpenHandForm from './commands/OpenHandForm';
import GrabCommandForm from './commands/GrabCommandForm';

export default function CommandTypeSelector(props) {
    let possibleCommands = props.possibleCommands;
    const sessionKey = props.sessionKey;
    const composite = props.composite;
    const [command, setCommand] = useState(props.possibleCommands?.[0]);

    if (composite) {
        possibleCommands = possibleCommands?.filter(c => c !== 'COMPOSITE_COMMAND');
    }
    return (
        <div>
            {composite ?
                <></> : <h4>Command Selection</h4>
            }
            <Grid container spacing={2} alignItems="center" marginBottom={3}>
                <Grid item xs={12} md={4}>
                    <Select
                        value={command}
                        variant="outlined"
                        fullWidth
                        onChange={(e) => setCommand(e.target.value)}
                    >
                        {possibleCommands.map((c, index) => (
                            <MenuItem key={index} value={c}>{c}</MenuItem>
                        ))}
                    </Select>
                </Grid>
            </Grid>
            {command === 'ADJUST_JOINT_ANGLE' ?
                <AdjustJointAngleForm addToComposite={props.addToComposite} composite={composite} sessionKey={sessionKey} />
                : <></>
            }
            {command === 'SET_JOINT_POSITION' ?
                <SetJointPositionForm addToComposite={props.addToComposite} composite={composite} sessionKey={sessionKey} />
                : <></>
            }
            {command === 'COMPOSITE_COMMAND' ?
                <CompositeCommandForm sessionKey={sessionKey} possibleCommands={possibleCommands} />
                : <></>
            }
            {command === 'POSE' ?
                <PoseCommandForm addToComposite={props.addToComposite} sessionKey={sessionKey} composite={composite} />
                : <></>
            }
            {command === 'RESET_TO_HOME' ?
                <ResetToHomeForm addToComposite={props.addToComposite} sessionKey={sessionKey} composite={composite} />
                : <></>
            }
            {command === 'OPEN_HAND' ?
                <OpenHandForm addToComposite={props.addToComposite} sessionKey={sessionKey} composite={composite} />
                : <></>
            }
            {command === 'GRAB' ?
                <GrabCommandForm addToComposite={props.addToComposite} sessionKey={sessionKey} composite={composite} />
                : <></>
            }
            <hr />
        </div>
    );

};
