import React, { useContext, useState } from 'react';
import { InstanceContext } from '../../contexts/Contexts';
import { Button, ButtonGroup, TextField, FormControlLabel, Checkbox, FormGroup, FormControl, FormLabel, Grid, } from '@mui/material';
import SimcompAlert from '../alerts/SimcompAlert';

export default function AddSession(props) {
    const [requestedNrOfSims, setRequestedNrOfSims] = useState('');
    const { instances } = useContext(InstanceContext);
    const [selectedInstances, setSelectedInstances] = useState([]);
    const [selectedTypes, setSelectedTypes] = useState([]);

    const options = [
        { label: 'Any Simulation', value: '1', type: 'ANY' },
        { label: 'Selected Instances', value: '2', type: 'SELECTED_INSTANCES' },
        { label: 'Selected Types', value: '3', type: 'SELECTED_TYPES' },
    ];
    const [option, setOption] = useState(options[0]);

    function handleOptionChange(option) {
        setOption(option);
    }

    function handleRequestedNrOfSimsChange(e) {
        setRequestedNrOfSims(e.target.value);
    };

    function handleSelectedInstancesChange(e) {
        const value = e.target.value;
        let instance;
        for (const i of instances) {
            if (i.name === value) {
                instance = i;
            }
        }
        //disallow selecting two or more instances of the same type
        if (selectedInstances.find(i => i.type === instance.type) == null) {
            setSelectedInstances([...selectedInstances, instance]);
        } else if (selectedInstances.some(i => i.name === instance.name)) {
            setSelectedInstances(selectedInstances.filter(i => i.name !== instance.name));
        }
    };

    function handleSelectedTypesChange(e) {
        const value = e.target.value;
        if (selectedTypes.includes(value)) {
            setSelectedTypes(selectedTypes.filter((name) => name !== value));
        } else {
            setSelectedTypes([...selectedTypes, value]);
        }
    }

    function getTypes() {
        return instances.reduce((acc, i) => {
            if (!acc.includes(i.type)) {
                acc.push(i.type);
            }
            return acc;
        }, []);
    }

    function handleAddSession() {
        let newSession = {};
        switch (option.type) {
            case 'ANY':
                if (requestedNrOfSims === '' || requestedNrOfSims <= 0) {
                    return;
                }
                newSession.requestedNrOfSims = requestedNrOfSims;
                break;
            case 'SELECTED_INSTANCES':
                if (selectedInstances.length === 0) {
                    return;
                }
                newSession.requestedInstances = selectedInstances;
                break;
            case 'SELECTED_TYPES':
                if (selectedTypes.length === 0) {
                    return;
                }
                newSession.requestedTypes = selectedTypes;
                break;
            default: return;
        }
        newSession.type = option.type;
        props.addSession(newSession);
    };

    if (instances.length === 0) {
        return <SimcompAlert
            dismissible={false}
            severity='warning'
            header='No instances available.'
            message='Add more instances to create another session.'
        />
    } else {
        return (
            <form>
                <Grid container direction="column" spacing={2}>
                    <Grid item>
                        <ButtonGroup variant="contained">
                            {options.map((o, index) => (
                                <Button
                                    key={index}
                                    variant={option.type === o.type ? 'contained' : 'outlined'}
                                    onClick={() => handleOptionChange(o)}
                                >
                                    {o.label}
                                </Button>
                            ))}
                        </ButtonGroup>
                    </Grid>
                    <Grid item>
                        {option.type === 'ANY' ? (
                            <TextField
                                className='m-2'
                                label="Enter # of simulations"
                                variant="outlined"
                                value={requestedNrOfSims}
                                onChange={handleRequestedNrOfSimsChange}
                            />
                        ) : null}
                    </Grid>
                    <Grid item>
                        {option.type === 'SELECTED_INSTANCES' ? (
                            <FormControl component="fieldset" className='m-2'>
                                <FormLabel component="legend">Available Instances:</FormLabel>
                                <FormGroup>
                                    {instances.map((instance, index) => (
                                        <FormControlLabel
                                            key={index}
                                            control={
                                                <Checkbox
                                                    checked={selectedInstances.some(i => i.name === instance.name)}
                                                    onChange={handleSelectedInstancesChange}
                                                    value={instance.name}
                                                />
                                            }
                                            label={instance.name + ' - ' + instance.type}
                                        />
                                    ))}
                                </FormGroup>
                            </FormControl>
                        ) : null}
                    </Grid>
                    <Grid item>
                        {option.type === 'SELECTED_TYPES' ? (
                            <FormControl component="fieldset" className='m-2'>
                                <FormLabel component="legend">Available Types:</FormLabel>
                                <FormGroup>
                                    {getTypes().map((type, index) => (
                                        <FormControlLabel
                                            key={index}
                                            control={
                                                <Checkbox
                                                    checked={selectedTypes.includes(type)}
                                                    onChange={handleSelectedTypesChange}
                                                    value={type}
                                                />
                                            }
                                            label={type}
                                        />
                                    ))}
                                </FormGroup>
                            </FormControl>
                        ) : null}
                    </Grid>
                    <Grid item>
                        <Button variant="contained" color="primary" onClick={handleAddSession}>
                            Add Session
                        </Button>
                    </Grid>
                </Grid>
            </form>
        )
    }
};
