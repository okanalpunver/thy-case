import React, { useState, useEffect } from "react";
import {
  Box,
  Button,
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

const TYPES = ["FLIGHT", "BUS", "SUBWAY", "UBER"];

export default function TransportationsPage() {
  const [data, setData] = useState([]);
  const [locations, setLocations] = useState([]);
  const [form, setForm] = useState({
    id: null,
    originId: "",
    destinationId: "",
    type: "",
    operatingDays: [],
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    api.get("/api/transportations").then((r) => setData(r.data));
    api.get("/api/locations").then((r) => setLocations(r.data));
  }, []);

  const validate = () => {
    const e = {};
    if (!form.originId) e.originId = "Required";
    if (!form.destinationId) e.destinationId = "Required";
    if (
      form.originId &&
      form.destinationId &&
      form.originId === form.destinationId
    )
      e.destinationId = "Must differ from origin";
    if (!form.type) e.type = "Required";
    if (!form.operatingDays.length) {
      e.operatingDays = "Select at least one day";
    }
    setErrors(e);
    return !Object.keys(e).length;
  };

  const reset = () => {
    setForm({
      id: null,
      originId: "",
      destinationId: "",
      type: "",
      operatingDays: [],
    });
    setErrors({});
  };

  const submit = () => {
    if (!validate()) return;
    const payload = {
      originId: form.originId,
      destinationId: form.destinationId,
      type: form.type,
      operatingDays: form.operatingDays,
    };
    const req = form.id
      ? api.put(`/api/transportations/${form.id}`, payload)
      : api.post("/api/transportations", payload);
    req.then(() => {
      api.get("/api/transportations").then((r) => {
        setData(r.data);
      });
      reset();
    });
  };

  const edit = (t) => {
    setForm({
      id: t.id,
      originId: t.originId,
      destinationId: t.destinationId,
      type: t.type,
      operatingDays: t.operatingDays || [],
    });
  };

  const remove = (id) => {
    api.delete(`/api/transportations/${id}`).then(() => {
      setData((d) => d.filter((x) => x.id !== id));
    });
  };

  return (
    <Box>
      <Box display="flex" gap={2} mb={2}>
        <FormControl error={!!errors.originId}>
          <InputLabel>Origin</InputLabel>
          <Select
            label="Origin"
            value={form.originId}
            onChange={(e) =>
              setForm((f) => ({ ...f, originId: e.target.value }))
            }
            sx={{ width: "10rem" }}
          >
            {locations.map((l) => (
              <MenuItem key={l.id} value={l.id}>
                {l.name} {l.cityName}/{l.countryIso2}
              </MenuItem>
            ))}
          </Select>
          {errors.originId && <Box color="error.main">{errors.originId}</Box>}
        </FormControl>
        <FormControl error={!!errors.destinationId}>
          <InputLabel>Destination</InputLabel>
          <Select
            label="Destination"
            value={form.destinationId}
            onChange={(e) =>
              setForm((f) => ({ ...f, destinationId: e.target.value }))
            }
            sx={{ width: "10rem" }}
          >
            {locations.map((l) => (
              <MenuItem key={l.id} value={l.id}>
                {l.name} {l.cityName}/{l.countryIso2}
              </MenuItem>
            ))}
          </Select>
          {errors.destinationId && (
            <Box color="error.main">{errors.destinationId}</Box>
          )}
        </FormControl>
        <FormControl error={!!errors.type}>
          <InputLabel>Type</InputLabel>
          <Select
            label="Type"
            value={form.type}
            onChange={(e) => setForm((f) => ({ ...f, type: e.target.value }))}
            sx={{ width: "10rem" }}
          >
            {TYPES.map((t) => (
              <MenuItem key={t} value={t}>
                {t}
              </MenuItem>
            ))}
          </Select>
          {errors.type && <Box color="error.main">{errors.type}</Box>}
        </FormControl>
        <FormControl error={!!errors.operatingDays}>
          <InputLabel>Days</InputLabel>
          <Select
            multiple
            label="Days"
            value={form.operatingDays}
            onChange={(e) => {
              let vals = e.target.value;
              if (typeof vals === "string") {
                vals = vals.split(",").map((v) => parseInt(v, 10));
              }
              setForm((f) => ({ ...f, operatingDays: vals }));
            }}
            renderValue={(vals) =>
              vals
                .map(
                  (d) =>
                    ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"][d - 1]
                )
                .join(", ")
            }
            sx={{ width: "12rem" }}
          >
            {[
              { value: 1, label: "Mon" },
              { value: 2, label: "Tue" },
              { value: 3, label: "Wed" },
              { value: 4, label: "Thu" },
              { value: 5, label: "Fri" },
              { value: 6, label: "Sat" },
              { value: 7, label: "Sun" },
            ].map((opt) => (
              <MenuItem key={opt.value} value={opt.value}>
                {opt.label}
              </MenuItem>
            ))}
          </Select>
          {errors.operatingDays && (
            <Box color="error.main">{errors.operatingDays}</Box>
          )}
        </FormControl>
        <Button variant="contained" onClick={submit}>
          {form.id ? "Update" : "Add"}
        </Button>
        <Button onClick={reset}>Clear</Button>
      </Box>

      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Origin</TableCell>
            <TableCell>Destination</TableCell>
            <TableCell>Type</TableCell>
            <TableCell>Actions</TableCell>
            <TableCell>Days</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.map((t) => (
            <TableRow key={t.id}>
              <TableCell>{t.originName}</TableCell>
              <TableCell>{t.destinationName}</TableCell>
              <TableCell>{t.type}</TableCell>
              <TableCell>
                {t.operatingDays
                  .map(
                    (d) =>
                      ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"][d - 1]
                  )
                  .join(", ")}
              </TableCell>
              <TableCell>
                <Button size="small" onClick={() => edit(t)}>
                  Edit
                </Button>
                <Button size="small" color="error" onClick={() => remove(t.id)}>
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
