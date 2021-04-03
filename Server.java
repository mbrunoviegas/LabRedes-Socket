import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
  private int port;
  private HashMap<Integer, PrintStream> clientsMap;

  public Server(int port) {
    this.port = port;
    this.clientsMap = new HashMap<Integer, PrintStream>();
  }

  public static void main(String[] args) throws IOException {
    // inicia o servidor
    new Server(12345).executa();
  }

  public void executa() throws IOException {
    ServerSocket server = new ServerSocket(this.port);
    System.out.println("Servidor iniciado com sucesso. Porta 12345 aberta.");
    
    while (true) {
      // aceita um cliente
      Socket client = server.accept();

      // cadastra o cliente
      Account account = new Account(client.getPort());
      System.out.println("Cliente conectado com sucesso (porta " + client.getPort() + ").");

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
}
