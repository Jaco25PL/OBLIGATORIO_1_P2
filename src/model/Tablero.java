//  * Autores: [Matías Piedra 354007], [Joaquin Piedra 304804] 


package model;

// import model.Punto;
// import model.Jugador;
// import model.Banda;
import java.util.ArrayList;
import java.util.List;

public class Tablero {

    private final List<Punto> puntosDisponibles;
    private final List<Banda> bandasColocadas;
    private final List<Triangulo> triangulosGanados;

    // crea un nuevo tablero.
    public Tablero() {
        this.puntosDisponibles = new ArrayList<>();
        this.bandasColocadas = new ArrayList<>();
        this.triangulosGanados = new ArrayList<>();
        this.inicializarPuntosDelTablero();
    }

    // inicializa los puntos del tablero.
    private void inicializarPuntosDelTablero() {
        agregarPuntosFila(1, new char[]{'D', 'F', 'H', 'J'});
        agregarPuntosFila(2, new char[]{'C', 'E', 'G', 'I', 'K'});
        agregarPuntosFila(3, new char[]{'B', 'D', 'F', 'H', 'J', 'L'});
        agregarPuntosFila(4, new char[]{'A', 'C', 'E', 'G', 'I', 'K', 'M'});
        agregarPuntosFila(5, new char[]{'B', 'D', 'F', 'H', 'J', 'L'});
        agregarPuntosFila(6, new char[]{'C', 'E', 'G', 'I', 'K'});
        agregarPuntosFila(7, new char[]{'D', 'F', 'H', 'J'});
    }

    // agrega puntos a una fila.
    private void agregarPuntosFila(int fila, char[] columnas) {
        for (char columna : columnas) {
            this.puntosDisponibles.add(new Punto(fila, columna));
        }
    }

    // devuelve punto por coordenadas.
    public Punto getPunto(char columna, int fila) {
        for (Punto p : this.puntosDisponibles) {
            if (p.getFila() == fila && p.getColumna() == Character.toUpperCase(columna)) {
                return p;
            }
        }
        return null;
    }
    
