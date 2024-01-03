import { useState } from 'react';
import { Alert, Typography } from '@mui/material';

export default function SimcompAlert(props) {
  const [show, setShow] = useState(true);
  const severity = props.severity;
  const header = props.header;
  const message = props.message;
  const dismissible = props.dismissible;
  const status = props.status;

  if (show) {
    if (dismissible) {
      return (
        <Alert severity={severity} onClose={() => setShow(false)} sx={{ marginBottom: 2 }}>
          <Typography variant="subtitle1">{header}</Typography>
          <Typography variant="body1">{message}</Typography>
          {status != null ?
            <Typography variant="body1">Status: {status}</Typography>
            : <></>
          }
        </Alert>
      )
    } else {
      return (
        <Alert severity={severity} sx={{ marginBottom: 2 }}>
          <Typography variant="subtitle1">{header}</Typography>
          <Typography variant="body1">{message}</Typography>
          {status != null ?
            <Typography variant="body1">Status: {status}</Typography>
            : <></>
          }
        </Alert>
      )
    }
  } else {
    return null;
  }

}
