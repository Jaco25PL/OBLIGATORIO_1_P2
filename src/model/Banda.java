package model;

public class Banda {

    private final Punto puntoA; 
    private final Punto puntoB; 
    private final Jugador jugador; 

    public Banda(Punto pA, Punto pB, Jugador jugador) {
        if (pA == null) {
            throw new NullPointerException("El primer punto (pA) no puede ser nulo.");
        }
        if (pB == null) {
            throw new NullPointerException("El segundo punto (pB) no puede ser nulo.");
        }
        if (jugador == null) {
            throw new NullPointerException("El jugador no puede ser nulo.");
        }
        this.jugador = jugador; // Asignar jugador después de validar que no es nulo

        if (pA.equals(pB)) {
            throw new IllegalArgumentException("Los puntos de una banda no pueden ser iguales: " + pA);
        }

        if (!sonAdyacentes(pA, pB)) {
            throw new IllegalArgumentException("Los puntos " + pA + " y " + pB + " no son adyacentes.");
        }

        if (pA.getFila() < pB.getFila() || (pA.getFila() == pB.getFila() && pA.getColumna() < pB.getColumna())) {
            this.puntoA = pA;
            this.puntoB = pB;
        } else {
            this.puntoA = pB; // pB es "menor"
            this.puntoB = pA;
        }
    }

    private boolean sonAdyacentes(Punto p1, Punto p2) { 
        int diffFilas = Math.abs(p1.getFila() - p2.getFila()); 
        int diffCols = Math.abs(p1.getColumna() - p2.getColumna());

        boolean mismaFilaAdy = (diffFilas == 0 && diffCols == 2); 

        boolean filaAdyacenteAdy = (diffFilas == 1 && diffCols == 1); 

        return mismaFilaAdy || filaAdyacenteAdy;
    }

    public Punto getPuntoA() {
        return puntoA;
    }

    public Punto getPuntoB() {
        return puntoB;
    }

    public Jugador getJugador() {
        return jugador;
    }

    @Override
    public boolean equals(Object o) {
        boolean sonIguales = false;
        if (this == o) {
            sonIguales = true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Banda otraBanda = (Banda) o;
            sonIguales = this.puntoA.equals(otraBanda.puntoA) &&
                         this.puntoB.equals(otraBanda.puntoB);
        }
        return sonIguales;
    }

    @Override
    public int hashCode() {
        int result = puntoA.hashCode();
        result = 31 * result + puntoB.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Banda [A: " + puntoA + ", B: " + puntoB + ", Jugador: " + jugador.getUsername() + "]";
    }
    
}
