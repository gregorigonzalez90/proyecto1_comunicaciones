/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicaciones1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gregori.gonzalez
 */
public class Cliente{
    DatagramSocket socket = null;
    DatagramPacket paquete = null;
    byte[] buf;
    InetAddress address;
    int port;
    int limit; 
    
    public Cliente(int port, int limit) {
        this.port = port;
        this.limit = limit;
        
        try {
            socket = new DatagramSocket();
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }
    
    public void enviarUnicast(String direccion, String mensaje){
        try {
            int mensajeLength = mensaje.getBytes("UTF-8").length;
            int cantPaquetes = (mensajeLength / this.limit) + 1;
            
            try {
                address = InetAddress.getByName(direccion);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            }
            byte[] buffer = new byte[mensajeLength];
            buffer = mensaje.getBytes("UTF-8");
            
            if(cantPaquetes == 0) 
                cantPaquetes = 1;

            for(int i = 0; i < cantPaquetes; i++) {
                buf = Arrays.copyOfRange(buffer, i * this.limit, (i + 1) * this.limit);
                paquete =  new DatagramPacket(buf, buf.length,address,this.port);
                try {
                    socket.send(paquete);
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
