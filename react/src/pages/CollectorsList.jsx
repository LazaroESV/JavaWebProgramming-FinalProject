import { useEffect, useState } from "react";
import Card from "../components/Card";
import Table from "../components/Table";
import Button from "../components/Button";
import Modal from "../components/Modal";
import EmptyState from "../components/EmptyState";
import ErrorBanner from "../components/ErrorBanner";
import { CollectorsAPI } from "../services/collectors";
import CollectorEditor from "./CollectorEditor";
import { Link } from "react-router-dom";

export default function CollectorsList() {
  const [collectors, setCollectors] = useState([]);
  const [problem, setProblem] = useState(null);
  const [openEditor, setOpenEditor] = useState(false);
  const [editId, setEditId] = useState(null);
  const pageSize = 8;
  const [page, setPage] = useState(1);

  const [searchText, setSearchText] = useState("");
  const [searchField, setSearchField] = useState("all");

  async function load() {
      try {
        const data = await CollectorsAPI.list();
        
        setCollectors(Array.isArray(data) ? data : []);
        setProblem(null);

      } catch (e) {
        setProblem(e.problem || { title: "Load error", detail: String(e) });

        // Fallback to empty array
        setCollectors([]);
      }
  }

  useEffect(() => { load(); }, []);

  useEffect(() => { setPage(1); }, [collectors]);

  function collectorFullName(c) {
      return `${c.firstName ?? ""} ${c.lastName ?? ""}`.trim() || "—";
  }

  async function runSearch() {
      const t = (searchText || "").trim();
      if (!t) {
        return load();
      }

    // Try server-side search first
    try {
      const q = {};
      if (searchField === "all") q.q = t;
      else if (searchField === "name") q.name = t;
      else if (searchField === "email") q.email = t;
      else if (searchField === "address") q.address = t;

      const data = await CollectorsAPI.search(q);
      setCollectors(data);
      setProblem(null);
    } catch (e) {

      // Fallback to client-side filtering if server-side search fails
      try {
        const all = await CollectorsAPI.list();
        const tLower = t.toLowerCase();
        const filtered = all.filter((c) =>
          (collectorFullName(c) + " " + (c.email || "") + " " + (c.address || ""))
            .toLowerCase()
            .includes(tLower)
        );
        setCollectors(filtered);
        setProblem(null);
      } catch (fallbackError) {
        setProblem(e.problem || { title: "Search error", detail: String(e) });
      }
    }
  }

  // Search 
  useEffect(() => {
    const h = setTimeout(runSearch, 250);
    return () => clearTimeout(h);
  }, [searchText, searchField]);

  // Display only a portion of collectors
  const total = Array.isArray(collectors) ? collectors.length : 0;
  const start = (page - 1) * pageSize;
  const end = Math.min(start + pageSize, total);
  const pageItems = Array.isArray(collectors) ? collectors.slice(start, end) : [];

  async function del(id) {
      if (!confirm("Delete this collector?")) return;

      try {
        await CollectorsAPI.remove(id);
        await load();
      } catch (e) {
        setProblem(e.problem || { title: "Delete failed", detail: String(e) });
      }
  }

  return (
    <Card title="Collectors" right={
      <div>
        <Button onClick={() => { setEditId(null); setOpenEditor(true); }}>Add Collector</Button>
      </div>
    }>
      <ErrorBanner problem={problem} />
      <div className="stack">
        <div className="row">
          <select value={searchField} onChange={(e) => setSearchField(e.target.value)}>
            <option value="all">All</option>
            <option value="name">Name</option>
            <option value="email">Email</option>
            <option value="address">Address</option>
          </select>
          <input className="input" placeholder="Search…" value={searchText} onChange={(e) => setSearchText(e.target.value)} />
        </div>
      </div>

      {collectors.length === 0 ? (
        <EmptyState message="No collectors yet — add one to get started." />
      ) : (
        <>
          <Table
            columns={[
              { title: "Name", key: "name" },
              { title: "Email", key: "email" },
              { title: "Phone", key: "phone" },
              { title: "Address", key: "address" },
              { title: "Actions", key: "actions" }
            ]}
            rows={pageItems.map((c) => ({
              key: c.id,
              name: <Link to={`/collectors/${c.id}`}>{collectorFullName(c)}</Link>,
              email: c.email || "—",
              phone: c.phone || "—",
              address: c.address || "—",
              actions: (
                <div className="btn-row">
                  <Button onClick={() => { setEditId(c.id); setOpenEditor(true); }}>Edit</Button>
                  <Button variant="danger" onClick={() => del(c.id)}>Delete</Button>
                </div>
              )
            }))}
          />

          <div className="spread mt-3">
            <div>Showing {start + 1}–{end} of {total}</div>
            <div>
              <Button onClick={() => setPage(1)} disabled={page === 1}>First</Button>
              <Button onClick={() => setPage((p) => Math.max(1, p - 1))} disabled={page === 1}>Prev</Button>
              <Button onClick={() => setPage((p) => p + 1)} disabled={end >= total}>Next</Button>
              <Button onClick={() => setPage(Math.ceil(total / pageSize))} disabled={end >= total}>Last</Button>
            </div>
          </div>
        </>
      )}

      <Modal open={openEditor} title={editId ? "Edit Collector" : "Add Collector"} onClose={() => setOpenEditor(false)}>
        <CollectorEditor
          collectorId={editId}
          onDone={() => { setOpenEditor(false); load(); }}
        />
      </Modal>
    </Card>
  );
}