package transpay.account;

public class AccountSystem {
    private RegisteredAccount head;

    public AccountSystem() {
        this.head = null;
    }

    public void addAccount(Account account) {
        RegisteredAccount registeredAccount = new RegisteredAccount(account);

        if (head == null) {
            head = registeredAccount;
        } else {
            RegisteredAccount current = head;
            while (current.getPointer() != null) {
                current = current.getPointer();
            }
            current.setPointer(registeredAccount);
        }
    }

    public Account getAccount(String accountNumber) {
        RegisteredAccount current = head;
        while (current != null) {
            if (current.getAccount().getAccountNumber().equals(accountNumber)) {
                return current.getAccount();
            }
            current = current.getPointer();
        }
        return null;
    }

    public void deleteAccount(String accountNumber) {
        RegisteredAccount current = head;
        RegisteredAccount previous = null;
        while (current != null) {
            if (current.getAccount().getAccountNumber().equals(accountNumber)) {
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

    public void updateAccount(String accountNumber, Account account) {
        RegisteredAccount current = head;
        while (current != null) {
            if (current.getAccount().getAccountNumber().equals(accountNumber)) {
                current.setAccount(account);
                return;
            }
            current = current.getPointer();
        }
    }

    public int length() {
        RegisteredAccount current = head;
        int count = 0;
        while (current != null) {
            count++;
            current = current.getPointer();
        }
        return count;
    }
}
