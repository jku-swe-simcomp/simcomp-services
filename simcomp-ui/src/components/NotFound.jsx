import React from 'react';
import { Typography, Box } from '@mui/material';
import Navigation from './navigation/Navigation';

export default function NotFoundComponent() {
    return (
        <div>
            <Navigation />
            <Box textAlign="center" py={4}>
                <Typography variant="h5" gutterBottom>
                    Element Not Found
                </Typography>
                <Typography variant="body1">
                    Sorry, the requested element could not be found.
                </Typography>
            </Box>
        </div>

    );
};
