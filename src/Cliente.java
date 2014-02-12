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
    grupoMulticast conexionGrupo;
    JTextArea logs;
    
    public Cliente(int port, int limit, JTextArea logs) {
        this.port = port;
        this.limit = limit;
        this.logs = logs;
        conexionGrupo = new grupoMulticast();

        try {
            socket = new DatagramSocket();
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }
    
    public void enviarUnicast(String direccion, String mensaje, int port){
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
                paquete =  new DatagramPacket(buf, buf.length,address,port);
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
    
    public class grupoMulticast extends Thread{
        String grupo;
        boolean unidoAlGrupo = false;
        MulticastSocket socket;
        
        public void setGrupo(String grupo) {
            this.grupo = grupo;
        }
        
        @Override
        public void run() {
            try {
                while(unidoAlGrupo) {
                    socket = new MulticastSocket(4446);
                    
                    InetAddress address = InetAddress.getByName(grupo);
                    socket.joinGroup(address);
                    
                    DatagramPacket packet;
                    
                    byte[] buf = new byte[limit];
                    packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    
                    String received = new String(packet.getData(), 0, packet.getLength());
                    AgregarRecibidos(packet.getAddress().toString() + ": " + received);
                }   
                
                socket.leaveGroup(address);
                socket.close();
            
            } catch (UnknownHostException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void detenerHilo() {
            try {
                socket.leaveGroup(address);
                socket.close();
                unidoAlGrupo = false;
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void AgregarRecibidos(String mensaje){
          logs.append(mensaje + "\n");
        }
    }
    
    
}
