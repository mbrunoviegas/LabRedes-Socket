public class Account {
  private int id;
  private float balance;

  public Account(int id) {
    this.id = id;
    this.balance = 0;
  }

  public int getId() {
    return this.id;
  }
  public float getBalance() {
    return this.balance;
  }
  public void setBalance(float balance) {
    this.balance = balance;
  }
}
