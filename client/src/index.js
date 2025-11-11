import express from "express";
import currency_service from "./services/currency-service.js";

const PORT = 3000;

const server = express();

server.get("/api/currency/:prefix", async (req, res) => {
  const { prefix } = req.params;
  const [result, rawXml] = await currency_service.getCurrencyByPrefix(prefix);
  res.json({ result, rawResult: rawXml });
});

server.listen(PORT, () => {
  console.log("Server listening on localhost:" + PORT);
});
