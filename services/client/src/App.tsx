import "./app.css";

const App = () => {
  return (
    <main className="app">
      <h1>OCR Review Prototype</h1>
      <p>
        FoundationDB-backed OCR workflows orchestrated with Kestra. Use the root Makefile to build
        individual services or run <code>docker compose up</code> for the full stack.
      </p>
    </main>
  );
};

export default App;

