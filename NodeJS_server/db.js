const Pool = require('pg').Pool
require('dotenv').config()
const isProduction = process.env.NODE_ENV === 'production'
let pool

const connectionString = `postgresql://${process.env.PG_USER}:${process.env.PG_PASSWORD}@${process.env.PG_HOST}:${process.env.PG_PORT}/${process.env.PG_DATABASE}`
console.log(connectionString)
console.log(isProduction)
if (isProduction) {
  pool = new Pool({
    connectionString: process.env.DATABASE_URL,
    ssl: {
      rejectUnauthorized: false,
    },
  })
} else {
  pool = new Pool({
    connectionString: connectionString,
  })
}

module.exports = pool
