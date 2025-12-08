
export default function Table({ columns, rows }) {
  // Ensure that rows is always an array to avoid errors with .map
  const safeRows = Array.isArray(rows) ? rows : [];

  return (
    <table className="table">
      <thead>
        <tr>
          {columns.map((col) => (
            <th key={col.key}>{col.title}</th>
          ))}
        </tr>
      </thead>
      <tbody>
        {safeRows.map((row) => (  
          <tr key={row.key}>
            {columns.map((col) => (
              <td key={col.key}>{row[col.key]}</td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
}