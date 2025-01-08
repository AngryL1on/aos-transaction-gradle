package dev.angryl1on.domainservice.models.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a transaction entity stored in the "transactions" collection
 * in MongoDB.
 *
 * <p>This class is annotated with {@link org.springframework.data.mongodb.core.mapping.Document}
 * to indicate its mapping to a MongoDB document. Each instance represents a single
 * transaction record, with fields for transaction ID, amount, date, and type.</p>
 *
 * <p>The class includes constructors for initialization, as well as
 * getter and setter methods for field access and modification.</p>
 *
 * @author AngryL1on
 * @version 1.0
 * @since 1.0
 */
@Document(collection = "transactions")
public class TransactionEntity {

    /**
     * The unique identifier of the transaction.
     * Annotated with {@link org.springframework.data.annotation.Id} to
     * indicate it is the primary key for the document.
     */
    @Id
    private String id;

    /**
     * The amount of the transaction.
     */
    private double amount;

    /**
     * The date of the transaction.
     * Expected format is a string representation of the date (e.g., "YYYY-MM-DD").
     */
    private String date;

    /**
     * The type of the transaction (e.g., "credit", "debit").
     */
    private String type;

    /**
     * Default no-argument constructor.
     */
    public TransactionEntity() {
    }

    /**
     * Constructor for initializing all fields of the transaction entity.
     *
     * @param id The unique identifier of the transaction.
     * @param amount The amount of the transaction.
     * @param date The date of the transaction.
     * @param type The type of the transaction.
     */
    public TransactionEntity(String id, double amount, String date, String type) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    /**
     * Constructor for initializing all fields except the transaction ID.
     * Useful for creating new transactions before assigning an ID.
     *
     * @param amount The amount of the transaction.
     * @param date The date of the transaction.
     * @param type The type of the transaction.
     */
    public TransactionEntity(double amount, String date, String type) {
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
