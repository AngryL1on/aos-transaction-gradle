package dev.angryl1on.gateway.models.dtos;

public class TransactionDTO {
    private String id;
    private double amount;
    private String date;
    private String type;

    public TransactionDTO() {
    }

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
