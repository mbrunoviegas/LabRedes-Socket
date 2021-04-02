import java.io.*;
import java.net.*;
import java.util.*;

public class Recebedor implements Runnable{
    private InputStream server;
    public Recebedor(InputStream server){
        this.server= server;
    }
    public void run(){// recebe msgs do server e imprime na tela     
        Scanner s =new Scanner(this.server);
        while(s.hasNextLine()){       
            System.out.println(s.nextLine());
        }
    }
}