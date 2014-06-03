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
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

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
    JTextArea logs;
    
    public Cliente(int port, int limit, JTextArea logs) {
        this.port = port;
        this.limit = limit;
        this.logs = logs;

        try {
            socket = new DatagramSocket();
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }
    
    public void enviarUnicast(String direccion, String mensaje, int port){
            int mensajeLength = mensaje.getBytes().length;
            int cantPaquetes = (mensajeLength / this.limit) + 1;
            
            try {
                address = InetAddress.getByName(direccion);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            }
            byte[] buffer = new byte[mensajeLength];
            buffer = mensaje.getBytes();
            
            if(cantPaquetes == 0) 
                cantPaquetes = 1;
            
            int capacidad = this.limit;
            for(int i = 0; i < cantPaquetes; i++) {
                if(mensajeLength < this.limit)
                    capacidad = mensajeLength;
                buf = Arrays.copyOfRange(buffer, i * capacidad, (i + 1) * capacidad);
                mensajeLength -= this.limit;
                
                paquete =  new DatagramPacket(buf, buf.length,address,port);
                try {
                    socket.send(paquete);
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
    }
}
