// src/pages/RoutesPage.jsx
import React, { useState, useEffect } from "react";
import {
  Box,
  Button,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  TextField,
  List,
  ListItemButton,
  ListItemText,
  Dialog,
  DialogTitle,
  DialogContent,
  Stepper,
  Step,
  StepLabel,
  StepConnector,
  stepConnectorClasses,
  styled,
  Typography,
  Alert,
} from "@mui/material";
import RadioButtonUncheckedIcon from "@mui/icons-material/RadioButtonUnchecked";
import api from "../Api";

const DashedConnector = styled(StepConnector)(({ theme }) => ({
  [`& .${stepConnectorClasses.line}`]: {
    borderStyle: "dashed",
  },
}));

export default function RoutesPage() {
  const [locations, setLocations] = useState([]);
  const [form, setForm] = useState({
    originId: "",
    destinationId: "",
    date: "", // yyyy-MM-dd
  });
  const [errors, setErrors] = useState({});
  const [globalError, setGlobalError] = useState("");
  const [routes, setRoutes] = useState([]);
  const [selectedIdx, setSelectedIdx] = useState(null);

  const [searched, setSearched] = useState(false);

  useEffect(() => {
    api.get("/api/locations").then((r) => setLocations(r.data));
  }, []);

  const validate = () => {
    const e = {};
    if (!form.originId) e.originId = "Required";
    if (!form.destinationId) e.destinationId = "Required";
    if (form.originId === form.destinationId) e.destinationId = "Must differ";
    if (!form.date) e.date = "Required";
    setErrors(e);
    return !Object.keys(e).length;
  };

  const handleSearch = () => {
    if (!validate()) return;
    setSearched(true);
    setGlobalError("");
    api
      .post("/api/routes", {
        originId: form.originId,
        destinationId: form.destinationId,
        date: form.date,
      })
      .then((r) => {
        setRoutes(r.data);
        setSelectedIdx(null);
      })
      .catch((err) => {
        setGlobalError(err.response?.data?.message || "Error fetching routes");
      });
  };

  const flightLeg = (route) =>
    route.find((leg) => leg.type === "FLIGHT") || route[0];

  return (
    <Box>
      {globalError && <Alert severity="error">{globalError}</Alert>}

      <Box display="flex" gap={2} mb={3}>
        <FormControl error={!!errors.originId} sx={{ minWidth: 180 }}>
          <InputLabel>Origin</InputLabel>
          <Select
            label="Origin"
            value={form.originId}
            onChange={(e) =>
              setForm((f) => ({ ...f, originId: e.target.value }))
            }
          >
            {locations.map((loc) => (
              <MenuItem key={loc.id} value={loc.id}>
                {loc.name}
              </MenuItem>
            ))}
          </Select>
          {errors.originId && (
            <Typography color="error">{errors.originId}</Typography>
          )}
        </FormControl>

        <FormControl error={!!errors.destinationId} sx={{ minWidth: 180 }}>
          <InputLabel>Destination</InputLabel>
          <Select
            label="Destination"
            value={form.destinationId}
            onChange={(e) =>
              setForm((f) => ({ ...f, destinationId: e.target.value }))
            }
          >
            {locations.map((loc) => (
              <MenuItem key={loc.id} value={loc.id}>
                {loc.name}
              </MenuItem>
            ))}
          </Select>
          {errors.destinationId && (
            <Typography color="error">{errors.destinationId}</Typography>
          )}
        </FormControl>

        <TextField
          type="date"
          label="Date"
          InputLabelProps={{ shrink: true }}
          value={form.date}
          onChange={(e) => setForm((f) => ({ ...f, date: e.target.value }))}
          error={!!errors.date}
          helperText={errors.date}
        />

        <Button variant="contained" onClick={handleSearch}>
          Search
        </Button>
      </Box>

      <List>
        {routes.map((route, idx) => {
          const flight = flightLeg(route);
          return (
            <ListItemButton
              key={idx}
              selected={selectedIdx === idx}
              onClick={() => setSelectedIdx(idx)}
            >
              <ListItemText
                primary={`Via ${flight.originName} (${flight.originCode})`}
              />
            </ListItemButton>
          );
        })}
      </List>

      {searched && routes.length === 0 && !globalError && (
        <Alert severity="info">
          There is no route with the given parameters.
        </Alert>
      )}

      <Dialog
        open={selectedIdx !== null}
        onClose={() => setSelectedIdx(null)}
        fullWidth
        maxWidth="sm"
      >
        <DialogTitle>Route Details</DialogTitle>
        <DialogContent>
          {selectedIdx !== null && (
            <Stepper
              orientation="vertical"
              connector={<DashedConnector />}
              activeStep={-1}
            >
              {routes[selectedIdx].map((leg, i) => (
                <Step key={i}>
                  <StepLabel
                    StepIconComponent={() => <RadioButtonUncheckedIcon />}
                  >
                    <Box>
                      <Typography variant="body1">
                        {leg.originName} ({leg.originCode})
                      </Typography>
                      <Typography variant="caption">{leg.type}</Typography>
                    </Box>
                  </StepLabel>
                </Step>
              ))}
              <Step key="final">
                <StepLabel
                  StepIconComponent={() => <RadioButtonUncheckedIcon />}
                >
                  <Box>
                    <Typography variant="body1">
                      {routes[selectedIdx].slice(-1)[0].destinationName} (
                      {routes[selectedIdx].slice(-1)[0].destinationCode})
                    </Typography>
                  </Box>
                </StepLabel>
              </Step>
            </Stepper>
          )}
        </DialogContent>
        <Box textAlign="right" p={2}>
          <Button onClick={() => setSelectedIdx(null)}>Close</Button>
        </Box>
      </Dialog>
    </Box>
  );
}
