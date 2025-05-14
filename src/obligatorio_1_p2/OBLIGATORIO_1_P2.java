
package obligatorio_1_p2;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import model.Interfaz;

public class OBLIGATORIO_1_P2 {

    public static void main(String[] args) {

        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error grave: Codificación UTF-8 no soportada. " + e.getMessage());
        }

        Interfaz interfazDeUsuario = new Interfaz();
        interfazDeUsuario.iniciarAplicacion();
    }
}
