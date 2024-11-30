package transpay.bank;

public class BankTransaction {
    private Transaction transaction;
    private BankTransaction pointer;

    public BankTransaction(Transaction transaction) {
        this.transaction = transaction;
        this.pointer = null;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public BankTransaction getPointer() {
        return pointer;
    }

    public void setPointer(BankTransaction pointer) {
        this.pointer = pointer;
    }
}
