package bank.resolvers;

public class AccountGson {

    String number;
    String owner;
    float balance;
    boolean active;

    public AccountGson() {
    }

    public AccountGson(String number, String owner, float balance, boolean active) {
        this.number = number;
        this.owner = owner;
        this.balance = balance;
        this.active = active;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "AccountGson{" +
                "number='" + number + '\'' +
                ", owner='" + owner + '\'' +
                ", balance=" + balance +
                ", active=" + active +
                '}';
    }
}
