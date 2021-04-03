import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
  private final int SALDO = 1;
  private final int DEPOSITO = 2;
  private final int SAQUE = 3;

  private String host;
  private int port;

  public Client(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public static void main(String[] args) throws UnknownHostException, IOException {
    // dispara client
    new Client("127.0.0.1", 12345).executa();
  }

  public void executa() throws UnknownHostException, IOException {
    Socket client = new Socket(this.host, this.port);
    System.out.println("Conectado com sucesso. Bem-vindo ao seu banco!");

    // thread para receber mensagens do servidor
    Receiver r = new Receiver(client.getInputStream());
    new Thread(r).start();

    // faz a leitura do teclado e manda para o servidor
    Scanner keyboard = new Scanner(System.in);
    PrintStream sendToServer = new PrintStream(client.getOutputStream());

    String menu = "\nMENU - Escolha uma opcao\n\n0 - Sair\n1 - Saldo\n2 - Deposito\n3 - Saque\n\nSua opcao: ";
    int option = -1;
    String mensagem = "";
    float saldo = 0;
    float amount = 0;

    System.out.print(menu);
    option = keyboard.nextInt();
    while (option < 0 || option > 3) {
      System.out.println("\nPor favor, escolha uma opcao entre 0 e 3.");
      System.out.print(menu);
      option = keyboard.nextInt();
    }

    while (option != 0 && keyboard.hasNextLine()) {
      switch (option) {
      case SALDO:
        mensagem = "1";
        break;

      case DEPOSITO:
        System.out.print("Por favor, digite o valor a ser depositado: ");
        amount = keyboard.nextFloat();
        mensagem = "2;" + amount;
        break;

      case SAQUE:
        sendToServer.println("1"); // verificar o saldo
        // saldo = retorno do servidor
        System.out.println("Seu saldo é de R$ " + saldo + ".");

        System.out.print("Por favor, digite o valor a ser retirado: ");
        amount = keyboard.nextFloat();

        while (amount > saldo) {
          System.out.println("Saldo insuficiente.");
          System.out.print("Por favor, digite novamente: ");
          amount = keyboard.nextFloat();
        }

        mensagem = "3;" + amount;
        break;
      }

      sendToServer.println(mensagem);

      try {
        Thread.sleep(1000);
      } catch (InterruptedException ie) {
      }

      System.out.print(menu);
      option = keyboard.nextInt();
      while (option < 0 || option > 3) {
        System.out.println("\nPor favor, escolha uma opcao entre 0 e 3.");
        System.out.print(menu);
        option = keyboard.nextInt();
      }
    }

    System.out.print("Muito obrigado por utilizar nosso banco!");

    sendToServer.close();
    keyboard.close();
    client.close();
  }
}
