package dev.angryl1on.gateway;

public class TransactionRegistryDTO {
    private double amount;
    private String date;
    private String type;

    public TransactionRegistryDTO() {
    }

    public TransactionRegistryDTO(Double amount, String date, String type) {

        this.amount = amount;
        this.date = date;
        this.type = type;
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
