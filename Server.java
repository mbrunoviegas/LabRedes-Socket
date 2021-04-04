import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
  private int port;
  private HashMap<Integer, PrintStream> clientsMap;
  private HashMap<Integer, Account> accountsMap;

  public Server(int port) {
    this.port = port;
    this.clientsMap = new HashMap<Integer, PrintStream>();
    this.accountsMap = new HashMap<Integer, Account>();
  }

  public static void main(String[] args) throws IOException {
    // inicia o servidor
    new Server(12345).executa();
  }

  public void executa() throws IOException {
    ServerSocket server = new ServerSocket(this.port);
    System.out.println("Servidor iniciado com sucesso.\n");

    while (true) {
      // aceita um cliente
      Socket client = server.accept();

      // cadastra o cliente
      Account account = new Account(client.getPort());
      this.accountsMap.put(account.getId(), account);
      System.out.println("Cliente conectado com sucesso (conta " + client.getPort() + ").");

      // adiciona saida do client à lista
      PrintStream ps = new PrintStream(client.getOutputStream());
      this.clientsMap.put(account.getId(), ps);

      // cria um atendente para o cliente numa nova thread
      Clerk clerk = new Clerk(account, client.getInputStream(), this);
      new Thread(clerk).start();
    }
  }

  public void sendMessage(int id, String msg) {
    this.clientsMap.get(id).println(msg);
  }

  public boolean existAccount(int id) {
    return this.accountsMap.get(id) != null;
  }

  public void transfer (int id, float amount) {
    this.accountsMap.get(id).setBalance(this.accountsMap.get(id).getBalance() + amount);
    this.sendMessage(id, "9;Transferencia recebida com o valor de R$ " + amount + ".;" + this.accountsMap.get(id).getBalance());
  }
}
