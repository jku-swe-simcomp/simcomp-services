import React, { useEffect, useState } from 'react';
import SimcompAlert from '../alerts/SimcompAlert';
import { FormControl, InputLabel, CircularProgress, Select, MenuItem, TextField, Button, Grid, Typography, Link, } from '@mui/material';
import axios from 'axios';

export default function AddInstance(props) {
    const [adaptors, setAdaptors] = useState([]);
    const [loading, setLoading] = useState(true);
    const [type, setType] = useState('');
    const [name, setName] = useState('');
    const [host, setHost] = useState('');
    const [port, setPort] = useState('');

    useEffect(() => {
        async function getAdaptors() {
            try {
                const res = await axios.get(process.env.REACT_APP_API_URL + '/simulation/type');
                console.log(res);
                setAdaptors(res.data.availableSimulations);
            } catch (e) {
                console.error(e);
            }
            setLoading(false);
        }
        getAdaptors();
        const intervalID = setInterval(() => {
            getAdaptors();
        }, 10_000);
        return () => {
            clearInterval(intervalID);
        }
    }, []);

    function handleTypeChange(adaptor) {
        setType(adaptor);
    };

    function handleNameChange(name) {
        setName(name);
    }

    function handleHostChange(host) {
        setHost(host);
    };

    function handlePortChange(port) {
        setPort(port);
    };

    function handleAddInstance() {
        const newInstance = {
            type: type,
            name: name,
            host: host,
            port: port,
        };
        props.addInstance(newInstance);
    };

    if (loading) {
        return (
            <CircularProgress />
        )
    } else if (!loading && adaptors.length === 0) {
        return <SimcompAlert
            dismissible={false}
            severity='warning'
            header='No adaptors available.'
            message='There are no adaptors currently registered. Start an adaptor service to communicate with one of your simulations.
            You can find documentation on how to create an adaptor here: https://github.com/jku-swe-simcomp/simcomp-services#demo-adaptor'
        />
    }
    return (
        <div>
            <form>
                <Grid container direction="column" spacing={2}>
                    <Grid item>
                        <FormControl className='m-2'>
                            <InputLabel id="type-label">Type of simulation</InputLabel>
                            <Select
                                labelId="type-label"
                                id="type"
                                value={type}
                                label="Type of simulation"
                                onChange={(e) => handleTypeChange(e.target.value)}
                                displayEmpty
                            >
                                {adaptors.map((adaptor, index) => (
                                    <MenuItem key={index} value={adaptor.name}>
                                        {adaptor.name}
                                    </MenuItem>
                                ))}
                            </Select>
                            <Typography variant="body2">
                                If you don't see a type corresponding to your simulation, you need to{' '}
                                <Link target="_blank" rel="noopener noreferrer" href='https://github.com/jku-swe-simcomp/simcomp-services#demo-adaptor'>
                                    create an adaptor.
                                </Link>
                            </Typography>
                        </FormControl>
                    </Grid>
                    <Grid item>
                        <TextField
                            label="Name"
                            value={name}
                            onChange={(e) => handleNameChange(e.target.value)}
                            variant="outlined"
                            className="m-2"
                            helperText="The name must be unique."
                        />
                    </Grid>
                    <Grid item>
                        <TextField
                            label="Host"
                            value={host}
                            onChange={(e) => handleHostChange(e.target.value)}
                            variant="outlined"
                            className="m-2"
                            helperText="The host in combination with the port must also be unique."
                        />
                    </Grid>
                    <Grid item>
                        <TextField
                            label="Port"
                            value={port}
                            onChange={(e) => handlePortChange(e.target.value)}
                            variant="outlined"
                            className="m-2"
                        />
                    </Grid>
                    <Grid item>
                        <Button variant="contained" color="primary" onClick={handleAddInstance} className='m-2'>
                            Add Instance
                        </Button>
                    </Grid>
                </Grid>
            </form>
        </div >
    );
};
