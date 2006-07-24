// file: Client.java
import gnu.cajo.invoke.Remote;
import gnu.cajo.invoke.RemoteInvoke;
import gnu.cajo.utils.extra.ItemProxy;

public class Client {
   Thread loop;
   ItemProxy ip;
   int count;
   Object server;
   Client() throws Exception {
      server = Remote.getItem("//192.168.0.12:1234/VirArbServer");
      RemoteInvoke cp =
         (RemoteInvoke)Remote.invoke(server, "getCp", null);
      ip = new ItemProxy(cp, this);
      loop = new Thread(new Runnable() {
         public void run() {
            while(!loop.isInterrupted()) {
               try {
                  Object result =
                     Remote.invoke(server, "call", "Hello " + count++);
                  System.out.println("Server call, result = " + result);
               } catch(Exception x) { // network error
                  x.printStackTrace();
                  break;
               }
               try { loop.sleep(2000); }
               catch(InterruptedException x) { break; }
            }
         }
      });
      loop.start();
      System.out.println("Client started");
   }
   public String callback(String message) {
      System.out.println("Server callback, data = " + message);
      return "Thanks!";
   }
   static Client client;
   public static void main(String args[]) {
      try { client = new Client(); }
      catch(Exception x) { x.printStackTrace(); }
   }
}


// 