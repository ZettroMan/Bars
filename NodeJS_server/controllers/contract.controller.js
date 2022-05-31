const db = require('../db')

class ContractController {
  async createContract(req, res) {
    try {
      const { date, number, title } = req.body
      console.log('CREATE:')
      console.log(req.body)
      const newContract = await db.query(
        `INSERT INTO "contract" 
    (date, number, title) VALUES ($1, $2, $3) RETURNING *`,
        [date, number, title],
      )
      res.json(newContract.rows[0])
    } catch (error) {
      res.status(400).send({
        message: 'Error occurred!',
      })
    }
  }
  async getContracts(req, res) {
    console.log('Get all contracts')
    try {
      const contracts = await db.query(`SELECT * FROM "contract"`)
      res.json(contracts.rows)
    } catch (error) {
      res.status(400).send({
        message: 'Error occurred!',
      })
    }
  }

  async getOneContract(req, res) {
    try {
      const id = req.params.id
      const contract = await db.query(
        `SELECT * FROM "contract" WHERE id = $1`,
        [id],
      )
      res.json(contract?.rows[0])
    } catch (error) {
      res.status(400).send({
        message: 'Error occurred!',
      })
    }
  }

  async updateContract(req, res) {
    try {
      const { id, date, number, title } = req.body
      console.log('UPDATE:')
      console.log(req.body)
      const contract = await db.query(
        `UPDATE "contract" SET date = $2, number = $3, title = $4 
        WHERE id = $1 RETURNING *`,
        [id, date, number, title],
      )
      res.json(contract.rows[0])
    } catch (error) {
      res.status(400).send({
        message: 'Error occurred!',
      })
    }
  }

  async deleteContract(req, res) {
    try {
      const id = req.params.id
      console.log('DELETE contract with id = ' + id)
      const user = await db.query(`DELETE FROM "contract" WHERE id = $1`, [id])
      res.json(user.rows[0])
    } catch (error) {
      res.status(400).send({
        message: 'Error occurred!',
      })
    }
  }
}

module.exports = new ContractController()
