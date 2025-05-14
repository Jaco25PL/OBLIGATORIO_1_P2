
package model;

import java.util.ArrayList;
import java.util.List;


public class Partida {

    
    private Jugador jugadorBlanco;
    private Jugador jugadorNegro;
    private Tablero tablero;
    private ConfiguracionPartida configuracion;
    private Jugador turnoActual;
    private int bandasColocadasEnPartida; 
    private int triangulosJugadorBlanco;
    private int triangulosJugadorNegro;
    private List<String> historialJugadas;
    private boolean partidaTerminada;
    private Jugador ganador;
    private Jugador jugadorAbandono;
    private int movimientosRealizados; 

    public Partida(Jugador jugadorBlanco, Jugador jugadorNegro, ConfiguracionPartida configuracion) {
        this.jugadorBlanco = jugadorBlanco;
        this.jugadorNegro = jugadorNegro;
        this.configuracion = configuracion;
        this.tablero = new Tablero(); 
        this.turnoActual = jugadorBlanco;
        this.bandasColocadasEnPartida = 0;
        this.triangulosJugadorBlanco = 0;
        this.triangulosJugadorNegro = 0;
        this.historialJugadas = new ArrayList<>();
        this.partidaTerminada = false;
        this.ganador = null;
        this.jugadorAbandono = null;
        this.movimientosRealizados = 0;
    }

