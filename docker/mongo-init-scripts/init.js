db = db.getSiblingDB('transaction_db');

db.createCollection('transactions');

db.students.insertOne({
    amount: "1000",
    date: "2025-01-01",
    type: "SELL",
});

print('Database "transaction_db" and collection "transactions" have been initialized.');