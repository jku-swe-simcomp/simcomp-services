import React, { useContext, useEffect, useState } from 'react';
import { Box, Card, CardContent, Typography, Grid, CircularProgress } from '@mui/material';
import { SessionContext } from '../../contexts/Contexts';
import axios from 'axios';
import SimcompAlert from '../alerts/SimcompAlert';
import AttributeSelector from './AttributeSelector';
import JointPositionsTable from './attributeTables/JointPositionsTable';

export default function SessionAttributesList() {
    const [attribute, setAttribute] = useState('JOINT_POSITIONS');
    const [alerts, setAlerts] = useState([]);
    const [loading, setLoading] = useState(true);
    const { sessions, setSessions } = useContext(SessionContext);

    function handleAttributeChange(newAttribute) {
        setAttribute(newAttribute);
    }

    useEffect(() => {
        async function updateAttributes() {
            setLoading(true);
            const promises = [];
            for (const session of sessions) {
                promises.push(axios.get(process.env.REACT_APP_API_URL + '/session/' + session.sessionKey + '/attribute/' + attribute));
            }
            const results = await Promise.allSettled(promises);
            results.forEach((r, index) => {
                sessions[index].attribute = r;
            });
            setLoading(false);
        }
        updateAttributes();
    }, [attribute]);


    console.log(sessions[0]?.attribute?.value.data);
    return (
        <div>
            {alerts.map((a, index) => (
                <SimcompAlert
                    key={index}
                    dismissible={a.dismissible}
                    severity={a.severity}
                    header={a.header}
                    message={a.message}
                />
            ))}
            <AttributeSelector attribute={attribute} handleAttributeChange={handleAttributeChange} />
            {loading ?
                <CircularProgress /> :
                <div>
                    {sessions.length === 0 ? <h4>No sessions found.</h4> : (
                        <div>
                            <Typography variant="h4" gutterBottom>Sessions:</Typography>
                            <Grid container spacing={2}>
                                {sessions.map((session, index) => (
                                    <Grid item xs={12} key={index}>
                                        <Card variant="outlined">
                                            <CardContent>
                                                <Typography variant="h6" component="h2">{session.sessionKey}</Typography>
                                                <Typography variant="body1" gutterBottom>Instances:</Typography>
                                                {session.acquiredSimulations.map((sim, index) => (
                                                    <Box key={index}>
                                                        <h5>{sim}</h5>
                                                        {attribute === 'JOINT_POSITIONS' ?
                                                            <JointPositionsTable data={session.attribute?.value.data[sim]} />
                                                            :
                                                            <></>
                                                        }
                                                        {attribute === 'JOINT_STATES' ?
                                                            <></>
                                                            :
                                                            <></>
                                                        }
                                                        {attribute === 'POSITION' ?
                                                            <></>
                                                            :
                                                            <></>
                                                        }
                                                        {attribute === 'POSE' ?
                                                            <></>
                                                            :
                                                            <></>
                                                        }
                                                        {attribute === 'ORIENTATION' ?
                                                            <></>
                                                            :
                                                            <></>
                                                        }
                                                    </Box>
                                                ))}
                                            </CardContent>
                                        </Card>
                                    </Grid>
                                ))}
                            </Grid>
                        </div>
                    )}
                </div>
            }

        </div>
    );
};
