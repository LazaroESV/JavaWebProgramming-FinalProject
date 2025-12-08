import { useEffect, useState } from "react";
import Card from "../components/Card";
import Table from "../components/Table";
import Button from "../components/Button";
import Modal from "../components/Modal";
import EmptyState from "../components/EmptyState";
import ErrorBanner from "../components/ErrorBanner";
import { CardsAPI } from "../services/cards";
import { CollectorsAPI } from "../services/collectors";
import CardEditor from "./CardEditor";
import { Link } from "react-router-dom";

function parseRange(text) {
  
  const t = (text || "").trim();
  if (!t) return { min: null, max: null };
  
  const ge = t.match(/^>=\s*(\d+(\.\d+)?)$/);
  if (ge) return { min: Number(ge[1]), max: null };
  const le = t.match(/^<=\s*(\d+(\.\d+)?)$/);
  if (le) return { min: null, max: Number(le[1]) };
  const rng = t.match(/^(\d+(\.\d+)?)\s*-\s*(\d+(\.\d+)?)$/);
  if (rng) return { min: Number(rng[1]), max: Number(rng[3]) };
  const n = Number(t);
  if (!Number.isNaN(n)) return { min: n, max: n };
  return { min: null, max: null };
}

export default function CardsList() {
  const [cards, setCards] = useState([]);
  const [collectors, setCollectors] = useState([]);
  const [problem, setProblem] = useState(null);
  const [openEditor, setOpenEditor] = useState(false);
  const [editId, setEditId] = useState(null);

  const pageSize = 10;
  const [page, setPage] = useState(1);

  const [searchField, setSearchField] = useState("all");
  const [searchText, setSearchText] = useState("");

  async function load() {
    try {
      const [cList, colList] = await Promise.all([CardsAPI.list(), CollectorsAPI.list()]);

      setCards(Array.isArray(cList) ? cList : []);
      setCollectors(Array.isArray(colList) ? colList : []);

      setProblem(null);
    } catch (e) {
      setProblem(e.problem || { title: "Load failed", detail: String(e) });

      // Fallback to empty arrays
      setCards([]);
      setCollectors([]);
    }
  }

  useEffect(() => { load(); }, []);
  useEffect(() => { setPage(1); }, [cards]);

  async function runSearch() {
    const t = (searchText || "").trim();
    if (!t) {
      return load();
    }

    const tryServerSearch = async () => {
      const q = {};
      switch (searchField) {
        case "serial": q.serialNumberContains = t; break;
        case "pokemon": q.pokemonContains = t; break;
        case "collector": q.collectorName = t; break;
        case "forSale": q.forSale = t.toLowerCase() === "true"; break;
        case "year": {
          const { min, max } = parseRange(t);
          if (min != null) q.minReleaseYear = min;
          if (max != null) q.maxReleaseYear = max;
          break;
        }
        case "price": {
          const { min, max } = parseRange(t);
          if (min != null) q.minPrice = min;
          if (max != null) q.maxPrice = max;
          break;
        }
        case "grade": {
          const { min, max } = parseRange(t);
          if (min != null) q.minGrade = min;
          if (max != null) q.maxGrade = max;
          break;
        }
        default: q.q = t;
      }
      return await CardsAPI.search(q);
    };

    // Try server-side search first
    try {
      const data = await tryServerSearch();
      setCards(data || []);
      setProblem(null);
    } catch (e) {
      
      // Fallback to client-side filtering if server-side search fails
      try {
        const all = await CardsAPI.list();
        const tLower = t.toLowerCase();
        let filtered = all;
        switch (searchField) {
          case "serial":
            filtered = all.filter(c => (c.serialNumber || "").toLowerCase().includes(tLower));
            break;
          case "pokemon":
            filtered = all.filter(c => (c.pokemon || "").toLowerCase().includes(tLower));
            break;
          case "collector":
            filtered = all.filter(c => ((c.collector?.firstName || "") + " " + (c.collector?.lastName || "")).toLowerCase().includes(tLower));
            break;
          case "forSale":
            const want = tLower === "true";
            filtered = all.filter(c => Boolean(c.forSale) === want);
            break;
          case "year": {
            const { min, max } = parseRange(t);
            filtered = all.filter(c => {
              const y = Number(c.releaseYear) || 0;
              return (min == null || y >= min) && (max == null || y <= max);
            });
            break;
          }
          case "price": {
            const { min, max } = parseRange(t);
            filtered = all.filter(c => {
              const p = Number(c.price) || 0;
              return (min == null || p >= min) && (max == null || p <= max);
            });
            break;
          }
          case "grade": {
            const { min, max } = parseRange(t);
            filtered = all.filter(c => {
              const g = Number(c.grade) || 0;
              return (min == null || g >= min) && (max == null || g <= max);
            });
            break;
          }
          default:
            filtered = all.filter(c =>
              `${c.serialNumber || ""} ${c.pokemon || ""} ${(c.collector?.firstName || "")} ${(c.collector?.lastName || "")}`
                .toLowerCase()
                .includes(tLower)
            );
        }
        setCards(filtered);
        setProblem(null);
      } catch (fallbackError) {
        setProblem(e.problem || { title: "Search error", detail: String(e) });
      }
    }
  }

  // Search 
  useEffect(() => {
    const handle = setTimeout(runSearch, 250);
    return () => clearTimeout(handle);
  }, [searchText, searchField]);

  // Display only a portion of cards
  const total = Array.isArray(cards) ? cards.length : 0; 
  const start = (page - 1) * pageSize;
  const end = Math.min(start + pageSize, total);
  const pageItems = Array.isArray(cards) ? cards.slice(start, end) : [];

  async function del(id) {
    if (!confirm("Delete this card?")) return;
    try {
      await CardsAPI.remove(id);
      await load();
    } catch (e) {
      setProblem(e.problem || { title: "Delete failed", detail: String(e) });
    }
  }

  function collectorName(c) {
    return c.collector ? `${c.collector.firstName || ""} ${c.collector.lastName || ""}`.trim() : "—";
  }

  return (
    <Card title="Cards" right={<Button onClick={() => { setEditId(null); setOpenEditor(true); }}>Add Card</Button>}>
      <ErrorBanner problem={problem} />

      <div className="stack">
        <div className="row">
          <select value={searchField} onChange={(e) => setSearchField(e.target.value)}>
            <option value="all">All</option>
            <option value="serial">Serial</option>
            <option value="pokemon">Pokemon</option>
            <option value="collector">Collector</option>
            <option value="forSale">For sale (true/false)</option>
            <option value="grade">Grade (range)</option>
            <option value="year">Release year</option>
            <option value="price">Price</option>
          </select>
          <input className="input" placeholder="Search…" value={searchText} onChange={(e) => setSearchText(e.target.value)} />
        </div>
      </div>

      {cards.length === 0 ? (
        <EmptyState message="No cards yet — add one to get started." />
      ) : (
        <>
          <Table
            columns={[
              { title: "Serial", key: "serialNumber" },
              { title: "Pokemon", key: "pokemon" },
              { title: "Grade", key: "grade" },
              { title: "For Sale", key: "forSale" },
              { title: "Year", key: "releaseYear" },
              { title: "Price", key: "price" },
              { title: "Collector", key: "collector" },
              { title: "Actions", key: "actions" }
            ]}
            rows={pageItems.map(c => ({
              key: c.id,
              serialNumber: <Link to={`/cards/${c.id}`}>{c.serialNumber}</Link>,
              pokemon: c.pokemon,
              grade: Number(c.grade).toFixed(2),
              forSale: c.forSale ? "Yes" : "No",
              releaseYear: c.releaseYear,
              price: `$${c.price}`,
              collector: collectorName(c),
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

      <Modal open={openEditor} title={editId ? "Edit Card" : "Add Card"} onClose={() => setOpenEditor(false)}>
        <CardEditor
          cardId={editId}
          collectors={collectors}
          onDone={() => { setOpenEditor(false); load(); }}
        />
      </Modal>
    </Card>
  );
}