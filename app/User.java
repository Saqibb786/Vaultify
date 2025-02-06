

public class User {
    private String name;
    private String bankPassword;
    private String accountNumber;

    // ✅ Correct Constructor
    public User(String name, String bankPassword, String accountNumber) {
        this.name = name;
        this.bankPassword = bankPassword;
        this.accountNumber = accountNumber;
    }

    // ✅ Getters
    public String getName() {
        return name;
    }

    public String getBankPassword() {
        return bankPassword;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    // ✅ Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setBankPassword(String bankPassword) {
        this.bankPassword = bankPassword;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    // ✅ Function to Return Complete User Object
    public static User getUser(String name, String bankPassword, String accountNumber) {
        return new User(name, bankPassword, accountNumber);
    }
}
