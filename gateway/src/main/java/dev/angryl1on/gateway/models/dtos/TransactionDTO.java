package dev.angryl1on.gateway.models.dtos;

/**
 * Data Transfer Object (DTO) for representing transaction data.
 *
 * <p>The {@code TransactionDTO} class is a lightweight container used to transfer
 * transaction-related data between layers or services. It includes details such as
 * the transaction ID, amount, date, and type.</p>
 *
 * <p>This class is typically used in scenarios where the transaction data
 * needs to be serialized, deserialized, or passed as method arguments.</p>
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
    private double amount;

    /**
     * The date of the transaction, represented as a {@code String}.
     * Expected format: "YYYY-MM-DD".
     */
    private String date;

    /**
     * The type of the transaction (e.g., "credit", "debit").
     */
    private String type;

    /**
     * Default no-argument constructor.
     */
    public TransactionDTO() {
    }

    /**
     * Constructs a {@code TransactionDTO} with all fields initialized.
     *
     * @param id     The unique identifier of the transaction.
     * @param amount The amount of the transaction.
     * @param date   The date of the transaction.
     * @param type   The type of the transaction.
     */
    public TransactionDTO(String id, Double amount, String date, String type) {
        this.id = id;
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
