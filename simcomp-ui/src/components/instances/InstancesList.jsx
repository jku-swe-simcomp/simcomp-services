import axios from 'axios';
import AddInstance from './AddInstance.jsx';
import { Card, CardContent, Typography, Button, Grid, Divider, } from '@mui/material';
import { useContext, useState } from 'react';
import { InstanceContext } from '../../contexts/Contexts.js';
import AlertList from '../alerts/AlertList.jsx';

export default function InstancesList() {
    const [alerts, setAlerts] = useState([]);
    const { instances, setInstances } = useContext(InstanceContext);

    async function handleAddInstance(newInstance) {
        try {
            await axios.post(process.env.REACT_APP_API_URL + '/simulation/instance', {
                simulationType: newInstance.type,
                instanceId: newInstance.name,
                instanceHost: newInstance.host,
                instancePort: newInstance.port
            });
            setInstances([...instances, newInstance]);
        } catch (e) {
            setAlerts([...alerts, {
                dismissible: true,
                severity: 'error',
                header: 'Something went wrong.',
                message: e.response.data.message,
                status: e.response.data.status
            }])
        }
    };

    async function handleDeleteInstance(instance) {
        const res = await axios.delete(process.env.REACT_APP_API_URL + '/simulation/' + instance.type + '/instance/' + instance.name);
        if (res.status !== 200) {
            setAlerts([...alerts, {
                dismissible: true,
                severity: 'error',
                header: 'test',
                message: res.statusText
            }])
        }
        setInstances(instances.filter(i => i.name !== instance.name));
    }

    return (
        <div>
            <AlertList alerts={alerts} />
            <AddInstance addInstance={handleAddInstance} />
            {instances.length === 0 ? null : (
                <div>
                    <Divider sx={{ marginTop: 2, marginBottom: 2 }} />
                    <Typography variant="h4" gutterBottom>Instances:</Typography>
                    <Grid container spacing={2}>
                        {instances.map((instance, index) => (
                            <Grid item xs={8} key={index}>
                                <Card variant="outlined">
                                    <CardContent>
                                        <Typography variant="h6" component="h2">{instance.name}</Typography>
                                        <Typography variant="body1" gutterBottom>Instance Details:</Typography>
                                        <Typography variant="body2">Type: {instance.type}</Typography>
                                        <Typography variant="body2">Host: {instance.host}</Typography>
                                        <Typography variant="body2">Port: {instance.port}</Typography>
                                        <Button variant="contained" color="error" onClick={() => handleDeleteInstance(instance)}>Delete</Button>
                                    </CardContent>
                                </Card>
                            </Grid>
                        ))}
                    </Grid>
                </div>
            )
            }
        </div >
    );
};
