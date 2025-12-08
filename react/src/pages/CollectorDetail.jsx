import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import Card from "../components/Card";
import { CollectorsAPI } from "../services/collectors";

export default function CollectorDetail() {
  const { id } = useParams();
  const [collector, setCollector] = useState(null);
  const [cards, setCards] = useState([]);
  const [problem, setProblem] = useState(null);

  useEffect(() => {
    if (!id) return;
    async function load() {
      try {
        const c = await CollectorsAPI.get(id);
        setCollector(c);
        const cs = await CollectorsAPI.cardsByCollector(id);
        setCards(cs || []);
        setProblem(null);
      } catch (e) {
        setProblem(e.problem || { title: "Load failed", detail: String(e) });
      }
    }
    load();
  }, [id]);

  if (!collector && !problem) return null;

  return (
    <Card title={`${collector?.firstName ?? ""} ${collector?.lastName ?? ""}`}>
      {problem && <div className="error">{problem.title}: {problem.detail}</div>}

      <div className="stack">
        <div><strong>Email:</strong> {collector?.email || "—"}</div>
        <div><strong>Phone:</strong> {collector?.phone || "—"}</div>
        <div><strong>Address:</strong> {collector?.address || "—"}</div>
      </div>

      <h3 className="mt-4">Cards</h3>
      {cards.length === 0 ? (
        <p>No cards for this collector.</p>
      ) : (
        <ul>
          {cards.map(c => (
            <li key={c.id}>
              <Link to={`/cards/${c.id}`}>{c.serialNumber} — {c.pokemon}</Link>
              &nbsp;• grade: {c.grade} • ${c.price} • {c.forSale ? "For sale" : "Not for sale"}
            </li>
          ))}
        </ul>
      )}
    </Card>
  );
}