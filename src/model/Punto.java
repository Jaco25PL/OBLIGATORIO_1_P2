/*
 * Autores: [Matías Piedra 354007], [Joaquin Piedra 304804] 
 */

package model;

public class Punto {

    private final int fila;
    private final char columna;

    // crea un nuevo punto.
    public Punto(int fila, char columna) {
        char colUpper = Character.toUpperCase(columna);
        boolean valido = false;

        // Validaciones según la forma del tablero
        if (fila == 1 || fila == 7) { 
            valido = (colUpper == 'D' || colUpper == 'F' || colUpper == 'H' || colUpper == 'J');
        } else if (fila == 2 || fila == 6) { 
            valido = (colUpper == 'C' || colUpper == 'E' || colUpper == 'G' || colUpper == 'I' || colUpper == 'K');
        } else if (fila == 3 || fila == 5) { 
            valido = (colUpper == 'B' || colUpper == 'D' || colUpper == 'F' || colUpper == 'H' || colUpper == 'J' || colUpper == 'L');
        } else if (fila == 4) { 
            valido = (colUpper == 'A' || colUpper == 'C' || colUpper == 'E' || colUpper == 'G' || colUpper == 'I' || colUpper == 'K' || colUpper == 'M');
        }

        if (!valido) {
            throw new IllegalArgumentException("El punto " + colUpper + fila + " no es válido o no existe en el tablero.");
        }

        this.fila = fila;
        this.columna = colUpper; 
    }

    // obtiene la fila.
    public int getFila() {
        return fila;
    }

    // obtiene la columna.
    public char getColumna() {
        return columna;
    }

    // devuelve representación textual.
    @Override 
    public String toString() {
        return "" + columna + fila; 
    }

    // compara este punto con otro.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Punto otroPunto = (Punto) o;
        return fila == otroPunto.fila && columna == otroPunto.columna;
    }

    // genera código hash.
    @Override
    public int hashCode() { 
        int result = fila; 
        result = 31 * result + (int) columna; 
        return result; 
    }
}