    public Jugador getTurnoActual() {
        return turnoActual;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public int getTriangulosJugadorBlanco() {
        return triangulosJugadorBlanco;
    }

    public int getTriangulosJugadorNegro() {
        return triangulosJugadorNegro;
    }

    public List<String> getHistorialJugadas() {
        return new ArrayList<>(historialJugadas); 
    }
    
    public boolean isPartidaTerminada() {
        return partidaTerminada;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public Jugador getJugadorAbandono() {
        return jugadorAbandono;
    }

    public boolean procesarJugada(String inputJugada) {
        if (partidaTerminada) {
            System.out.println("La partida ya ha terminado.");
            return false;
        }

        String inputUpper = inputJugada.toUpperCase();

        if ("X".equals(inputUpper)) {
            abandonarPartida(turnoActual);
            return true; 
        }

        if ("H".equals(inputUpper)) {
            mostrarHistorial();
            return false; // No avanza el turno, solo muestra info
        }

        ParsedJugada jugada = parsearJugadaInput(inputUpper);
        if (jugada == null) {
            System.out.println("Formato de jugada incorrecto. Reingrese.");
            return true; // Indica que se debe pedir reingreso
        }

        if (!validarLogicaJugada(jugada)) {
            return true; 
        }

        Punto puntoActual = jugada.getOrigen();
        List<Banda> segmentosColocadosEstaJugada = new ArrayList<>();

        for (int i = 0; i < jugada.getLargo(); i++) {
            Punto puntoSiguiente = calcularPuntoSiguiente(puntoActual, jugada.getDireccion());

            if (puntoSiguiente == null || tablero.getPunto(puntoSiguiente.getColumna(), puntoSiguiente.getFila()) == null) {
                System.out.println("Error: Movimiento fuera del tablero o a punto inválido en el segmento " + (i+1));
                return true; 
            }
            
            Punto pA = tablero.getPunto(puntoActual.getColumna(), puntoActual.getFila());
            Punto pB = tablero.getPunto(puntoSiguiente.getColumna(), puntoSiguiente.getFila());

            if (pA == null || pB == null || !Tablero.sonPuntosAdyacentes(pA, pB)) {
                System.out.println("Error: Segmento inválido " + pA + " a " + pB + ". No son adyacentes o no existen.");
                return true; 
            }

            Banda nuevoSegmento = new Banda(pA, pB, turnoActual);
            
            tablero.addBanda(nuevoSegmento);
            segmentosColocadosEstaJugada.add(nuevoSegmento);
            this.bandasColocadasEnPartida++;

            int nuevosTriangulos = simularDeteccionTriangulos(nuevoSegmento); // Placeholder
            if (turnoActual.equals(jugadorBlanco)) {
                triangulosJugadorBlanco += nuevosTriangulos;
            } else {
                triangulosJugadorNegro += nuevosTriangulos;
            }

            puntoActual = puntoSiguiente; // Avanzar al siguiente punto para el próximo segmento
        }
        
        historialJugadas.add(inputJugada); // Guardar la jugada original
        movimientosRealizados++;

        if (verificarFinPartida()) {
            determinarGanadorFinal();
        } else {
            cambiarTurno();
        }
        return true; 
    }

    private int simularDeteccionTriangulos(Banda banda) {
        return 0; 
    }


    private ParsedJugada parsearJugadaInput(String input) {
        if (input == null || input.length() < 2) return null;

        char colChar = input.charAt(0);
        int fila;
        Direccion dir;
        int largo = configuracion.isLargoBandasVariable() ? 0 : configuracion.getLargoFijo(); // Default si se omite

        int dirIndex;
        if (Character.isDigit(input.charAt(1))) { 
            if (input.length() < 3) return null; 
            try {
                fila = Integer.parseInt(input.substring(1, 2));
                dirIndex = 2;
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null; 
        }
        
        if (input.length() <= dirIndex) return null;
        dir = Direccion.fromChar(input.charAt(dirIndex));
        if (dir == null) return null;

        if (input.length() > dirIndex + 1) { 
            try {
                largo = Integer.parseInt(input.substring(dirIndex + 1));
            } catch (NumberFormatException e) {
                System.out.println("Largo de banda inválido.");
                return null;
            }
        } else {
            if (configuracion.isLargoBandasVariable()) {
                 largo = 4; // Default de la consigna si se omite en modo variable
            }
        }


        Punto origen = new Punto(fila, colChar); 
        return new ParsedJugada(origen, dir, largo);
    }

    private boolean validarLogicaJugada(ParsedJugada jugada) {
        Punto origenTablero = tablero.getPunto(jugada.getOrigen().getColumna(), jugada.getOrigen().getFila());
        if (origenTablero == null) {
            System.out.println("Punto de origen inválido o fuera del tablero.");
            return false;
        }
        jugada.setOrigen(origenTablero); 

        if (configuracion.isLargoBandasVariable()) {
            if (jugada.getLargo() < ConfiguracionPartida.MIN_LARGO_BANDA || jugada.getLargo() > ConfiguracionPartida.MAX_LARGO_BANDA) {
                System.out.println("Largo de banda ("+jugada.getLargo()+") inválido. Debe ser entre " 
                        + ConfiguracionPartida.MIN_LARGO_BANDA + " y " + ConfiguracionPartida.MAX_LARGO_BANDA + ".");
                return false;
            }
        } else { 
            if (jugada.getLargo() != configuracion.getLargoFijo()) {
                 System.out.println("Largo de banda debe ser fijo de " + configuracion.getLargoFijo() 
                         + ", se intentó " + jugada.getLargo());
                 return false;
            }
        }

        if (configuracion.isRequiereContacto() && movimientosRealizados > 0) {
            boolean contactoEncontrado = false;
            if (!tablero.getBandasQueUsanPunto(origenTablero).isEmpty()) {
                contactoEncontrado = true;
            }
            if (!contactoEncontrado) {
                 System.out.println("Jugada inválida: Se requiere contacto con una banda existente y el punto de origen no lo tiene.");
                 return false;
            }
        }
        
        Punto current = origenTablero;
        for (int i = 0; i < jugada.getLargo(); i++) {
            Punto next = calcularPuntoSiguiente(current, jugada.getDireccion());
            if (next == null || tablero.getPunto(next.getColumna(), next.getFila()) == null) {
                System.out.println("La banda se sale del tablero o pasa por un punto inválido en el segmento " + (i + 1) + ".");
                return false;
            }
            if (!Tablero.sonPuntosAdyacentes(tablero.getPunto(current.getColumna(), current.getFila()), tablero.getPunto(next.getColumna(), next.getFila()))) {
                 System.out.println("Segmento " + (i+1) + " ("+current+" a "+next+") no conecta puntos adyacentes.");
                 return false;
            }
            current = next;
        }
        return true;
    }

    private Punto calcularPuntoSiguiente(Punto actual, Direccion dir) {
        if (actual == null || dir == null) return null;
        int filaActual = actual.getFila();
        char colActual = actual.getColumna();
        int nuevaFila = filaActual;
        char nuevaCol = colActual;

        switch (dir) {
            case NOROESTE: // Q
                nuevaFila--;
                nuevaCol--;
                break;
            case NORESTE:  // E
                nuevaFila--;
                nuevaCol++;
                break;
            case ESTE:     // D
                nuevaCol += 2;
                break;
            case SURESTE:  // C
                nuevaFila++;
                nuevaCol++; // 
                break;
            case SUROESTE: // Z
                nuevaFila++;
                nuevaCol--;
                break;
            case OESTE:    // A
                nuevaCol -= 2;
                break;
        }
        try {
            return new Punto(nuevaFila, nuevaCol);
        } catch (IllegalArgumentException e) {
            return null; 
        }
    }


    private void cambiarTurno() {
        if (turnoActual.equals(jugadorBlanco)) {
            turnoActual = jugadorNegro;
        } else {
            turnoActual = jugadorBlanco;
        }
        System.out.println("Turno del jugador: " + turnoActual.getUsername());
    }

    private boolean verificarFinPartida() {
        if (this.bandasColocadasEnPartida >= configuracion.getCantidadBandasFin()) {
            partidaTerminada = true;
            return true;
        }
        return false;
    }

    private void determinarGanadorFinal() {
        if (!partidaTerminada) return; 
        if (jugadorAbandono != null) { 
            ganador = (jugadorAbandono.equals(jugadorBlanco)) ? jugadorNegro : jugadorBlanco;
        } else {
            if (triangulosJugadorBlanco > triangulosJugadorNegro) {
                ganador = jugadorBlanco;
            } else if (triangulosJugadorNegro > triangulosJugadorBlanco) {
                ganador = jugadorNegro;
            } else {
                ganador = null; // Empate
            }
        }
        
        if (ganador != null) {
            ganador.incrementarPartidasGanadas();
            ganador.incrementarRachaActual();
            ganador.actualizarRachaMaxima();
            Jugador perdedor = ganador.equals(jugadorBlanco) ? jugadorNegro : jugadorBlanco;
            perdedor.resetRachaActual();
            System.out.println("Partida finalizada. Ganador: " + ganador.getUsername());
        } else if (jugadorAbandono == null) { // Empate sin abandono
            jugadorBlanco.resetRachaActual();
            jugadorNegro.resetRachaActual();
            System.out.println("Partida finalizada. Es un empate!");
        }
        if (jugadorAbandono != null) {
             jugadorAbandono.resetRachaActual();
        }


        System.out.println("--- Fin de la Partida ---");
        System.out.println(jugadorBlanco.getUsername() + " (Blanco): " + triangulosJugadorBlanco + " triángulos.");
        System.out.println(jugadorNegro.getUsername() + " (Negro): " + triangulosJugadorNegro + " triángulos.");
        if (ganador != null) {
            System.out.println("¡Felicidades " + ganador.getUsername() + "!");
        } else if (jugadorAbandono == null) {
            System.out.println("¡Ha sido un empate!");
        }
    }

    private void abandonarPartida(Jugador jugadorQueAbandona) {
        this.partidaTerminada = true;
        this.jugadorAbandono = jugadorQueAbandona;
        System.out.println("El jugador " + jugadorQueAbandona.getUsername() + " ha abandonado la partida.");
        determinarGanadorFinal();
    }

    private void mostrarHistorial() {
        System.out.println("--- Historial de Jugadas ---");
        if (historialJugadas.isEmpty()) {
            System.out.println("No se han realizado jugadas aún.");
        } else {
            for (int i = 0; i < historialJugadas.size(); i++) {
                System.out.println((i + 1) + ". " + historialJugadas.get(i));
            }
        }
        System.out.println("----------------------------");
    }

    private static class ParsedJugada {
        private Punto origen; 
        private Direccion direccion;
        private int largo;

        public ParsedJugada(Punto origen, Direccion direccion, int largo) {
            this.origen = origen;
            this.direccion = direccion;
            this.largo = largo;
        }

        public Punto getOrigen() { return origen; }
        public void setOrigen(Punto origen) { this.origen = origen; } 
        public Direccion getDireccion() { return direccion; }
        public int getLargo() { return largo; }
    }
}
