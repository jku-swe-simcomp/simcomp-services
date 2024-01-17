import Navigation from "../../components/navigation/Navigation";
import SessionList from "../../components/sessions/SessionList";
import SessionAttributesList from "../../components/attributes/SessionAttributesList";
import InstancesList from "../../components/instances/InstancesList";
import { Box, Tab } from '@mui/material';
import { TabContext, TabList, TabPanel } from '@mui/lab';
import { useState } from "react";

export default function Home() {
  const [tabValue, setTabValue] = useState('1');

  function handleChange(event, newValue) {
    setTabValue(newValue);
  };

  return (
    <div>
      <Navigation />
      <Box sx={{ width: '100%', typography: 'body1' }}>
        <TabContext value={tabValue}>
          <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
            <TabList variant='scrollable' onChange={handleChange}>
              <Tab label="Overview" value="1" />
              <Tab label="Instances" value="2" />
              <Tab label="Anomaly Detection & Attributes" value="3" />
            </TabList>
          </Box>
          <TabPanel value="1">
            <SessionList />
          </TabPanel>
          <TabPanel value="2">
            <InstancesList />
          </TabPanel>
          <TabPanel value="3">
            <SessionAttributesList />
          </TabPanel>
        </TabContext>
      </Box>
    </div>
  );
}
