import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Servidor {
    public static void main(String[] args) throws IOException {
        int puertoEscucha = 1234;
        int[] numeros = new int[5];
        int[] soles = new int[2];
        String mensaje = "";
        try (DatagramSocket socket = new DatagramSocket(puertoEscucha)) {
            System.out.println("Esperando numero de Eurojackpot en puerto: " + puertoEscucha);

            while (!mensaje.equals("y")) {

                ServerService.rellenarArrayNummerosServidor();
                ServerService.rellenarSolesServidor();
                ServerService.mostrarArrayServidor();

                
                byte[] bufferRecibo = new byte[1024];
                DatagramPacket paqueteRecibido = new DatagramPacket(bufferRecibo, bufferRecibo.length);
                socket.receive(paqueteRecibido);

                ByteBuffer byteBuffer = ByteBuffer.wrap(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());

                int lenNumeros = byteBuffer.getInt();
                for (int i = 0; i < numeros.length; i++) {
                    numeros[i] = byteBuffer.getInt();
                }
                Arrays.sort(numeros);
                System.out.println("El numero recibido del cliente es: " + Arrays.toString(numeros));

                int lenSoles = byteBuffer.getInt();
                for (int i = 0; i < soles.length; i++) {
                    soles[i] = byteBuffer.getInt();
                }
                Arrays.sort(soles);
                System.out.println("Los soles recibidos del cliente son: " + Arrays.toString(soles));

        
                InetAddress destino = paqueteRecibido.getAddress();
                int puertoCliente = paqueteRecibido.getPort();
                mensaje = ServerService.devolucionAlCliente(numeros, soles);
                byte[] BufferSalida = mensaje.getBytes();

                DatagramPacket respuesta = new DatagramPacket(BufferSalida, BufferSalida.length, destino,
                        puertoCliente);

                socket.send(respuesta);
                System.out.println("Respuesta enviada");

                byte[] bufferDecision = new byte[1024];
                DatagramPacket paqueteDecision = new DatagramPacket(bufferDecision, bufferDecision.length);
                socket.receive(paqueteDecision);

                mensaje = new String(paqueteDecision.getData(), 0, paqueteDecision.getLength(), StandardCharsets.UTF_8)
                        .trim().toLowerCase();
            }
            // cerrar el socket
            socket.close();

        } catch (IOException e) {
            System.err.println("Error al recibir el paquete UDP: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Juego finalizado");
    }
}
