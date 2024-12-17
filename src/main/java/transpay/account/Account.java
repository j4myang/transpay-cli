package transpay.account;

public class Account {
    private String accountNumber;
    private String PIN;
    private double balance;
    private String name;
    private String dateRegistered;

    public Account(String accountNumber, String PIN, double balance, String name) {
        this.accountNumber = accountNumber;
        this.PIN = PIN;
        this.balance = balance;
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPIN() {
        return PIN;
    }

    public double getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void transfer(Account account, double amount) {
        account.deposit(amount);
        this.withdraw(amount);
    }
}