    // devuelve punto por string.
    public Punto getPunto(String coordenada) {
        if (coordenada == null || coordenada.length() < 2) {
            return null;
        }
        char col = Character.toUpperCase(coordenada.charAt(0));
        try {
            int fil = Integer.parseInt(coordenada.substring(1));
            return getPunto(col, fil);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // agrega banda al tablero.
    public void addBanda(Banda banda) {
        if (banda != null) {
            this.bandasColocadas.add(banda);
        }
    }

    // devuelve todas las bandas.
    public List<Banda> getBandas() {
        return this.bandasColocadas;
    }

    // agrega triángulo ganado.
    public void addTrianguloGanado(Triangulo triangulo) {
        if (triangulo != null) {
            this.triangulosGanados.add(triangulo);
        }
    }

    // devuelve triángulos ganados.
    public List<Triangulo> getTriangulosGanados() {
        return this.triangulosGanados;
    }

    // triángulos ganados por jugador.
    public List<Triangulo> getTriangulosGanadosPor(Jugador jugador) {
        List<Triangulo> triangulosDelJugador = new ArrayList<>();
        if (jugador != null) {
            for (Triangulo t : this.triangulosGanados) {
                if (jugador.equals(t.getJugadorGanador())) {
                    triangulosDelJugador.add(t);
                }
            }
        }
        return triangulosDelJugador;
    }
    
    // bandas que usan un punto.
    public List<Banda> getBandasQueUsanPunto(Punto punto) {
        List<Banda> bandasDelPunto = new ArrayList<>();
        if (punto != null) {
            for (Banda b : this.bandasColocadas) {
                if (b.getPuntoA().equals(punto) || b.getPuntoB().equals(punto)) {
                    bandasDelPunto.add(b);
                }
            }
        }
        return bandasDelPunto;
    }

    // verifica si puntos adyacentes.
    public static boolean sonPuntosAdyacentes(Punto p1, Punto p2) {
        if (p1 == null || p2 == null || p1.equals(p2)) {
            return false;
        }
        int diffFilas = Math.abs(p1.getFila() - p2.getFila());
        int diffCols = Math.abs(p1.getColumna() - p2.getColumna());

        boolean mismaFilaAdy = (diffFilas == 0 && diffCols == 2);
        boolean filaAdyacenteAdy = (diffFilas == 1 && diffCols == 1);

        return mismaFilaAdy || filaAdyacenteAdy;
    }

    // devuelve puntos adyacentes.
    public List<Punto> getPuntosAdyacentes(Punto punto) {
        List<Punto> adyacentes = new ArrayList<>();
        if (punto == null) {
            return adyacentes;
        }
        for (Punto p : this.puntosDisponibles) {
            if (sonPuntosAdyacentes(punto, p)) {
                adyacentes.add(p);
            }
        }
        return adyacentes;
    }

    // representación textual del tablero.
    @Override
    public String toString() {
        final int numFilasOriginal = 7;
        final int numDisplayFilas = numFilasOriginal * 2 - 1;
        final int numColsLetras = 13; // A to M
        final int anchoDisplay = numColsLetras * 2 -1;
        
        char[][] displayGrid = new char[numDisplayFilas][anchoDisplay];

        for (int i = 0; i < numDisplayFilas; i++) {
            for (int j = 0; j < anchoDisplay; j++) {
                displayGrid[i][j] = ' ';
            }
        }

        for (Punto p : this.puntosDisponibles) {
            int displayFila = (p.getFila() - 1) * 2;
            int displayCol = (p.getColumna() - 'A') * 2; 
            if(displayFila >= 0 && displayFila < numDisplayFilas && displayCol >=0 && displayCol < anchoDisplay){
                 displayGrid[displayFila][displayCol] = '*'; 
            }
        }
        
        for (Banda banda : this.bandasColocadas) {
            Punto pA = banda.getPuntoA(); 
            Punto pB = banda.getPuntoB(); 

            int origFilaA = pA.getFila() - 1; // 0-indexed
            int displayColA = (pA.getColumna() - 'A') * 2;
            int displayColB = (pB.getColumna() - 'A') * 2;
            int targetBandCol = (displayColA + displayColB) / 2;

            if (pA.getFila() == pB.getFila()) { // Horizontal band
                int targetBandRow = origFilaA * 2;
                if (targetBandRow >= 0 && targetBandRow < numDisplayFilas &&
                    targetBandCol - 1 >= 0 && targetBandCol + 1 < anchoDisplay) {
                    displayGrid[targetBandRow][targetBandCol - 1] = '-';
                    displayGrid[targetBandRow][targetBandCol] = '-';
                    displayGrid[targetBandRow][targetBandCol + 1] = '-';
                }
            } else { // Diagonal band
                int targetBandRow = origFilaA * 2 + 1; // Band is between point rows
                char bandChar = (pB.getColumna() < pA.getColumna()) ? '/' : '\\'; // Determine slant
                if (targetBandRow >= 0 && targetBandRow < numDisplayFilas &&
                    targetBandCol >= 0 && targetBandCol < anchoDisplay) {
                    displayGrid[targetBandRow][targetBandCol] = bandChar;
                }
            }
        }
        
        for (Triangulo t : this.triangulosGanados) {
            Punto p1 = t.getPunto1();
            Punto p2 = t.getPunto2();
            Punto p3 = t.getPunto3();
            
            char simboloTri;
            if (t.getJugadorGanador() != null) {
                if (t.isWhitePlayer()) { // As per Interfaz.java usage for display
                    simboloTri = '□'; 
                } else {
                    simboloTri = '■'; 
                }
            } else {
                simboloTri = 'T';  // Fallback, should not happen if logic is correct
            }

            int r1=p1.getFila(), r2=p2.getFila(), r3=p3.getFila();
            char c1=p1.getColumna(), c2=p2.getColumna(), c3=p3.getColumna();
            
            // Calculate the display position for the triangle symbol (center of the triangle)
            int targetFilaDisplayOriginal = -1;
            int targetColDisplayOriginal = -1;

            // This logic attempts to find the center of the triangle for placing the symbol
            if (r1==r2) { // p1, p2 are horizontal, p3 is the peak
                targetFilaDisplayOriginal = r1-1; // 0-indexed original row
                targetColDisplayOriginal = ( (c1-'A')*2 + (c2-'A')*2 ) / 2 +1; // Midpoint column for symbol
            } else if (r1==r3) { // p1, p3 are horizontal, p2 is the peak
                targetFilaDisplayOriginal = r1-1;
                targetColDisplayOriginal = ( (c1-'A')*2 + (c3-'A')*2 ) / 2 +1;
            } else if (r2==r3) { // p2, p3 are horizontal, p1 is the peak
                targetFilaDisplayOriginal = r2-1;
                targetColDisplayOriginal = ( (c2-'A')*2 + (c3-'A')*2 ) / 2 +1;
            }

            if(targetFilaDisplayOriginal != -1) { // If a horizontal base was found
                int finalTargetFila = targetFilaDisplayOriginal * 2; // Triangle symbol on point row
                
                if(finalTargetFila >= 0 && finalTargetFila < numDisplayFilas &&
                   targetColDisplayOriginal >=0 && targetColDisplayOriginal < anchoDisplay) {
                    if (displayGrid[finalTargetFila][targetColDisplayOriginal] == ' ') { 
                         displayGrid[finalTargetFila][targetColDisplayOriginal] = simboloTri;
                    } else { 
                        // Fallback: if center is occupied, try to place on one of the vertices (peak)
                        Punto pico = null;
                        if(r1 != r2 && r1 != r3) pico = p1; // p1 is peak
                        else if (r2 != r1 && r2 != r3) pico = p2; // p2 is peak
                        else pico = p3; // p3 is peak
                        
                        if (pico != null) {
                            int picoDisplayFila = (pico.getFila()-1)*2;
                            int picoDisplayCol = (pico.getColumna()-'A')*2;
                            if(picoDisplayFila >= 0 && picoDisplayFila < numDisplayFilas && 
                               picoDisplayCol >=0 && picoDisplayCol < anchoDisplay) {
                                displayGrid[picoDisplayFila][picoDisplayCol] = simboloTri; 
                            }
                        }
                    }
                }
            }
        }    

        StringBuilder sb = new StringBuilder();
        // Column headers
        sb.append("  "); // Indent for row numbers if they were added
        for (char c = 'A'; c < 'A' + numColsLetras; c++) {
            sb.append(c);
            if (c < 'A' + numColsLetras - 1) {
                sb.append(" "); // Space between column letters
            }
        }
        sb.append("\n");
        sb.append("\n"); // Extra newline for spacing
        
        // Grid content
        for (int i = 0; i < numDisplayFilas; i++) {
            // Row numbers could be added here if desired: sb.append(String.format("%2d ", i/2 + 1));
            for (int j = 0; j < anchoDisplay; j++) {
                sb.append(displayGrid[i][j]);
            }
            sb.append("\n"); 
        }
        return sb.toString();
    }
}