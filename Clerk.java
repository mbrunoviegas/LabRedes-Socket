import java.io.*;
import java.util.*;

public class Clerk implements Runnable {
  private final String SALDO = "1";
  private final String DEPOSITO = "2";
  private final String SAQUE = "3";
  private final String TRANSFERENCIA = "4";

  private Account account;
  private InputStream clientInputStream;
  private Server server;

  public Clerk(Account account, InputStream clientInputStream, Server server) {
    this.account = account;
    this.clientInputStream = clientInputStream;
    this.server = server;
  }

  public void run() {
    Scanner s = new Scanner(this.clientInputStream);
    String messageFromClient = "";
    String messageToClient = "";
    String errorMessage = "";
    String[] payload;
    float amount = 0;
    int targetAccount = 0;
    boolean success;

    // espera a mensagem do cliente
    while (s.hasNextLine()) {
      messageFromClient = s.nextLine();
      payload = messageFromClient.split(";");
      success = true;
      errorMessage = ";";

      switch (payload[0]) {
      case SALDO:
        System.out.println("Ocorreu uma operacao de SALDO, na conta " + account.getId() + ".");
        break;

      case DEPOSITO:
        amount = Float.parseFloat(payload[1]);
        this.account.setBalance(this.account.getBalance() + amount);
        System.out.println("Ocorreu uma operacao de DEPOSITO, no valor de R$ " + amount + ", na conta " + account.getId() + ".");
        break;

      case SAQUE:
        amount = Float.parseFloat(payload[1]);
        if (this.account.getBalance() >= amount) {
          this.account.setBalance(this.account.getBalance() - amount);
          System.out.println("Ocorreu uma operacao de SAQUE, no valor de R$ " + amount + ", na conta " + account.getId() + ".");
        }
        else {
          System.out.println("Falha na operacao de SAQUE, na conta " + account.getId() + ".");
          success = false;
          errorMessage += "Saldo insuficiente.";
        }
        break;
        
      case TRANSFERENCIA:
        amount = Float.parseFloat(payload[1]);
        targetAccount = Integer.parseInt(payload[2]);

        if (this.account.getBalance() >= amount) {
          if (server.existAccount(targetAccount)) {
            this.account.setBalance(this.account.getBalance() - amount);
            server.transfer(targetAccount, amount);
            
            System.out.println("Ocorreu uma operacao de TRANSFERENCIA, no valor de R$ " + amount + ", da conta " + account.getId() + " para a conta " + payload[2] + ".");
          }
          else {
            System.out.println("Falha na operacao de TRANSFERENCIA, da conta " + account.getId() + " para a conta " + payload[2] + ".");
            success = false;
            errorMessage += "Conta destino inexistente.";
          }
        }
        else {
          System.out.println("Falha na operacao de TRANSFERENCIA, da conta " + account.getId() + " para a conta " + payload[2] + ".");
          success = false;
          errorMessage += "Saldo insuficiente.";
        }
        break;
      }

      messageToClient = success
        ? payload[0] + ";1;" + String.valueOf(this.account.getBalance())
        : payload[0] + ";0;" + String.valueOf(this.account.getBalance()) + errorMessage;

      server.sendMessage(account.getId(), messageToClient);
    }

    s.close();
  }
}
