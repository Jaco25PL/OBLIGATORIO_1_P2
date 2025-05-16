/*
 * Autores: [Matías Piedra 354007], [Joaquin Piedra 304804] 
 */

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

    // crea una nueva partida.
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

    // obtiene el turno actual.
    public Jugador getTurnoActual() {
        return turnoActual;
    }

    // obtiene el tablero.
    public Tablero getTablero() {
        return tablero;
    }

    // obtiene triángulos jugador blanco.
    public int getTriangulosJugadorBlanco() {
        return triangulosJugadorBlanco;
    }

    // obtiene triángulos jugador negro.
    public int getTriangulosJugadorNegro() {
        return triangulosJugadorNegro;
    }

    // obtiene historial de jugadas.
    public List<String> getHistorialJugadas() {
        return new ArrayList<>(historialJugadas); 
    }
    
    // verifica si partida terminó.
    public boolean isPartidaTerminada() {
        return partidaTerminada;
    }

    // obtiene el ganador.
    public Jugador getGanador() {
        return ganador;
    }

    // obtiene jugador que abandonó.
    public Jugador getJugadorAbandono() {
        return jugadorAbandono;
    }

    // obtiene bandas colocadas.
    public int getBandasColocadasEnPartida() {
        return bandasColocadasEnPartida;
    }

    // procesa la jugada ingresada.
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
            return false; 
        }

        
        ParsedJugada jugada = parsearJugadaInput(inputUpper);
        if (jugada == null) {
            System.out.println("Formato de jugada incorrecto. Reingrese.");
            return true; 
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

            int nuevosTriangulos = detectarNuevosTriangulosConBanda(nuevoSegmento);
            if (turnoActual.equals(jugadorBlanco)) {
                triangulosJugadorBlanco += nuevosTriangulos;
            } else {
                triangulosJugadorNegro += nuevosTriangulos;
            }


            puntoActual = puntoSiguiente; 
        }
        
        this.bandasColocadasEnPartida++;

        historialJugadas.add(inputJugada); 
        movimientosRealizados++;

        
        if (verificarFinPartida()) {
            determinarGanadorFinal();
            
        } else {
            cambiarTurno();
        }
        return true; 
    }

    // detecta nuevos triángulos.
    private int detectarNuevosTriangulosConBanda(Banda banda) {
        int nuevos = 0;
        Punto a = banda.getPuntoA();
        Punto b = banda.getPuntoB();
        Jugador jugador = banda.getJugador();
        
        List<Punto> adyacentesA = tablero.getPuntosAdyacentes(a);
        List<Punto> adyacentesB = tablero.getPuntosAdyacentes(b);
        
        for (Punto c : adyacentesA) {
            if (adyacentesB.contains(c)) {
                // System.out.println("Found common adjacent point: " + c); // Debug
                
                Banda acBanda = null;
                Banda bcBanda = null;
                
                for (Banda other : tablero.getBandas()) {
                    if (other.getJugador().equals(jugador)) {
                        if ((other.getPuntoA().equals(a) && other.getPuntoB().equals(c)) ||
                            (other.getPuntoA().equals(c) && other.getPuntoB().equals(a))) {
                            acBanda = other;
                        }
                        
                        if ((other.getPuntoA().equals(b) && other.getPuntoB().equals(c)) ||
                            (other.getPuntoA().equals(c) && other.getPuntoB().equals(b))) {
                            bcBanda = other;
                        }
                    }
                }
                
                if (acBanda != null && bcBanda != null) {
                    // System.out.println("Found triangle with bands: " + banda + ", " + acBanda + ", " + bcBanda); // Debug
                    
                    Triangulo nuevoTriangulo = new Triangulo(a, b, c);
                    boolean yaExiste = false;
                    
                    for (Triangulo existente : tablero.getTriangulosGanados()) {
                        if (existente.equals(nuevoTriangulo)) {
                            yaExiste = true;
                            // System.out.println("Triangle already exists in won triangles"); // Debug
                            break;
                        }
                    }
                    
                    if (!yaExiste) {
                        nuevoTriangulo.setJugadorGanador(jugador, jugador.equals(jugadorBlanco));
                        tablero.addTrianguloGanado(nuevoTriangulo);
                        // System.out.println("New triangle added for player " + jugador.getNombre() + (jugador.equals(jugadorBlanco) ? " (White)" : " (Black)")); // Debug
                        nuevos++;
                    }
                } else {
                    // System.out.println("Missing bands for triangle. AC band: " + (acBanda != null ? "found" : "missing") + ", BC band: " + (bcBanda != null ? "found" : "missing")); // Debug
                }
            }
        }
        
        // System.out.println("Found " + nuevos + " new triangles"); // Debug
        return nuevos;
    }

    // parsea entrada de jugada.
    private ParsedJugada parsearJugadaInput(String input) {
        if (input == null || input.length() < 2) return null;

        char colChar = input.charAt(0);
        int fila;
        Direccion dir;
        int largo = configuracion.isLargoBandasVariable() ? 0 : configuracion.getLargoFijo(); 

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
        // dir = Direccion.fromChar(input.charAt(dirIndex));
        dir = new Direccion(input.charAt(dirIndex));
        // if (dir == null) return null;

        if (input.length() > dirIndex + 1) { 
            try {
                largo = Integer.parseInt(input.substring(dirIndex + 1));
            } catch (NumberFormatException e) {
                System.out.println("Largo de banda inválido en el input.");
                return null;
            }
        } else {
            if (configuracion.isLargoBandasVariable()) {
                 largo = 4; // Default largo if variable and not specified
            }
            // If not variable, 'largo' is already set to configuracion.getLargoFijo()
        }

        try {
            Punto origen = new Punto(fila, colChar); 
            return new ParsedJugada(origen, dir, largo);
        } catch (IllegalArgumentException e) {
            System.out.println("Punto de origen inválido: " + e.getMessage());
            return null;
        }
    }

    // valida lógica de jugada.
    private boolean validarLogicaJugada(ParsedJugada jugada) {
        Punto origenTablero = tablero.getPunto(jugada.getOrigen().getColumna(), jugada.getOrigen().getFila());
        if (origenTablero == null) {
            System.out.println("Punto de origen inválido o fuera del tablero.");
            return false;
        }
        jugada.setOrigen(origenTablero); 

        
        if (configuracion.isLargoBandasVariable()) {
            if (jugada.getLargo() < configuracion.getMinLargoBandaConstant() || jugada.getLargo() > configuracion.getMaxLargoBandaConstant()) {
                System.out.println("Largo de banda ("+jugada.getLargo()+") inválido. Debe ser entre " + configuracion.getMinLargoBandaConstant() + " y " + configuracion.getMaxLargoBandaConstant() + ".");
                return false;
            }
        } else { 
            if (jugada.getLargo() != configuracion.getLargoFijo()) {
                 System.out.println("Largo de banda debe ser fijo de " + configuracion.getLargoFijo() + ", se intentó " + jugada.getLargo());
                 return false;
            }
        }

        
        if (configuracion.isRequiereContacto() && movimientosRealizados > 0) {
            boolean contactoEncontrado = false;
            
            if (!tablero.getBandasQueUsanPunto(origenTablero).isEmpty()) {
                contactoEncontrado = true;
            }
            // Check contact along the path of the new band
            Punto currentPathPoint = origenTablero;
            for (int i = 0; i < jugada.getLargo() && !contactoEncontrado; i++) {
                Punto nextPathPoint = calcularPuntoSiguiente(currentPathPoint, jugada.getDireccion());
                if (nextPathPoint == null) break; // Path goes off board
                Punto tableroNextPathPoint = tablero.getPunto(nextPathPoint.getColumna(), nextPathPoint.getFila());
                if (tableroNextPathPoint == null) break; // Path goes to invalid point

                if (!tablero.getBandasQueUsanPunto(tableroNextPathPoint).isEmpty()) {
                    contactoEncontrado = true;
                }
                currentPathPoint = tableroNextPathPoint;
            }
            
            if (!contactoEncontrado) {
                 System.out.println("Jugada inválida: Se requiere contacto con una banda existente y la nueva banda no lo tiene.");
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
            Punto currentOnBoard = tablero.getPunto(current.getColumna(), current.getFila());
            Punto nextOnBoard = tablero.getPunto(next.getColumna(), next.getFila());
            if (currentOnBoard == null || nextOnBoard == null || !Tablero.sonPuntosAdyacentes(currentOnBoard, nextOnBoard)) {
                 System.out.println("Segmento " + (i+1) + " ("+current+" a "+next+") no conecta puntos adyacentes válidos.");
                 return false;
            }
            current = next;
        }

        return true;
    }

    
    private Punto calcularPuntoSiguiente(Punto actual, Direccion dir) { // Signature remains same
        if (actual == null || dir == null) return null;
        int filaActual = actual.getFila();
        char colActual = actual.getColumna();
        int nuevaFila = filaActual;
        char nuevaCol = colActual;

        // OLD: switch (dir) {
        // OLD:    case NOROESTE: 
        // NEW:
        switch (dir.getCodigo()) {
            case 'Q': // Corresponds to NOROESTE
                nuevaFila--;
                nuevaCol--;
                break;
            case 'E':  // Corresponds to NORESTE
                nuevaFila--;
                nuevaCol++;
                break;
            case 'D':     // Corresponds to ESTE
                nuevaCol += 2;
                break;
            case 'C':  // Corresponds to SURESTE
                nuevaFila++;
                nuevaCol++; 
                break;
            case 'Z': // Corresponds to SUROESTE
                nuevaFila++;
                nuevaCol--;
                break;
            case 'A':    // Corresponds to OESTE
                nuevaCol -= 2;
                break;
            default:
                // Handle unknown direction code if necessary, though current Direccion constructor doesn't prevent it
                return null; 
        }
        
        try {
            return new Punto(nuevaFila, nuevaCol);
        } catch (IllegalArgumentException e) {
            return null; 
        }
    }
    // private Punto calcularPuntoSiguiente(Punto actual, Direccion dir) {
    //     if (actual == null || dir == null) return null;
    //     int filaActual = actual.getFila();
    //     char colActual = actual.getColumna();
    //     int nuevaFila = filaActual;
    //     char nuevaCol = colActual;

    //     switch (dir) {
    //         case NOROESTE: 
    //             nuevaFila--;
    //             nuevaCol--;
    //             break;
    //         case NORESTE:  
    //             nuevaFila--;
    //             nuevaCol++;
    //             break;
    //         case ESTE:     
    //             nuevaCol += 2;
    //             break;
    //         case SURESTE:  
    //             nuevaFila++;
    //             nuevaCol++; 
    //             break;
    //         case SUROESTE: 
    //             nuevaFila++;
    //             nuevaCol--;
    //             break;
    //         case OESTE:    
    //             nuevaCol -= 2;
    //             break;
    //     }
        
    //     try {
    //         return new Punto(nuevaFila, nuevaCol);
    //     } catch (IllegalArgumentException e) {
    //         return null; 
    //     }
    // }

    // cambia el turno.
    private void cambiarTurno() {
        turnoActual = (turnoActual.equals(jugadorBlanco)) ? jugadorNegro : jugadorBlanco;
        // System.out.println("Turno del jugador: " + turnoActual.getNombre()); // Debug or game flow info
    }

    // verifica fin de partida.
    private boolean verificarFinPartida() {
        if (this.bandasColocadasEnPartida >= configuracion.getCantidadBandasFin()) {
            partidaTerminada = true;
            return true;
        }
        return false;
    }

    // determina ganador final.
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
        } else if (jugadorAbandono == null) { // Empate sin abandono
            jugadorBlanco.resetRachaActual();
            jugadorNegro.resetRachaActual();
        }
        
        if (jugadorAbandono != null && !jugadorAbandono.equals(ganador)) { // Si hubo abandono, el que abandonó resetea racha
             jugadorAbandono.resetRachaActual();
        }
    }

    // abandona la partida.
    private void abandonarPartida(Jugador jugadorQueAbandona) {
        this.partidaTerminada = true;
        this.jugadorAbandono = jugadorQueAbandona;
        System.out.println("El jugador " + jugadorQueAbandona.getNombre() + " ha abandonado la partida.");
        determinarGanadorFinal(); 
    }

    // muestra historial de jugadas.
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

    // clase para jugada parseada.
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