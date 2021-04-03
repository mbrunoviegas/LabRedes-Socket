import java.io.*;
import java.util.*;

public class Clerk implements Runnable {
  private final String SALDO = "1";
  private final String DEPOSITO = "2";
  private final String SAQUE = "3";

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
    String[] payload;
    float amount = 0;

    // espera a mensagem do cliente
    while (s.hasNextLine()) {
      messageFromClient = s.nextLine();
      payload = messageFromClient.split(";");

      switch (payload[0]) {
        case SALDO:
        break;

        case DEPOSITO:
          amount = Float.parseFloat(payload[1]);
          this.account.setBalance(this.account.getBalance() + amount);
        break;

        case SAQUE:
          amount = Float.parseFloat(payload[1]);
          this.account.setBalance(this.account.getBalance() - amount);
        break;
      }

      messageToClient = String.valueOf(this.account.getBalance()) + ";Operacao realizada com sucesso.";
      server.sendMessage(account.getId(), messageToClient);
    }

    s.close();
  }
}
