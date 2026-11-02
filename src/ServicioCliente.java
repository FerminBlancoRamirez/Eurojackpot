import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

public class ServicioCliente {

    public static int pedirEnteroPositivo(Scanner sc, int min, int max) {
        int numero;
        do {
            while (!sc.hasNextInt()) {
                System.out.println("Error: Debes introducir un número entero válido.");
                sc.next();
            }
            numero = sc.nextInt();
            if ((numero < min || numero > max)) {
                System.out.println("Error: El número debe estar comprendido entre " + min + " y " + max + ". Inténtalo de nuevo.");
            }
        } while ((numero < min || numero > max));
        return numero;
    }

    public static boolean tieneDuplicados(int[] numeros) {
        Set<Integer> set = new HashSet<>();
        for (int numero : numeros) {
            if (!set.add(numero)) {
                System.out.println("ERROR: No puede haber números duplicados en el boleto.");
                return true;
            }
        }
        return false;
    }
}