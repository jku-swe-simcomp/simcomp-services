import React, { useState } from 'react';
import axios from 'axios';
import { FormControl, InputLabel, Select, MenuItem, FormGroup, FormControlLabel, Checkbox, Button, Grid, Typography, } from '@mui/material';

export default function MovementsList() {
    const [sessions, setSessions] = useState([]);
    const [newSessionName, setNewSessionName] = useState('');
    const [selectedOptions, setSelectedOptions] = useState([]);
    const [separateCheckbox, setSeparateCheckbox] = useState(false);

    async function getOptions() {
        const res = await axios.get(process.env.REACT_APP_API_URL + '/simulation');
        console.log(res);
    }

    const options = [
        { value: 'option1', label: 'Option 1' },
        { value: 'option2', label: 'Option 2' },
        { value: 'option3', label: 'Option 3' },
    ];

    const handleNewSessionChange = (e) => {
        setNewSessionName(e.target.value);
    };

    const handleOptionChange = (selected) => {
        setSelectedOptions(selected);
    };

    const handleSeparateCheckboxChange = (e) => {
        setSeparateCheckbox(e.target.checked);
    };

    const handleAddSession = () => {
        const newSession = {
            name: newSessionName,
            options: selectedOptions,
            separate: separateCheckbox,
        };

        setSessions([...sessions, newSession]);
        setNewSessionName('');
        setSelectedOptions([]);
        setSeparateCheckbox(false);
    };

    return (
        <>
            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <FormControl fullWidth>
                        <InputLabel htmlFor="sessionName">Enter session name</InputLabel>
                        <input
                            type="text"
                            id="sessionName"
                            placeholder="Enter session name"
                            value={newSessionName}
                            onChange={handleNewSessionChange}
                        />
                    </FormControl>
                </Grid>
                <Grid item xs={12}>
                    <FormControl fullWidth>
                        <InputLabel id="optionSelect">Select options</InputLabel>
                        <Select
                            labelId="optionSelect"
                            id="optionSelect"
                            multiple
                            value={selectedOptions}
                            onChange={(e) =>
                                handleOptionChange(Array.from(e.target.selectedOptions, (option) => option.value))
                            }
                            fullWidth
                        >
                            {options.map((option) => (
                                <MenuItem key={option.value} value={option.value}>
                                    {option.label}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </Grid>
                <Grid item xs={12}>
                    <FormGroup>
                        <FormControlLabel
                            control={
                                <Checkbox
                                    checked={separateCheckbox}
                                    onChange={handleSeparateCheckboxChange}
                                    name="separateCheckbox"
                                />
                            }
                            label="Separate Checkbox"
                        />
                    </FormGroup>
                </Grid>
                <Grid item xs={12}>
                    <Button variant="contained" color="primary" onClick={handleAddSession}>
                        Add Session
                    </Button>
                </Grid>
            </Grid>

            <Grid container>
                <Grid item xs={12}>
                    <Typography variant="h4" gutterBottom>
                        Sessions:
                    </Typography>
                    <ul>
                        {sessions.map((session, index) => (
                            <li key={index}>
                                <strong>Name:</strong> {session.name},{' '}
                                <strong>Options:</strong> {session.options.join(', ')},{' '}
                                <strong>Separate:</strong> {session.separate ? 'Yes' : 'No'}
                            </li>
                        ))}
                    </ul>
                </Grid>
            </Grid>
        </>
    );
};
