import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

public class ServicioCliente {

    public static int pedirSoloEntero(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.println("Error: Debes introducir un número entero válido.");
            sc.next();
        }
        int numero = sc.nextInt();
        sc.nextLine();
        return numero;
    }

    public static boolean tieneFueraDeRango(int[] numeros, int min, int max) {
        for (int numero : numeros) {
            if (numero < min || numero > max) {
                System.out.println("ERROR: Hay números fuera del rango permitido (" + min + " - " + max + ").");
                return true;
            }
        }
        return false;
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