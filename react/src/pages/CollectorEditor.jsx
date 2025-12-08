import { useEffect, useState } from "react";
import { CollectorsAPI } from "../services/collectors";
import ErrorBanner from "../components/ErrorBanner";
import FormField from "../components/FormField";
import Button from "../components/Button";

export default function CollectorEditor({ collectorId, onDone }) {
  const [problem, setProblem] = useState(null);
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    address: ""
  });

  const editing = Boolean(collectorId);

  useEffect(() => {
    if (!editing) return;
    CollectorsAPI.get(collectorId).then((c) => {
      setForm({
        firstName: c.firstName || "",
        lastName: c.lastName || "",
        email: c.email || "",
        phone: c.phone || "",
        address: c.address || ""
      });
    }).catch((e) => setProblem(e.problem || { title: "Load error", detail: String(e) }));
  }, [collectorId]);

  function up(k, v) {
    setForm((s) => ({ ...s, [k]: v }));
  }

  async function submit(e) {
    e.preventDefault();
    setProblem(null);

    const payload = {
      firstName: (form.firstName || "").trim(),
      lastName: (form.lastName || "").trim(),
      email: (form.email || "").trim(),
      phone: (form.phone || "").trim(),
      address: (form.address || "").trim()
    };

    // Validate that all of the attributes are present
      // First name
      if (!payload.firstName) {
        return setProblem({ title: "Validation Error", detail: "First name is required." });
      }
      if (payload.firstName.length < 2) {
        return setProblem({ title: "Validation Error", detail: "First name must be at least 2 characters." });
      }

      // Last name
      if (!payload.lastName) {
        return setProblem({ title: "Validation Error", detail: "Last name is required." });
      }
      if (payload.lastName.length < 2) {
        return setProblem({ title: "Validation Error", detail: "Last name must be at least 2 characters." });
      }

      // Email
      if (!payload.email) {
        return setProblem({ title: "Validation Error", detail: "Email is required." });
      }
      const emailRegex = /^\S+@\S+\.\S+$/;
      if (!emailRegex.test(payload.email)) {
        return setProblem({ title: "Validation Error", detail: "Email format is invalid." });
      }

      // Phone
      if (!payload.phone) {
        return setProblem({ title: "Validation Error", detail: "Phone number is required." });
      }
      const phoneRegex = /^[0-9+\-()\s]+$/;
      if (!phoneRegex.test(payload.phone)) {
        return setProblem({ 
          title: "Validation Error", 
          detail: "Phone number may only contain digits, spaces, +, -, and parentheses." 
        });
      }
      if (payload.phone.replace(/\D/g, "").length < 7) {
        return setProblem({ 
          title: "Validation Error", 
          detail: "Phone number must contain at least 7 digits." 
        });
      }

      // Address
      if (!payload.address) {
        return setProblem({ title: "Validation Error", detail: "Address is required." });
      }
      if (payload.address.length < 5) {
        return setProblem({ title: "Validation Error", detail: "Address must be at least 5 characters." });
      }

    try {
      if (editing) {
        await CollectorsAPI.update(collectorId, payload);
      } else {
        await CollectorsAPI.create(payload);
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
        <FormField label="First name" value={form.firstName} onChange={(v) => up("firstName", v)} />
        <FormField label="Last name" value={form.lastName} onChange={(v) => up("lastName", v)} />
        <FormField label="Email" value={form.email} onChange={(v) => up("email", v)} />
        <FormField label="Phone" value={form.phone} onChange={(v) => up("phone", v)} />
        <FormField label="Address" value={form.address} onChange={(v) => up("address", v)} required />
      </div>

      <div className="stack mt-4">
        <Button type="submit">{editing ? "Update" : "Create"}</Button>
        <Button type="button" variant="soft" onClick={() => onDone?.()}>Cancel</Button>
      </div>
    </form>
  );
}
