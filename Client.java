import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
  private String host;
  private int port;

  public Client(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public static void main(String[] args) throws UnknownHostException, IOException {
    new Client("127.0.0.1", 3333).execute();
  }

  public void execute() throws UnknownHostException, IOException {
    Socket clientSocket = new Socket(this.host, this.port);
    System.out.println("Conectado com sucesso!");
    new Thread().start();
  }

}