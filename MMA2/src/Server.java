// file: Server.java
import gnu.cajo.invoke.Remote;
import gnu.cajo.utils.ItemServer;
import gnu.cajo.utils.extra.ClientProxy;

public class Server {
   Thread loop;
   ClientProxy cp;
   int count;
   Server() throws Exception {
      Remote.config("localhost", 1198, "localhost", 1198);
      ItemServer.bind(this, "main");
      loop = new Thread(new Runnable() {
         public void run() {
            while(!loop.isInterrupted()) {
               if (cp != null) {
                  try {
                     Object result =
                        Remote.invoke(cp, "callback", "Hello " + count++);
                     System.out.println("Client callback, result = " + result);
                  } catch(Exception x) { // network error
                     x.printStackTrace();
                     break;
                  }
               }
               try { loop.sleep(2000); }
               catch(InterruptedException x) { break; }
            }
         }
      });
      loop.start();
      System.out.println("Server started");
   }
   public String call(String message) {
      System.out.println("Client call, data = " + message);
      return "Thanks!";
   }
   public Remote getCp() throws Exception {
      cp = new ClientProxy();
      return cp.remoteThis;
   }
   static Server server;
   public static void main(String args[]) {
      try { server = new Server(); }
      catch(Exception x) { x.printStackTrace(); }
   }
}