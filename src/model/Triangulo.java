package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Triangulo {

    private final Punto punto1;
    private final Punto punto2;
    private final Punto punto3;
    private Jugador jugadorGanador; 

    public Triangulo(Punto p1, Punto p2, Punto p3) {
        if (p1 == null || p2 == null || p3 == null) {
            throw new NullPointerException("Los puntos de un triángulo no pueden ser nulos.");
        }
        this.punto1 = p1;
        this.punto2 = p2;
        this.punto3 = p3;
        this.jugadorGanador = null; // Inicialmente no ganado
    }

    public Punto getPunto1() {
        return punto1;
    }

    public Punto getPunto2() {
        return punto2;
    }

    public Punto getPunto3() {
        return punto3;
    }

    public Jugador getJugadorGanador() {
        return jugadorGanador;
    }

    public void setJugadorGanador(Jugador jugadorGanador) {
        this.jugadorGanador = jugadorGanador;
    }

    public boolean contienePunto(Punto p) {
        if (p == null) {
            return false;
        }
        return p.equals(punto1) || p.equals(punto2) || p.equals(punto3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triangulo otro = (Triangulo) o;

        Set<Punto> misPuntos = new HashSet<>(Arrays.asList(this.punto1, this.punto2, this.punto3));
        Set<Punto> otrosPuntos = new HashSet<>(Arrays.asList(otro.punto1, otro.punto2, otro.punto3));

        return misPuntos.equals(otrosPuntos);
    }

    @Override
    public int hashCode() {
        Set<Punto> misPuntos = new HashSet<>(Arrays.asList(this.punto1, this.punto2, this.punto3));
        return misPuntos.hashCode();
    }

    @Override
    public String toString() {
        String ganadorStr;
        if (jugadorGanador != null) {
            ganadorStr = "ganado por [" + jugadorGanador.getUsername() + "]";
        } else {
            ganadorStr = "(No ganado)";
        }
        return "Triángulo en [" + punto1.toString() +
               ", " + punto2.toString() +
               ", " + punto3.toString() +
               "] " + ganadorStr;
    }
}
