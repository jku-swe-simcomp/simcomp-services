import React from 'react';
import ReactDOM from 'react-dom/client';
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';
import './index.css';
import { createBrowserRouter, RouterProvider, redirect } from "react-router-dom";
import Error from './pages/Error/Error';
import Home from './pages/Home/Home';
import Session from './pages/Session/Session';
import Wrapper from './Wrapper';
import { Container } from '@mui/material';
import { deleteAllInstances } from './helpers/helpers';

async function authorizedRouteLoader({ request }) {
  if (true) {
    return null;
  } else {
    return redirect('/login');
  }
}

const router = createBrowserRouter([
  {
    path: "/",
    element: <Home />,
    errorElement: <Error />,
    /* loader: authorizedRouteLoader */
  },
  {
    path: "/session/:sessionKey",
    element: <Session />,
    errorElement: <Error />,
  },
]);

console.log('Connecting to: ' + process.env.REACT_APP_API_URL);
await deleteAllInstances();
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Wrapper>
      <Container maxWidth='xl'>
        <RouterProvider router={router} />
      </Container>
    </Wrapper>
  </React.StrictMode>
);