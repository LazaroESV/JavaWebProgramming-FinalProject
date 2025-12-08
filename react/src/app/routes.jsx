import { Routes, Route } from "react-router-dom";
import AppLayout from "./AppLayout";
import CardsList from "../pages/CardsList";
import CardDetail from "../pages/CardDetail";
import CollectorsList from "../pages/CollectorsList";
import CollectorDetail from "../pages/CollectorDetail";

export default function AppRoutes() {
  return (
    <Routes>
      <Route element={<AppLayout />}>
        {/* Default path */}
        <Route path="/" element={<CollectorsList />} />

        <Route path="/cards" element={<CardsList />} />
        <Route path="/cards/:id" element={<CardDetail />} />
        <Route path="/collectors" element={<CollectorsList />} />
        <Route path="/collectors/:id" element={<CollectorDetail />} />
      </Route>
    </Routes>
  );
}