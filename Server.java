import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

  public static void main(String[] args) throws IOException {
    // inicia o server
    new Server(12345).executa();
  }

  private int porta;
  private List<PrintStream> clientes;

  public Server(int porta) {
    this.porta = porta;
    this.clientes = new ArrayList<PrintStream>();
  }

  public void executa() throws IOException {
    ServerSocket server = new ServerSocket(this.porta);
    System.out.println("Porta 12345 aberta!");
    while (true) {
      // aceita um client
      Socket client = server.accept();

      // cadastrar o client no banco
      Account account = new Account(client.getPort());

      System.out.println(
        "Client cadastrado com sucesso!" +
        client.getInetAddress().getHostAddress()
      );
      // adiciona saida do client à lista
      PrintStream ps = new PrintStream(client.getOutputStream());
      this.clientes.add(ps);
      // cria tratador de client numa nova thread
      TrataCliente tc = new TrataCliente(client.getInputStream(), this);
      new Thread(tc).start();
    }
  }

  public void distribuiMensagem(String msg) {
    // envia msg para todo mundo
    for (PrintStream client : this.clientes) {
      client.println(msg);
    }
  }
}

class Account {
    int id;
    double balance;

    public Account(int id) {
      this.id = id;
      this.balance = 0;
    }
}