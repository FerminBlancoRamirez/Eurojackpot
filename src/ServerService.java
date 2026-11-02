import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ServerService {
    private static int[] arrayServidor = new int[7];

    public static void rellenarArrayNummerosServidor() {
        boolean esValido;
        int[] arraySinRepeticiones = new int[5];
        do {
            esValido = true;
            for (int i = 0; i < arraySinRepeticiones.length; i++) {
                arraySinRepeticiones[i] = (int) (Math.random() * 50) + 1;
            }
            if (hayRepetidos(arraySinRepeticiones)) {
                esValido = false;
            }
        } while (!esValido);

        for (int i = 0; i < 5; i++) {
            arrayServidor[i] = arraySinRepeticiones[i];
        }
    }

    public static void rellenarSolesServidor() {
        boolean esValido;
        int[] arraySinRepeticiones = new int[2];
        do {
            esValido = true;
            for (int i = 0; i < arraySinRepeticiones.length; i++) {
                arraySinRepeticiones[i] = (int) (Math.random() * 12) + 1;
            }
            if (hayRepetidos(arraySinRepeticiones)) {
                esValido = false;
            }
        } while (!esValido);

        for (int i = 0; i < 2; i++) {
            arrayServidor[5 + i] = arraySinRepeticiones[i];
        }
    }

    public static int coincidenciasNumeros(int[] arrayCliente) {
        int contadorIguales = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (arrayServidor[i] == arrayCliente[j]) {
                    contadorIguales++;
                    break;
                }
            }
        }
        return contadorIguales;
    }

    public static int coincidenciaSoles(int[] solesCliente) {
        int contadorIguales = 0;
        for (int i = 5; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                if (arrayServidor[i] == solesCliente[j]) {
                    contadorIguales++;
                    break;
                }
            }
        }
        return contadorIguales;
    }

    // MÉTODO AÑADIDO: Permite procesar el texto recibido por el socket UDP y devolver la respuesta
    public static String devolucionAlCliente(String tramaCliente) {
        String[] partes = tramaCliente.split(";");
        String[] numsStr = partes[0].split(",");
        String[] solesStr = partes[1].split(",");

        int[] numsCliente = new int[5];
        for (int i = 0; i < 5; i++) {
            numsCliente[i] = Integer.parseInt(numsStr[i]);
        }

        int[] solesCliente = new int[2];
        for (int i = 0; i < 2; i++) {
            solesCliente[i] = Integer.parseInt(solesStr[i]);
        }

        int numCoincidencias = coincidenciasNumeros(numsCliente);
        int solesCoincidencias = coincidenciaSoles(solesCliente);

        return "Aciertos -> Números: " + numCoincidencias + "/5 | Soles: " + solesCoincidencias + "/2";
    }

    public static void mostrarArrayServidor() {
        System.out.println("Array servidor (5 números + 2 soles): " + Arrays.toString(arrayServidor));
    }

    public static boolean hayRepetidos(int[] array) {
        Set<Integer> vistos = new HashSet<>();
        for (int i : array) {
            if (!vistos.add(i)) {
                return true;
            }
        }
        return false;
    }
}