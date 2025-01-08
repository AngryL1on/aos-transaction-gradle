package dev.angryl1on.domainservice.models.dtos;

public class TransactionDTO {
    private String id;
    private Double  amount;
    private String date;
    private String type;
    private String operation;

    public TransactionDTO() {
    }

    public TransactionDTO(String id, Double amount, String date, String type) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

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

    public Double  getAmount() {
        return amount;
    }

    public void setAmount(Double  amount) {
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
