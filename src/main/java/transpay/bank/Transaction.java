package transpay.bank;

import transpay.account.Account;

public class Transaction {
    private Account account;
    private String target;
    private double amount;
    private String type;
    private String date;
    
    public Transaction(Account account, double amount, String type, String date) {
        this.account = account;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public Account getAccount() {
        return account;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }
}
