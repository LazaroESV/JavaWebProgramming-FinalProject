import { useEffect, useState } from "react";
import { CardsAPI } from "../services/cards";
import { CollectorsAPI } from "../services/collectors";
import ErrorBanner from "../components/ErrorBanner";
import FormField from "../components/FormField";
import Button from "../components/Button";

export default function CardEditor({ cardId, collectors: initialCollectors = [], onDone }) {
  const [problem, setProblem] = useState(null);
  const [collectors, setCollectors] = useState(initialCollectors);
  const [form, setForm] = useState({
    serialNumber: "",
    pokemon: "",
    grade: "",
    forSale: false,
    releaseYear: "",
    price: "",
    collectorId: ""
  });

  const editing = Boolean(cardId);

  useEffect(() => {
    if (!collectors || collectors.length === 0) {
      CollectorsAPI.list().then(setCollectors).catch(() => {});
    }
  }, []);

  useEffect(() => {
    if (!editing) return;
    CardsAPI.get(cardId).then(c => {
      setForm({
        serialNumber: c.serialNumber || "",
        pokemon: c.pokemon || "",
        grade: c.grade != null ? String(c.grade) : "",
        forSale: Boolean(c.forSale),
        releaseYear: c.releaseYear != null ? String(c.releaseYear) : "",
        price: c.price != null ? String(c.price) : "",
        collectorId: c.collector?.id ? String(c.collector.id) : ""
      });
    }).catch((e) => setProblem(e.problem || { title: "Load error", detail: String(e) }));
  }, [cardId]);

  function up(k, v) {
    setForm((s) => ({ ...s, [k]: v }));
  }

  async function submit(e) {
    e.preventDefault();
    setProblem(null);

    const payload = {
    serialNumber: (form.serialNumber || "").trim(),
    pokemon: (form.pokemon || "").trim(),
    grade: form.grade !== "" ? Number(form.grade) : null,
    forSale: Boolean(form.forSale),                       
    releaseYear: form.releaseYear !== "" ? Number(form.releaseYear) : null,
    price: form.price !== "" ? Number(form.price) : null,                 
    collectorId: form.collectorId != null ? Number(form.collectorId) : null
  };

    // Validate that all of the attributes are present
      // Serial Number
      if (!payload.serialNumber) {
        return setProblem({ title: "Validation Error", detail: "Serial number is required." });
      }
      if (payload.serialNumber.length > 40) {
        return setProblem({ title: "Validation Error", detail: "Serial number must be at most 40 characters." });
      }

      // Pokémon Name
      if (!payload.pokemon) {
        return setProblem({ title: "Validation Error", detail: "Pokémon name is required." });
      }
      if (payload.pokemon.length > 60) {
        return setProblem({ title: "Validation Error", detail: "Pokémon name must be at most 60 characters." });
      }

      // Grade (0–10 typical grading range)
      if (payload.grade == null || Number.isNaN(payload.grade)) {
        return setProblem({ title: "Validation Error", detail: "Grade is required and must be a number." });
      }
      if (payload.grade < 0 || payload.grade > 10) {
        return setProblem({ 
          title: "Validation Error", 
          detail: "Grade must be between 0.0 and 10.0." 
        });
      }

      // Release Year
      const currentYear = new Date().getFullYear() + 1;
      if (Number.isNaN(payload.releaseYear)) {
        return setProblem({ title: "Validation Error", detail: "Release year is required and must be a valid year." });
      }
      if (payload.releaseYear < 1950 || payload.releaseYear > currentYear) {
        return setProblem({
          title: "Validation Error",
          detail: `Release year must be between 1950 and ${currentYear}.`
        });
      }

      // Price
      if (Number.isNaN(payload.price)) {
        return setProblem({ title: "Validation Error", detail: "Price is required and must be a number." });
      }
      if (payload.price < 0) {
        return setProblem({ title: "Validation Error", detail: "Price must be zero or greater." });
      }

      // Collector (foreign key)
      if (!payload.collectorId || Number.isNaN(payload.collectorId)) {
        return setProblem({ title: "Validation Error", detail: "Collector must be selected." });
      }


    try {
       if (editing) {
      await CardsAPI.update(cardId, payload);
    } else {
      await CardsAPI.create(payload);        
    }
      onDone?.();
    } catch (e) {
      setProblem(e.problem || { title: "Save failed", detail: String(e) });
    }
  }

  return (
    <form onSubmit={submit}>
      <ErrorBanner problem={problem} />

      <div className="grid cols-2 gap-2">
        <FormField label="Serial number" value={form.serialNumber} onChange={(v) => up("serialNumber", v)} required />
        <FormField label="Pokemon" value={form.pokemon} onChange={(v) => up("pokemon", v)} required />
        <FormField label="Grade" type="number" value={form.grade} onChange={(v) => up("grade", v)} placeholder="e.g., 9.5" />
        <div className="field">
          <label>For sale</label>
          <select className="input" value={String(form.forSale)} onChange={(e) => up("forSale", e.target.value === "true")}>
            <option value="false">No</option>
            <option value="true">Yes</option>
          </select>
        </div>
        <FormField label="Release year" type="number" value={form.releaseYear} onChange={(v) => up("releaseYear", v)} />
        <FormField label="Price" type="number" value={form.price} onChange={(v) => up("price", v)} />
        <div className="field">
          <label>Collector</label>
          <select className="input" value={form.collectorId} onChange={(e) => up("collectorId", e.target.value)} required>
            <option value="">Select collector…</option>
            {collectors.map(o => (
              <option key={o.id} value={o.id}>
                {o.firstName} {o.lastName} — {o.address ?? ""}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div className="stack mt-4">
        <Button type="submit">{editing ? "Update" : "Create"}</Button>
        <Button type="button" variant="soft" onClick={() => onDone?.()}>Cancel</Button>
      </div>
    </form>
  );
}