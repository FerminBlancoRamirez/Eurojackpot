
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        int puertoServidor = 1234;

        try {
            InetAddress destino = InetAddress.getLocalHost();
            DatagramSocket cliente = new DatagramSocket();
            Scanner sc = new Scanner(System.in);

            System.out.println("Cliente preparado para intentar ganar el bote de Eurojackpot");

            while (true) {
                int[] numerosCliente = new int[5];
                int[] solesCliente = new int[2];

                do {
                    System.out.println("Introduce numeros entre 1-50 :");
                    for (int i = 0; i < numerosCliente.length; i++) {
                        numerosCliente[i] = ServicioCliente.pedirEnteroPositivo(sc, 1, 50);
                    }
                    System.out.println("Introduce soles entre 1-12 :");
                    for (int i = 0; i < solesCliente.length; i++) {
                        solesCliente[i] = ServicioCliente.pedirEnteroPositivo(sc, 1, 12);
                    }


                } while (ServicioCliente.tieneDuplicados(numerosCliente) == true
                        || ServicioCliente.tieneDuplicados(solesCliente) == true);

                Arrays.sort(numerosCliente);

                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bOut);

                dos.writeInt(numerosCliente.length);
                for (int num : numerosCliente) {
                    dos.writeInt(num);
                }
                dos.flush();

                dos.writeInt(solesCliente.length);
                for (int sol : solesCliente) {
                    dos.write(sol);
                }

                byte[] bufferEnvio = bOut.toByteArray();
                DatagramPacket paqueteEnvio = new DatagramPacket(bufferEnvio, bufferEnvio.length, destino, puertoServidor);
                cliente.send(paqueteEnvio);

                byte[] bufferRecibo = new byte[1024];
                DatagramPacket paqueteRecibo = new DatagramPacket(bufferRecibo, bufferRecibo.length);
                cliente.receive(paqueteRecibo);

                String respuesta = new String(paqueteRecibo.getData(), 0, paqueteRecibo.getLength(),
                        StandardCharsets.UTF_8);
                System.out.println(respuesta);

                System.out.println("DESEA SALIR ? (y/n)");

                while (!sc.hasNextLine()) {
                    sc.next();
                }
                String decision = sc.nextLine();

                if (decision.toLowerCase() == "y") {
                    System.out.println("Cerrando comunicacion");
                    break;
                }
            }

            cliente.close();
            sc.close();
        } catch (IOException e) {
            System.err.println("Error en el cliente UDP: " + e.getMessage());
        }
    }
}