
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
                        numerosCliente[i] = ServicioCliente.pedirSoloEntero(sc);
                    }
                    System.out.println("Introduce soles entre 1-12 :");
                    for (int i = 0; i < solesCliente.length; i++) {
                        solesCliente[i] = ServicioCliente.pedirSoloEntero(sc);
                    }

                } while (ServicioCliente.tieneDuplicados(numerosCliente) == true
                        || ServicioCliente.tieneDuplicados(solesCliente) == true
                        || ServicioCliente.tieneFueraDeRango(numerosCliente, 1, 50)
                        || ServicioCliente.tieneFueraDeRango(solesCliente, 1, 12));

                Arrays.sort(numerosCliente);

                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bOut);

                dos.writeInt(numerosCliente.length);
                for (int num : numerosCliente) {
                    dos.writeInt(num);
                }
                dos.writeInt(solesCliente.length);
                for (int sol : solesCliente) {
                    dos.writeInt(sol);
                }
                dos.flush();

                byte[] bufferEnvio = bOut.toByteArray();
                DatagramPacket paqueteEnvio = new DatagramPacket(bufferEnvio, bufferEnvio.length, destino,
                        puertoServidor);
                cliente.send(paqueteEnvio);

                byte[] bufferRecibo = new byte[1024];
                DatagramPacket paqueteRecibo = new DatagramPacket(bufferRecibo, bufferRecibo.length);
                cliente.receive(paqueteRecibo);

                String respuesta = new String(paqueteRecibo.getData(), 0, paqueteRecibo.getLength(),
                        StandardCharsets.UTF_8);
                System.out.println(respuesta);

                String decision = "";
                do {
                    System.out.println("DESEA SALIR ? (y / n)");

                    while (!sc.hasNextLine()) {
                        sc.next();
                    }
                    decision = sc.nextLine().trim();

                    if (!decision.equalsIgnoreCase("y") && !decision.equalsIgnoreCase("n")) {
                        System.out.println("Opción no válida. Introduce y/n");
                    }

                } while (!decision.equalsIgnoreCase("y") && !decision.equalsIgnoreCase("n"));

                byte[] bufferDecision = decision.getBytes(StandardCharsets.UTF_8);
                DatagramPacket paqueteDecision = new DatagramPacket(bufferDecision, bufferDecision.length, destino,
                        puertoServidor);
                cliente.send(paqueteDecision);

                if (decision.toLowerCase().equalsIgnoreCase("y")) {
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