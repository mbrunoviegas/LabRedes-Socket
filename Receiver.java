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

      if (Integer.parseInt(payload[0]) == 9) // transferencia
        System.out.print("\n\n" + payload[1] + "\nSeu novo saldo e de: R$ " + payload[2] + "\n\nSua opcao: ");
      else {
        if (Integer.parseInt(payload[0]) != 1) { // não é uma operação de saldo
          if (Integer.parseInt(payload[1]) == 1) // sucesso
            System.out.println("\nOperacao realizada com sucesso.");
          else
            System.out.println("\nFalha na operacao. " + payload[3]);
        }
  
        System.out.println("Seu saldo e de: R$ " + payload[2]);
      }
    }

    scanner.close();
  }
}
