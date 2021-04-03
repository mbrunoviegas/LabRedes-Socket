import java.io.*;
import java.util.*;

public class Receiver implements Runnable {
  private InputStream server;

  public Receiver(InputStream server) {
    this.server = server;
  }

  public void run() {
    Scanner scanner = new Scanner(this.server);
    String messageFromServer = "";
    String[] payload;

    // espera a mensagem do servidor
    while (scanner.hasNextLine()) {
      messageFromServer = scanner.nextLine();
      payload = messageFromServer.split(";");

      System.out.println("\n" + payload[1] + "\nSeu saldo e de: R$ " + payload[0]);
    }

    scanner.close();
  }
}