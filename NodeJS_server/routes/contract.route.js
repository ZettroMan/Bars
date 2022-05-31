const Router = require("express");
const router = new Router();

const contractController = require("../controllers/contract.controller");

router.post("/contracts", contractController.createContract);
router.get("/contracts", contractController.getContracts);
router.get("/contracts/:id", contractController.getOneContract);
router.put("/contracts", contractController.updateContract);
router.delete("/contracts/:id", contractController.deleteContract);

module.exports = router;
