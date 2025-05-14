package model;

import java.util.ArrayList;
import java.util.List;

public class Tablero {

    private final List<Punto> puntosDisponibles;
    private final List<Banda> bandasColocadas;
    private final List<Triangulo> triangulosGanados;

    public Tablero() {
        this.puntosDisponibles = new ArrayList<>();
        this.bandasColocadas = new ArrayList<>();
        this.triangulosGanados = new ArrayList<>();
        this.inicializarPuntosDelTablero();
    }

    private void inicializarPuntosDelTablero() {
        // Fila 1: D, F, H, J
        agregarPuntosFila(1, new char[]{'D', 'F', 'H', 'J'});
        // Fila 2: C, E, G, I, K
        agregarPuntosFila(2, new char[]{'C', 'E', 'G', 'I', 'K'});
        // Fila 3: B, D, F, H, J, L
        agregarPuntosFila(3, new char[]{'B', 'D', 'F', 'H', 'J', 'L'});
        // Fila 4: A, C, E, G, I, K, M
        agregarPuntosFila(4, new char[]{'A', 'C', 'E', 'G', 'I', 'K', 'M'});
        // Fila 5: B, D, F, H, J, L (simétrica a la fila 3)
        agregarPuntosFila(5, new char[]{'B', 'D', 'F', 'H', 'J', 'L'});
        // Fila 6: C, E, G, I, K (simétrica a la fila 2)
        agregarPuntosFila(6, new char[]{'C', 'E', 'G', 'I', 'K'});
        // Fila 7: D, F, H, J (simétrica a la fila 1)
        agregarPuntosFila(7, new char[]{'D', 'F', 'H', 'J'});
    }

    private void agregarPuntosFila(int fila, char[] columnas) {
        for (char columna : columnas) {
            this.puntosDisponibles.add(new Punto(fila, columna));
        }
    }

    public Punto getPunto(char columna, int fila) {
        for (Punto p : this.puntosDisponibles) {
            if (p.getFila() == fila && p.getColumna() == Character.toUpperCase(columna)) {
                return p;
            }
        }
        return null; // No se encontró el punto
    }
    
    public Punto getPunto(String coordenada) {
        if (coordenada == null || coordenada.length() < 2) {
            return null;
        }
        char col = Character.toUpperCase(coordenada.charAt(0));
        try {
            int fil = Integer.parseInt(coordenada.substring(1));
            return getPunto(col, fil);
        } catch (NumberFormatException e) {
            return null; // Formato de fila inválido
        }
    }


    public void addBanda(Banda banda) {
        if (banda != null) {
            this.bandasColocadas.add(banda);
        }
    }

    public List<Banda> getBandas() {
        return this.bandasColocadas;
    }

    public void addTrianguloGanado(Triangulo triangulo) {
        if (triangulo != null) {
            this.triangulosGanados.add(triangulo);
        }
    }

    public List<Triangulo> getTriangulosGanados() {
        return this.triangulosGanados;
    }

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


