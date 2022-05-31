const express = require("express");
const contractRouter = require("./routes/contract.route");
const PORT = process.env.PORT || 8080;

const app = express();
app.use(function (req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header(
    "Access-Control-Allow-Headers",
    "Origin, X-Requested-With, Content-Type, Accept"
  );
  res.header("Access-Control-Allow-Methods", "PUT, POST, GET, DELETE, OPTIONS");
  next();
});

app.use(express.json());
app.use("/api", contractRouter);
app.listen(PORT, () => console.log(`Server started on port ${PORT}`));
