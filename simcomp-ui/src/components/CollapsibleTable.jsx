import { useState, Fragment } from 'react';
import Box from '@mui/material/Box';
import Collapse from '@mui/material/Collapse';
import IconButton from '@mui/material/IconButton';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';

function Row(props) {
  const { row, columns, nestedArrayColumns, nestedArrayName } = props;
  const [open, setOpen] = useState(false);

  return (
    <Fragment>
      <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
        <TableCell>
          <IconButton
            aria-label="expand row"
            size="small"
            onClick={() => setOpen(!open)}
          >
            {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </IconButton>
        </TableCell>
        {columns.map((c, index) => (
          <TableCell key={index} align="left">{row[c.field]}</TableCell>
        ))}
      </TableRow>
      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box sx={{ margin: 1 }}>
              <Typography variant="h6" gutterBottom component="div">
                {nestedArrayName.charAt(0).toUpperCase() + nestedArrayName.slice(1)}
              </Typography>
              <Table size="small" aria-label="purchases">
                <TableHead>
                  <TableRow>
                    {nestedArrayColumns.map((c, index) => (
                      <TableCell key={index}>{c.headerName}</TableCell>
                    ))}
                  </TableRow>
                </TableHead>
                <TableBody>
                  {row[nestedArrayName].map((r, index) => (
                    <TableRow key={index}>
                      {nestedArrayColumns.map((c, index2) => (
                        <TableCell key={index2} align="left">{r[c.field]}</TableCell>
                      ))}
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </Fragment>
  );
}

export default function CollapsibleTable(props) {
  const rows = props.rows;
  const columns = props.columns;
  const nestedArrayColumns = props.nestedArrayColumns;
  const nestedArrayName = props.nestedArrayName;

  return (
    <TableContainer component={Paper}>
      <Table aria-label="collapsible table">
        <TableHead>
          <TableRow>
            <TableCell>
              {nestedArrayName.charAt(0).toUpperCase() + nestedArrayName.slice(1)}
            </TableCell>
            {columns.map((c) => (
              <TableCell key={c.field}>{c.headerName}</TableCell>
            ))}
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row, index) => (
            <Row key={index} row={row} columns={columns} nestedArrayColumns={nestedArrayColumns} nestedArrayName={nestedArrayName} />
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
