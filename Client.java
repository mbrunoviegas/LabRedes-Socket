import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
  private final int SALDO = 1;
  private final int DEPOSITO = 2;
  private final int SAQUE = 3;
  private final int TRANSFERENCIA = 4;

  Scanner keyboard = new Scanner(System.in);
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
    System.out.println("Conta: " + client.getLocalPort());

    // thread para receber mensagens do servidor
    Receiver r = new Receiver(client.getInputStream());
    new Thread(r).start();

    // faz a leitura do teclado e manda para o servidor
    PrintStream sendToServer = new PrintStream(client.getOutputStream());

    String mensagem = "";
    float amount = 0;
    int targetAccount = 0;
    boolean confirmation = true;

    int option = menu();

    while (option != 0 && keyboard.hasNextLine()) {
      switch (option) {
      case SALDO:
        mensagem = "1";
        break;

      case DEPOSITO:
        System.out.print("Por favor, digite o valor a ser depositado: ");
        amount = keyboard.nextFloat();

        confirmation = askForConfirmation("Deseja depositar R$ " + amount + "?");
        mensagem = "2;" + amount;
        break;

      case SAQUE:
        sendToServer.println("1"); // verificar o saldo
        try {
          Thread.sleep(500);
        } catch (InterruptedException ie) {
        }

        System.out.print("Por favor, digite o valor a ser retirado: ");
        amount = keyboard.nextFloat();

        confirmation = askForConfirmation("Deseja sacar R$ " + amount + "?");
        mensagem = "3;" + amount;
        break;
        
      case TRANSFERENCIA:
        sendToServer.println("1"); // verificar o saldo
        try {
          Thread.sleep(500);
        } catch (InterruptedException ie) {
        }

        System.out.print("Por favor, digite o valor a ser transferido: ");
        amount = keyboard.nextFloat();

        System.out.print("Por favor, digite a conta destino: ");
        targetAccount = keyboard.nextInt();
        
        confirmation = askForConfirmation("Deseja transferir R$ " + amount + " para a conta " + targetAccount + "?");
        mensagem = "4;" + amount + ";" + targetAccount;
        break;
      }

      if (confirmation)
        sendToServer.println(mensagem);
      else
        System.out.println("Operacao cancelada.");

      try {
        Thread.sleep(500);
      } catch (InterruptedException ie) {
      }

      option = menu();
    }

    System.out.print("Muito obrigado por utilizar nosso banco!");

    sendToServer.close();
    keyboard.close();
    client.close();
  }

  public int menu() {
    String menu = "\nMENU - Escolha uma opcao\n\n0 - Sair\n1 - Saldo\n2 - Deposito\n3 - Saque\n4 - Transferencia\n\nSua opcao: ";
    int option;

    System.out.print(menu);
    option = keyboard.nextInt();

    while (option < 0 || option > 4) {
      System.out.println("\nPor favor, escolha uma opcao entre 0 e 4.");
      System.out.print(menu);
      option = keyboard.nextInt();
    }

    return option;
  }

  public boolean askForConfirmation(String text) {
    String opcoes = "\n0 - NAO\n1 - SIM\n\nSua opcao: ";
    int option;

    System.out.println(text);
    System.out.print(opcoes);
    option = keyboard.nextInt();
    
    while (option != 0 && option != 1) {
      System.out.println("\nPor favor, escolha uma opcao entre 0 e 1.");
      System.out.print(opcoes);
      option = keyboard.nextInt();
    }

    return option == 1;
  }
}
