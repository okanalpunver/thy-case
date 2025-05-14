import React from "react";
import ReactDOM from "react-dom/client";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import CssBaseline from "@mui/material/CssBaseline";
import Backdrop from "@mui/material/Backdrop";
import CircularProgress from "@mui/material/CircularProgress";
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";

import App from "./App";
import "./index.css";
import { LoadingProvider, useLoading } from "./context/LoadingContext";
import { ErrorProvider, useError } from "./context/ErrorContext";

const theme = createTheme({
  palette: {
    primary: { main: "#EF2E1F" },
    secondary: { main: "#0053A5" },
  },
});

function Root() {
  const { isLoading } = useLoading();
  const { error, clearError } = useError();

  return (
    <>
      <Backdrop
        open={isLoading}
        sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}
      >
        <CircularProgress color="inherit" />
      </Backdrop>

      <Snackbar
        open={!!error}
        autoHideDuration={6000}
        onClose={clearError}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert onClose={clearError} severity="error" sx={{ width: "100%" }}>
          {error}
        </Alert>
      </Snackbar>

      <App />
    </>
  );
}

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <ThemeProvider theme={theme}>
      <ErrorProvider>
        <LoadingProvider>
          <CssBaseline />
          <Root />
        </LoadingProvider>
      </ErrorProvider>
    </ThemeProvider>
  </React.StrictMode>
);
