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
        byte[] bufferArrayNumeros = new byte[1024];
        byte[] bufferArraySoles = new byte[1024];
        byte[] bufferArraySalida = new byte[1024];
        int[] numeros = new int[5];
        int[] soles = new int[2];
        String mensaje = "";
        try (DatagramSocket socket = new DatagramSocket(puertoEscucha)) {
            System.out.println("Esperando numero de Eurojackpot en puerto: " + puertoEscucha);

            while (!mensaje.equals("y")) {

                // Aplicamos los metodos y obtenemos los numeros ganadores
                ServerService.rellenarArrayNummerosServidor();
                ServerService.rellenarSolesServidor();
                ServerService.mostrarArrayServidor();

                // Numeros principales
                DatagramPacket NumerosRecibidos = new DatagramPacket(bufferArrayNumeros, bufferArrayNumeros.length);
                socket.receive(NumerosRecibidos);
                ByteBuffer byteBuffer = ByteBuffer.wrap(bufferArrayNumeros);
                for (int i = 0; i < numeros.length; i++) {
                    numeros[i] = byteBuffer.getInt();
                }
                Arrays.sort(numeros);
                System.out.println("El numero recibido del cliente es: " + Arrays.toString(numeros));

                // Soles
                DatagramPacket SolesRecibidos = new DatagramPacket(bufferArraySoles, bufferArraySoles.length);
                socket.receive(SolesRecibidos);
                ByteBuffer byteBuffer2 = ByteBuffer.wrap(bufferArrayNumeros);
                for (int i = 0; i < soles.length; i++) {
                    soles[i] = byteBuffer2.getInt();
                }
                Arrays.sort(soles);
                System.out.println("Los soles recibidos del cliente son: " + Arrays.toString(soles));

                // Comprobamos coincidencias y devolvemos resultado
                InetAddress destino = NumerosRecibidos.getAddress();
                int puertoCliente = NumerosRecibidos.getPort();
                mensaje = ServerService.devolucionAlCliente(numeros, soles);
                byte[] BufferSalida = mensaje.getBytes();

                DatagramPacket respuesta = new DatagramPacket(BufferSalida, BufferSalida.length, destino,
                        puertoCliente);

                socket.send(respuesta);
                System.out.println("Respuesta enviada");

                // Ver si termina el bucle
                DatagramPacket salida = new DatagramPacket(bufferArraySalida, bufferArraySalida.length);
                socket.receive(salida);
                int bytesRec = salida.getLength();
                mensaje = new String(salida.getData(), 0, bytesRec, StandardCharsets.UTF_8).toLowerCase();
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
