import React from 'react';
import { AppBar, Toolbar, Typography, Grid } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import './Navigation.css'

function Navigation(props) {
    const navigate = useNavigate();

    function goToHomepage() {
        navigate('/');
    }

    return (
        <AppBar position="static" color="default" elevation={1}>
            <Toolbar>
                <Grid container alignItems="center" justifyContent="space-between" wrap="nowrap">
                    <Grid item>
                        <Typography variant="h6" onClick={goToHomepage} sx={{ cursor: 'pointer' }}>
                            SIMCOMP
                        </Typography>
                    </Grid>
                    <Grid item>
                        <div className='mx-2'>
                            {/* Content for the right side */}
                        </div>
                        <div className='mx-2'>
                            {/* Content for the right side */}
                        </div>
                    </Grid>
                </Grid>
            </Toolbar>
        </AppBar>
    )

}

export default Navigation;