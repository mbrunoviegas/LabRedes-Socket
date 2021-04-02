import java.io.*;
import java.net.*;
import java.util.*;

public class TrataCliente implements Runnable {

  private InputStream client;
  private Server server;

  public TrataCliente(InputStream client, Server server) {
    this.client = client;
    this.server = server;
  }

  public void run() {
    // quando chegar uma msg, distribui pra todos
    Scanner s = new Scanner(this.client);
    while (s.hasNextLine()) {
      server.distribuiMensagem(s.nextLine());
    }
    s.close();
  }
}