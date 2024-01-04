import React from 'react';
import { TableContainer, Table, TableHead, TableRow, TableCell, TableBody, Paper } from '@mui/material';

export default function JointPositionsTable(props) {
    const data = props.data;

    if (data != null && data.jointPositions != null) {
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
                            <TableCell>{data.jointPositions[0]}</TableCell>
                            <TableCell>{data.jointPositions[1]}</TableCell>
                            <TableCell>{data.jointPositions[2]}</TableCell>
                            <TableCell>{data.jointPositions[3]}</TableCell>
                            <TableCell>{data.jointPositions[4]}</TableCell>
                            <TableCell>{data.jointPositions[5]}</TableCell>
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
