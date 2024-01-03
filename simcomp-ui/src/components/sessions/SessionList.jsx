import React, { useContext, useState } from 'react';
import AddSession from './AddSession';
import { Button, Card, CardContent, Typography, Grid, Divider, ButtonGroup } from '@mui/material';
import { SessionContext } from '../../contexts/Contexts';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import SimcompAlert from '../alerts/SimcompAlert';
import VisualizationLink from '../VisualizationLink';

export default function SessionList() {
    const [alerts, setAlerts] = useState([]);
    const { sessions, setSessions } = useContext(SessionContext);
    const navigate = useNavigate();

    async function handleAddSession(newSession) {
        let body = {};
        body.type = newSession.type;
        switch (newSession.type) {
            case 'ANY': body.n = newSession.requestedNrOfSims;
                break;
            case 'SELECTED_INSTANCES':
                body.requestedSimulationInstances = {};
                for (const instance of newSession.requestedInstances) {
                    body.requestedSimulationInstances[instance.type] = instance.name;
                }
                break;
            case 'SELECTED_TYPES':
                body.requestedSimulationTypes = newSession.requestedTypes;
                break;
            default: return;
        }
        try {
            const res = await axios.post(process.env.REACT_APP_API_URL + '/session', body);
            newSession.sessionKey = res.data.sessionKey;
            newSession.acquiredSimulations = res.data.acquiredSimulations;
            setSessions([...sessions, newSession]);
        } catch (e) {
            setAlerts([...alerts, {
                dismissible: true,
                severity: 'error',
                header: 'Something went wrong.',
                message: e.response.data.message,
                status: e.response.data.status
            }]);
        }
    };

    async function handleDeleteSession(session) {
        await axios.patch(process.env.REACT_APP_API_URL + '/session/' + session.sessionKey + '/close');
        setSessions(sessions.filter(s => s.sessionKey !== session.sessionKey));
    }

    function goToSession(sessionKey) {
        navigate('/session/' + sessionKey);
    }

    return (
        <div>
            {alerts.map((a, index) => (
                <SimcompAlert
                    key={index}
                    dismissible={a.dismissible}
                    severity={a.severity}
                    header={a.header}
                    message={a.message}
                >
                </SimcompAlert>
            ))}
            <AddSession addSession={handleAddSession} />
            {sessions.length === 0 ? null : (
                <div>
                    <Divider sx={{ marginY: 2 }} />
                    <Typography variant="h4" gutterBottom>Sessions:</Typography>
                    <Grid container spacing={2}>
                        {sessions.map((session, index) => (
                            <Grid item xs={12} key={index}>
                                <Card variant="outlined">
                                    <CardContent>
                                        <Typography variant="h6" component="h2">{session.sessionKey}</Typography>
                                        <Typography variant="body1" gutterBottom>Contained Simulation Instances:</Typography>
                                        {session.acquiredSimulations.map((sim, index) => (
                                            <Typography variant="body2" key={index}>{sim}</Typography>
                                        ))}
                                        <Grid container spacing={3}>
                                            <Grid item>
                                                <Button variant="contained" color="primary" onClick={() => goToSession(session.sessionKey)}>Go to session</Button>
                                            </Grid>
                                            <Grid item>
                                                <Button variant="contained" color="primary">
                                                    <VisualizationLink session={session} />
                                                </Button>
                                            </Grid>
                                            <Grid item>
                                                <Button variant="contained" color="error" onClick={() => handleDeleteSession(session)}>Close</Button>
                                            </Grid>
                                        </Grid>
                                    </CardContent>
                                </Card>
                            </Grid>
                        ))}
                    </Grid>
                </div>
            )}
        </div>
    );
};
