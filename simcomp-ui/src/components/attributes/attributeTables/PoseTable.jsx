import React from 'react';
import { TableContainer, Table, TableHead, TableRow, TableCell, TableBody, Paper } from '@mui/material';

export default function PoseTable(props) {
    const data = props.data;
    const sim = props.sim;
    const sourceOfTruth = props.sourceOfTruth;

    function getColor(position) {
        if (sim === sourceOfTruth) {
            return 'green';
        } else {
            const sourceOfTruthPosition = data[sourceOfTruth].pose.position[position];
            const ownPosition = data[sim].pose.position[position];
            if (Math.abs(ownPosition - sourceOfTruthPosition) <= 0.1 * Math.abs(sourceOfTruthPosition)) {
                return 'green';
            } else {
                return 'red';
            }
        }
    }

    if (data != null && data[sim] != null && data[sim].pose) {
        return (
            <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell>X</TableCell>
                            <TableCell>Y</TableCell>
                            <TableCell>Z</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        <TableRow
                            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                        >
                            <TableCell sx={{ backgroundColor: getColor('x') }}>{data[sim].pose.position.x}</TableCell>
                            <TableCell sx={{ backgroundColor: getColor('y') }}>{data[sim].pose.position.y}</TableCell>
                            <TableCell sx={{ backgroundColor: getColor('z') }}>{data[sim].pose.position.z}</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </TableContainer>
        );
    } else {
        return (
            <h4>No data available.</h4>
        );
    }
};