    @Override
    public String toString() {
        final int numFilas = 7;
        final int numColsLetras = 13; // A-M
        final int anchoDisplay = numColsLetras * 2 -1; // Para puntos y espacios/bandas horizontales
        
        char[][] displayGrid = new char[numFilas][anchoDisplay];

        for (int i = 0; i < numFilas; i++) {
            for (int j = 0; j < anchoDisplay; j++) {
                displayGrid[i][j] = ' ';
            }
        }

        for (Punto p : this.puntosDisponibles) {
            int displayFila = p.getFila() - 1;
            int displayCol = (p.getColumna() - 'A') * 2; // Cada letra ocupa una posición par.
            if(displayFila >= 0 && displayFila < numFilas && displayCol >=0 && displayCol < anchoDisplay){
                 displayGrid[displayFila][displayCol] = '*'; // Símbolo para un punto.
            }
        }
        
        for (Banda banda : this.bandasColocadas) {
            Punto pA = banda.getPuntoA(); // Punto "menor"
            Punto pB = banda.getPuntoB(); // Punto "mayor"

            int filaA = pA.getFila() - 1;
            int colAIdx = pA.getColumna() - 'A';
            int displayColA = colAIdx * 2;

            int filaB = pB.getFila() - 1;
            int colBIdx = pB.getColumna() - 'A';
            // int displayColB = colBIdx * 2; // No siempre necesario

            if (filaA == filaB) { // Banda horizontal
                // La banda está entre pA y pB, en la posición impar.
                if (displayColA + 1 < anchoDisplay) {
                    displayGrid[filaA][displayColA + 1] = '-';
                }
            } else { // Banda diagonal (pA está arriba de pB o en la misma fila y a la izquierda)
                if (colBIdx < colAIdx) { // pB está a la izquierda de pA (ej. pA=(2,D), pB=(3,C)) -> Banda '/'
                     if (displayColA - 1 >= 0) {
                        displayGrid[filaA][displayColA - 1] = '/';
                    }
                } else { // pB está a la derecha de pA (ej. pA=(2,C), pB=(3,D)) -> Banda '\'
                    if (displayColA + 1 < anchoDisplay) {
                        displayGrid[filaA][displayColA + 1] = '\\';
                    }
                }
            }
        }
        
        // 4. Dibujar los triángulos ganados (marcar el centro).
        for (Triangulo t : this.triangulosGanados) {
            Punto p1 = t.getPunto1();
            Punto p2 = t.getPunto2();
            Punto p3 = t.getPunto3();
            
            char simboloTri = 'T'; // Placeholder. Debería ser '□' o '■' según ConsignaObligatorio.md y el jugador.
            if (t.getJugadorGanador() != null) {
            }

            int r1=p1.getFila(), r2=p2.getFila(), r3=p3.getFila();
            char c1=p1.getColumna(), c2=p2.getColumna(), c3=p3.getColumna();
            int targetFilaDisplay = -1, targetColDisplay = -1;

            if (r1==r2) { // p1,p2 en la misma fila, p3 en otra
                targetFilaDisplay = r1-1;
                targetColDisplay = ( (c1-'A')*2 + (c2-'A')*2 ) / 2 + 1; // Espacio entre p1 y p2
            } else if (r1==r3) { // p1,p3 en la misma fila, p2 en otra
                targetFilaDisplay = r1-1;
                targetColDisplay = ( (c1-'A')*2 + (c3-'A')*2 ) / 2 + 1;
            } else if (r2==r3) { // p2,p3 en la misma fila, p1 en otra
                targetFilaDisplay = r2-1;
                targetColDisplay = ( (c2-'A')*2 + (c3-'A')*2 ) / 2 + 1;
            }
            
            if(targetFilaDisplay != -1 && targetColDisplay != -1 &&
               targetFilaDisplay >= 0 && targetFilaDisplay < numFilas &&
               targetColDisplay >=0 && targetColDisplay < anchoDisplay) {
                if (displayGrid[targetFilaDisplay][targetColDisplay] == ' ') { // Solo si es un espacio vacío
                     displayGrid[targetFilaDisplay][targetColDisplay] = simboloTri;
                } else { 
                    // Si el espacio preferido está ocupado (ej. por una banda),
                    // intentamos colocarlo en el punto "pico" del triángulo.
                    Punto pico = null;
                    if(r1 != r2 && r1 != r3) pico = p1;
                    else if (r2 != r1 && r2 != r3) pico = p2;
                    else pico = p3; // p3 es el pico si r1==r2, o si r1,r2,r3 son distintos (no debería pasar para triángulo válido)
                    if (pico != null) {
                        displayGrid[pico.getFila()-1][(pico.getColumna()-'A')*2] = simboloTri; // Sobrescribe el '*' del punto
                    }
                }
            }
        }

        // 5. Construir el String final con encabezados de columna.
        StringBuilder sb = new StringBuilder();
        sb.append("A B C D E F G H I J K L M\n"); 
        sb.append("\n"); // Primera línea vacía después del encabezado
        sb.append("\n"); // Segunda línea vacía después del encabezado

        for (int i = 0; i < numFilas; i++) {
            for (int j = 0; j < anchoDisplay; j++) {
                sb.append(displayGrid[i][j]);
            }
            sb.append("\n"); // Termina la línea de la grilla

            if (i < numFilas - 1) { // Si no es la última fila de la grilla
                sb.append("\n"); // Agregar una línea vacía entre las filas de la grilla
            }
        }
        return sb.toString();
    }
}