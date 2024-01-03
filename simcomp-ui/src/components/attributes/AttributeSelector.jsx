import { Grid, Select, MenuItem } from "@mui/material";

export default function AttributeSelector(props) {

    return (
        <div>
            <h3>Selected Attribute</h3>
            <Grid container spacing={2} alignItems="center" marginBottom={3}>
                <Grid item xs={12} md={4}>
                    <Select
                        value={props.attribute}
                        variant="outlined"
                        fullWidth
                        onChange={(e) => props.handleAttributeChange(e.target.value)}
                    >
                        <MenuItem value={'JOINT_STATES'}>JOINT_STATES</MenuItem>
                        <MenuItem value={'JOINT_POSITIONS'}>JOINT_POSITIONS</MenuItem>
                        <MenuItem value={'POSE'}>POSE</MenuItem>
                        <MenuItem value={'ORIENTATION'}>ORIENTATION</MenuItem>
                        <MenuItem value={'POSITION'}>POSITION</MenuItem>
                    </Select>
                </Grid>
            </Grid>
            <hr />
        </div>
    );

};
