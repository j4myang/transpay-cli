package transpay.bank;

public class BankSystem {
    private BankTransaction head;

    public BankSystem() {
        this.head = null;
    }

    public void addTransaction(Transaction transaction) {
        BankTransaction bankTransaction = new BankTransaction(transaction);

        if (head == null) {
            head = bankTransaction;
        } else {
            BankTransaction current = head;
            while (current.getPointer() != null) {
                current = current.getPointer();
            }
            current.setPointer(bankTransaction);
        }
    }

    public Transaction[] getTransactionsByAccount(String accountNumber) {
        BankTransaction current = head;
        Transaction[] transactions = new Transaction[0];

        while (current != null) {
            if (current.getTransaction().getAccount().getAccountNumber().equals(accountNumber)) {
                Transaction[] newTransactions = new Transaction[transactions.length + 1];
                for (int i = 0; i < transactions.length; i++) {
                    newTransactions[i] = transactions[i];
                }
                newTransactions[newTransactions.length - 1] = current.getTransaction();
                transactions = newTransactions;
            }
            current = current.getPointer();
        }

        return transactions;
    }

    public void deleteTransactionByAccount(String accountNumber) {
        BankTransaction current = head;
        BankTransaction previous = null;

        while (current != null) {
            if (current.getTransaction().getAccount().getAccountNumber().equals(accountNumber)) {
                if (previous == null) {
                    head = current.getPointer();
                } else {
                    previous.setPointer(current.getPointer());
                }
                return;
            }
            previous = current;
            current = current.getPointer();
        }
    }

    public int length() {
        BankTransaction current = head;
        int count = 0;
        while (current != null) {
            count++;
            current = current.getPointer();
        }
        return count;
    }
}
