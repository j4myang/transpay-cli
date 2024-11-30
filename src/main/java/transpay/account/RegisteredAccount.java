package transpay.account;

public class RegisteredAccount {
    private Account account;
    private RegisteredAccount pointer;

    public RegisteredAccount(Account account) {
        this.account = account;
        this.pointer = null;
    }

    public Account getAccount() {
        return account;
    }

    public RegisteredAccount getPointer() {
        return pointer;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setPointer(RegisteredAccount pointer) {
        this.pointer = pointer;
    }
}

