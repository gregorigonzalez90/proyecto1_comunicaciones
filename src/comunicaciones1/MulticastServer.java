/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package comunicaciones1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import javax.swing.JTextArea;

/**
 *
 * @author xyklex
 */
public class MulticastServer{
    String grupo;
    boolean unidoAlGrupo = false;
    MulticastSocket socket;
    int limit;
    JTextArea logs;
    InetAddress address;
    int port;
    Thread server;

    public MulticastServer(int port, int limit, JTextArea logs) {
        this.port = port;
        this.limit = limit;
        this.logs = logs;
    }

    Runnable a = new Runnable() {
        @Override
        public void run() {
            try {
                while(unidoAlGrupo) {
                    socket = new MulticastSocket(port);

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
                System.err.println("UnknownHostException");
            } catch (IOException ex) {
                System.err.println("IOException");
            }
        }
    };


    public void detenerHilo() {
        this.unidoAlGrupo = false;
        socket.close();
        server.interrupt();
    }

    public void AgregarRecibidos(String mensaje){
      logs.append(mensaje + "\n");
    }

    void setGrupo(String elemento) {
        this.grupo = elemento;
    }

    void start() {
        server = new Thread(a);
        server.start();
    }
}
