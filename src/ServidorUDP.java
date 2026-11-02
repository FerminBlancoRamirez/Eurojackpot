import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ServidorUDP {
    public static void main(String[] args) throws IOException {
        int puerto = 12345;
        int puertoEscucha = 12346;
        byte[] buffer1 = new byte[1024];
        byte[] buffer2 = new byte[1024];
        byte[] bufferMensaje = new byte[1024];
        int[] numerosPrincipales = new int[5];
        int[] solesArray = new int[2];
        String mensaje = "";

        try (DatagramSocket socket = new DatagramSocket(puerto)) {
            System.out.println("Esperando los numeros de parte del cliente: " + puerto);
            InetAddress destino = InetAddress.getLocalHost();

            while (!mensaje.equals("salir")) {
                DatagramPacket numeros = new DatagramPacket(buffer1, buffer1.length);
                socket.receive(numeros); 

                ByteBuffer byteBuffer1 = ByteBuffer.wrap(numeros.getData(), 0, numeros.getLength());
                for (int i = 0; i < numerosPrincipales.length; i++) {
                    numerosPrincipales[i] = byteBuffer1.getInt();
                }

                DatagramPacket soles = new DatagramPacket(buffer2, buffer2.length);
                socket.receive(soles);

                ByteBuffer byteBuffer2 = ByteBuffer.wrap(buffer2, 0, soles.getLength());
                for (int i = 0; i < solesArray.length; i++) {
                    solesArray[i] = byteBuffer2.getInt();
                }

                System.out.println("Los cinco primeros numeros recibidos del cliente son: " + numeros);
                System.out.println("Los soles recibidos del cliente son: " + solesArray);

                DatagramPacket recibo = new DatagramPacket(bufferMensaje, bufferMensaje.length);
                socket.receive(recibo); 
                int bytesRec = recibo.getLength();
                mensaje = new String(recibo.getData(), 0, bytesRec, StandardCharsets.UTF_8);
            }
        }
    }
}
