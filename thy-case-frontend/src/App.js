import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import CssBaseline from "@mui/material/CssBaseline";
import Divider from "@mui/material/Divider";
import Drawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemText from "@mui/material/ListItemText";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";

import LocationsPage from "./pages/LocationsPage";
import TransportationsPage from "./pages/TransportationsPage";
import RoutesPage from "./pages/RoutesPage";

const drawerWidth = 240;

export default function App() {
  return (
    <Router>
      <Box sx={{ display: "flex" }}>
        <CssBaseline />

        <AppBar position="fixed" sx={{ zIndex: (t) => t.zIndex.drawer + 1 }}>
          <Toolbar>
            <Typography variant="h6" noWrap>
              ThyCase UI
            </Typography>
          </Toolbar>
        </AppBar>

        <Drawer
          variant="permanent"
          sx={{
            width: drawerWidth,
            flexShrink: 0,
            "& .MuiDrawer-paper": {
              width: drawerWidth,
              boxSizing: "border-box",
            },
          }}
        >
          <Toolbar />
          <Divider />
          <List>
            <ListItem button component={Link} to="/locations">
              <ListItemText primary="Locations" />
            </ListItem>
            <ListItem button component={Link} to="/transportations">
              <ListItemText primary="Transportation" />
            </ListItem>
            <ListItem button component={Link} to="/routes">
              <ListItemText primary="Routes" />
            </ListItem>
          </List>
        </Drawer>

        <Box
          component="main"
          sx={{
            flexGrow: 1,
            p: 3,
            width: `calc(100% - ${drawerWidth}px)`,
          }}
        >
          <Toolbar />
          <Routes>
            <Route path="/locations" element={<LocationsPage />} />
            <Route path="/transportations" element={<TransportationsPage />} />
            <Route path="/routes" element={<RoutesPage />} />
            <Route path="*" element={<LocationsPage />} />
          </Routes>
        </Box>
      </Box>
    </Router>
  );
}
