import { useContext, useEffect, useState } from "react";
import axios from 'axios';
import { SessionContext } from "../../contexts/Contexts";
import AlertList from "../../components/alerts/AlertList";
import CollapsibleTable from "../../components/CollapsibleTable";
import { Box } from "@mui/material";

export default function CommandHistory(props) {
  const [alerts, setAlerts] = useState([]);
  const [commandStati, setCommandStati] = useState([]);
  const sessionKey = props.sessionKey;
  const session = useContext(SessionContext).sessions.find(s => s.sessionKey === sessionKey);

  useEffect(() => {
    async function updateCommands() {
      const promises = [];
      const commands = session.commands ? session.commands : [];
      for (const command of commands) {
        promises.push(axios.get(process.env.REACT_APP_API_URL + '/session/execution/' + command));
      }
      const results = await Promise.allSettled(promises);
      setCommandStati(results);
    }
    updateCommands();
    const intervalID = setInterval(() => {
      updateCommands();
    }, 10_000);
    return () => {
      clearInterval(intervalID);
    }
  }, [session]); // eslint-disable-line react-hooks/exhaustive-deps

  const columns = [
    { field: 'id', headerName: 'ID', width: 90 },
    { field: 'command', headerName: 'Command', width: 90 },
    { field: 'createdAt', headerName: 'Created At', width: 90 },
    { field: 'httpCode', headerName: 'Response Code', width: 90 },
  ];

  const nestedArrayColumns = [
    { field: 'simulationName', headerName: 'Type', width: 90 },
    { field: 'state', headerName: 'State', width: 90 },
    { field: 'report', headerName: 'Report', width: 90 },
    { field: 'responseCode', headerName: 'Response Code', width: 90 },
  ];

  function getRows() {
    const rows = [];
    if (session?.commands == null)
      return rows;
    session.commands.forEach((c, index) => {
      if (commandStati[index] == null) return;
      const commandStatus = commandStati[index];
      if (commandStatus.status === 'rejected') {
        rows.push({
          id: c,
          command: commandStatus.value?.data.command,
          createdAt: commandStatus.value?.data.createdAt,
          status: commandStatus.message,
          httpCode: commandStatus.status
        });
      } else {
        let sentCommand;
        try {
          sentCommand = JSON.parse(commandStatus.value?.data.command);
        } catch (e) {
          console.log(e);
        }
        rows.push({
          id: c,
          command: sentCommand.type,
          createdAt: commandStatus.value?.data.createdAt,
          httpCode: commandStatus.status,
          responses: commandStatus.value?.data.responses
        });
      }
    });
    return rows;
  }

  return (
    <Box sx={{ mb: 5 }}>
      <AlertList alerts={alerts} />
      <h4>Executed Commands</h4>
      <CollapsibleTable
        rows={getRows()}
        columns={columns}
        nestedArrayColumns={nestedArrayColumns}
        nestedArrayName={'responses'}
      />
    </Box>
  );
}
