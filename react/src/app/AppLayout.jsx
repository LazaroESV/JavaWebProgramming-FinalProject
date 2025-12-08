import { Link, Outlet } from "react-router-dom";

export default function AppLayout() {
  return (
    <div className="layout">
      <aside className="sidebar">
        <div className="brand">Card Database</div>
        <nav className="nav">
          <Link to="/cards">Cards</Link>
          <Link to="/collectors">Collectors</Link>
        </nav>
      </aside>

      <main className="content">
        <Outlet />
      </main>
    </div>
  );
}