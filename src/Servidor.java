/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicaciones1;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author gregori.gonzalez
 */
public class Servidor extends Thread{
    DatagramSocket socket = null;
    JTextArea jtareaRecibidos;
    int port;
    int limit;
    
    public Servidor(JTextArea jtarea, int port, int limit){
        this.port = port;
        this.limit = limit;
        try {
            socket = new DatagramSocket(this.port);
        } catch (SocketException ex) {
            System.out.println("Errorr");
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        jtareaRecibidos = jtarea;
    }
            @Override
            public void run() {
                    String mensaje;
                    while(true){
                        try {
                            byte[] buf = new byte[this.limit];
                           // receive request
                           DatagramPacket packet = new DatagramPacket(buf, buf.length);
                           socket.receive(packet);

                           mensaje = new String(packet.getData(), 0, packet.getLength());
                           AgregarRecibidos(packet.getAddress().toString() + ": " + mensaje);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                     }
            }
                
      public void AgregarRecibidos(String mensaje){
          jtareaRecibidos.append(mensaje + "\n");
         
      }

}
