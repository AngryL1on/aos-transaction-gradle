package dev.angryl1on.domainservice.models.dtos;

/**
 * Data Transfer Object (DTO) for transferring transaction data between layers.
 *
 * <p>The {@code TransactionDTO} class is a lightweight representation of a
 * transaction, designed to encapsulate data for communication purposes.
 * It includes additional fields such as {@code operation} to extend the
 * information provided about a transaction.</p>
 *
 * <p>The class includes constructors for flexibility in initialization and
 * getter and setter methods for field access and modification.</p>
 *
 * @author AngryL1on
 * @version 1.0
 * @since 1.0
 */
public class TransactionDTO {

    /**
     * The unique identifier of the transaction.
     */
    private String id;

    /**
     * The amount of the transaction.
     */
    private Double amount;

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
     * An additional field representing the operation performed during the transaction.
     * This could be used to describe actions or processes associated with the transaction.
     */
    private String operation;

    /**
     * Default no-argument constructor.
     */
    public TransactionDTO() {
    }

    /**
     * Constructor for initializing the core fields of the transaction DTO.
     *
     * @param id The unique identifier of the transaction.
     * @param amount The amount of the transaction.
     * @param date The date of the transaction.
     * @param type The type of the transaction.
     */
    public TransactionDTO(String id, Double amount, String date, String type) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    /**
     * Constructor for initializing all fields of the transaction DTO.
     *
     * @param id The unique identifier of the transaction.
     * @param amount The amount of the transaction.
     * @param date The date of the transaction.
     * @param type The type of the transaction.
     * @param operation The operation associated with the transaction.
     */
    public TransactionDTO(String id, Double amount, String date, String type, String operation) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.operation = operation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
