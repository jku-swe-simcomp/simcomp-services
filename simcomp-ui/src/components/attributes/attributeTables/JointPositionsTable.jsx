import React from 'react';
import { TableContainer, Table, TableHead, TableRow, TableCell, TableBody, Paper } from '@mui/material';
import { radiansToDegrees } from '../../../helpers/helpers';

export default function JointPositionsTable(props) {
    const data = props.data;
    const sim = props.sim;
    const sourceOfTruth = props.sourceOfTruth;

    function getColor(position) {
        if (sourceOfTruth == null) return 'green';
        if (sim === sourceOfTruth) {
            return 'green';
        } else {
            const sourceOfTruthPosition = radiansToDegrees(data[sourceOfTruth].jointPositions[position]);
            const ownPosition = radiansToDegrees(data[sim].jointPositions[position]);
            if (Math.abs(ownPosition - sourceOfTruthPosition) <= 10) {
                return 'green';
            } else {
                return 'red';
            }
        }
    }

    if (data != null && data[sim] && data[sim].jointPositions != null) {
        return (
            <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell>Joint 1</TableCell>
                            <TableCell>Joint 2</TableCell>
                            <TableCell>Joint 3</TableCell>
                            <TableCell>Joint 4</TableCell>
                            <TableCell>Joint 5</TableCell>
                            <TableCell>Joint 6</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        <TableRow
                            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                        >
                            <TableCell sx={{ backgroundColor: getColor(0) }}>{radiansToDegrees(data[sim].jointPositions[0])}</TableCell>
                            <TableCell sx={{ backgroundColor: getColor(1) }}>{radiansToDegrees(data[sim].jointPositions[1])}</TableCell>
                            <TableCell sx={{ backgroundColor: getColor(2) }}>{radiansToDegrees(data[sim].jointPositions[2])}</TableCell>
                            <TableCell sx={{ backgroundColor: getColor(3) }}>{radiansToDegrees(data[sim].jointPositions[3])}</TableCell>
                            <TableCell sx={{ backgroundColor: getColor(4) }}>{radiansToDegrees(data[sim].jointPositions[4])}</TableCell>
                            <TableCell sx={{ backgroundColor: getColor(5) }}>{radiansToDegrees(data[sim].jointPositions[5])}</TableCell>
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
