
package model;

public class Punto {

    private final int fila;
    private final char columna;

    public Punto(int fila, char columna) {
        char colUpper = Character.toUpperCase(columna);
        boolean valido = false;

        if (fila == 1 || fila == 7) { // Filas 1 y 7: D, F, H, J
            valido = (colUpper == 'D' || colUpper == 'F' || colUpper == 'H' || colUpper == 'J');
        } else if (fila == 2 || fila == 6) { // Filas 2 y 6: C, E, G, I, K
            valido = (colUpper == 'C' || colUpper == 'E' || colUpper == 'G' || colUpper == 'I' || colUpper == 'K');
        } else if (fila == 3 || fila == 5) { // Filas 3 y 5: B, D, F, H, J, L
            valido = (colUpper == 'B' || colUpper == 'D' || colUpper == 'F' || colUpper == 'H' || colUpper == 'J' || colUpper == 'L');
        } else if (fila == 4) { // Fila 4: A, C, E, G, I, K, M
            valido = (colUpper == 'A' || colUpper == 'C' || colUpper == 'E' || colUpper == 'G' || colUpper == 'I' || colUpper == 'K' || colUpper == 'M');
        }

        if (!valido) {
            throw new IllegalArgumentException("El punto " + colUpper + fila + " no es válido o no existe en el tablero.");
        }

        this.fila = fila;
        this.columna = colUpper; // Guardar siempre en mayúscula
    }

    public int getFila() {
        return fila;
    }

    public char getColumna() {
        return columna;
    }

    @Override 
    public String toString() {
        return "" + columna + fila; // Devuelve la representación en forma de string del punto (ej: "A1", "B2", etc.)
    }

    @Override
    public boolean equals(Object o) {
        boolean sonIguales = false; 

        if (this == o) {
            sonIguales = true; // Si son la misma instancia, son iguales
        }
        else if (o != null && getClass() == o.getClass()) {
            Punto otroPunto = (Punto) o;
            if (this.fila == otroPunto.fila && this.columna == otroPunto.columna) {
                sonIguales = true; // Si coinciden, son iguales
            }
        }
        return sonIguales;
    }

    @Override
    public int hashCode() { 
        int result = fila; 
        result = 31 * result + (int) columna; 
        return result; 
    }
}