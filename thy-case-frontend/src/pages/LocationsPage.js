import React, { useState, useEffect } from "react";
import {
  Box,
  Button,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
} from "@mui/material";
import api from "../Api";

export default function LocationsPage() {
  const [locations, setLocations] = useState([]);
  const [countries, setCountries] = useState([]);
  const [cities, setCities] = useState([]);
  const [form, setForm] = useState({
    id: null,
    name: "",
    countryIso2: "",
    cityId: "",
    locationCode: "",
  });
  const [errors, setErrors] = useState({});

  useEffect(() => {
    api.get("/api/locations").then((r) => setLocations(r.data));
    api.get("/api/locations/countries").then((r) => setCountries(r.data));
  }, []);

  useEffect(() => {
    if (form.countryIso2) {
      api
        .get(`/api/locations/countries/${form.countryIso2}/cities`)
        .then((r) => setCities(r.data));
    } else {
      setCities([]);
      setForm((f) => ({ ...f, cityId: "" }));
    }
  }, [form.countryIso2]);

  const validate = () => {
    const e = {};
    if (!form.name) e.name = "Name is required";
    if (!form.countryIso2) e.countryIso2 = "Country is required";
    if (!form.cityId) e.cityId = "City is required";
    if (!/^[A-Z0-9]{3,5}$/.test(form.locationCode))
      e.locationCode = "3–5 uppercase letters or digits";
    setErrors(e);
    return !Object.keys(e).length;
  };

  const reset = () => {
    setForm({
      id: null,
      name: "",
      countryIso2: "",
      cityId: "",
      locationCode: "",
    });
    setErrors({});
  };

  const submit = () => {
    if (!validate()) return;
    const payload = {
      name: form.name,
      countryIso2: form.countryIso2,
      cityName: cities.find((c) => c.id === form.cityId).name,
      locationCode: form.locationCode,
    };
    const req = form.id
      ? api.put(`/api/locations/${form.id}`, payload)
      : api.post("/api/locations", payload);
    req.then(() => {
      api.get("/api/locations").then((r) => setLocations(r.data));
      reset();
    });
  };

  const edit = (loc) => {
    setForm({
      id: loc.id,
      name: loc.name,
      countryIso2: loc.countryIso2,
      cityId: loc.cityId,
      locationCode: loc.locationCode,
    });
  };

  const remove = (id) => {
    api.delete(`/api/locations/${id}`).then(() => {
      setLocations((locs) => locs.filter((l) => l.id !== id));
    });
  };

  return (
    <Box>
      <Box display="flex" gap={2} mb={2}>
        <TextField
          label="Name"
          value={form.name}
          onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))}
          error={!!errors.name}
          helperText={errors.name}
        />
        <FormControl error={!!errors.countryIso2}>
          <InputLabel>Country</InputLabel>
          <Select
            label="Country"
            value={form.countryIso2}
            onChange={(e) =>
              setForm((f) => ({ ...f, countryIso2: e.target.value }))
            }
            sx={{ width: "10rem" }}
          >
            {countries.map((c) => (
              <MenuItem key={c.iso2} value={c.iso2}>
                {c.name}
              </MenuItem>
            ))}
          </Select>
          {errors.countryIso2 && (
            <Box color="error.main">{errors.countryIso2}</Box>
          )}
        </FormControl>
        <FormControl error={!!errors.cityId} disabled={!form.countryIso2}>
          <InputLabel>City</InputLabel>
          <Select
            label="City"
            value={form.cityId}
            onChange={(e) => setForm((f) => ({ ...f, cityId: e.target.value }))}
            sx={{ width: "10rem" }}
          >
            {cities.map((c) => (
              <MenuItem key={c.id} value={c.id}>
                {c.name}
              </MenuItem>
            ))}
          </Select>
          {errors.cityId && <Box color="error.main">{errors.cityId}</Box>}
        </FormControl>
        <TextField
          label="Code"
          value={form.locationCode}
          onChange={(e) =>
            setForm((f) => ({
              ...f,
              locationCode: e.target.value.toUpperCase(),
            }))
          }
          error={!!errors.locationCode}
          helperText={errors.locationCode}
        />
        <Button variant="contained" onClick={submit}>
          {form.id ? "Update" : "Add"}
        </Button>
        <Button onClick={reset}>Clear</Button>
      </Box>

      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Country</TableCell>
            <TableCell>City</TableCell>
            <TableCell>Code</TableCell>
            <TableCell>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {locations.map((loc) => (
            <TableRow key={loc.id}>
              <TableCell>{loc.name}</TableCell>
              <TableCell>{loc.countryIso2}</TableCell>
              <TableCell>{loc.cityName}</TableCell>
              <TableCell>{loc.locationCode}</TableCell>
              <TableCell>
                <Button size="small" onClick={() => edit(loc)}>
                  Edit
                </Button>
                <Button
                  size="small"
                  color="error"
                  onClick={() => remove(loc.id)}
                >
                  Delete
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Box>
  );
}
