import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import Card from "../components/Card";
import ErrorBanner from "../components/ErrorBanner";
import { CardsAPI } from "../services/cards";

export default function CardDetail() {
  const { id } = useParams();
  const [card, setCard] = useState(null);
  const [problem, setProblem] = useState(null);

  useEffect(() => {
    if (!id) return;

    async function load() {
      try {
        const c = await CardsAPI.get(id);
        setCard(c);
        setProblem(null);
      } catch (e) {
        setProblem(e.problem || { title: "Load failed", detail: String(e) });
      }
    }
    load();
  }, [id]);

  if (problem) {
    return (
      <Card title="Card Details">
        <ErrorBanner problem={problem} />
      </Card>
    );
  }

  if (!card) return null;

  const collectorName =
    card.collector
      ? `${card.collector.firstName ?? ""} ${card.collector.lastName ?? ""}`.trim()
      : "—";

  return (
    <Card title={`Card — ${card.serialNumber}`}>
      <div className="stack">

        <div>
          <strong>Serial Number:</strong> {card.serialNumber || "—"}
        </div>

        <div>
          <strong>Pokémon:</strong> {card.pokemon || "—"}
        </div>

        <div>
          <strong>Grade:</strong>{" "}
          {card.grade != null ? Number(card.grade).toFixed(2) : "—"}
        </div>

        <div>
          <strong>For Sale:</strong> {card.forSale ? "Yes" : "No"}
        </div>

        <div>
          <strong>Release Year:</strong> {card.releaseYear || "—"}
        </div>

        <div>
          <strong>Price:</strong> {card.price != null ? `$${card.price}` : "—"}
        </div>

        <div>
          <strong>Collector:</strong>{" "}
          {card.collector ? (
            <Link to={`/collectors/${card.collector.id}`}>
              {collectorName}
            </Link>
          ) : (
            "—"
          )}
        </div>

        {/* Optional timestamps — displayed if backend returns them */}
        <div>
          <strong>Created At:</strong>{" "}
          {card.createdAt
            ? new Date(card.createdAt).toLocaleString()
            : "—"}
        </div>

        <div>
          <strong>Updated At:</strong>{" "}
          {card.updatedAt
            ? new Date(card.updatedAt).toLocaleString()
            : "—"}
        </div>

      </div>
    </Card>
  );
}