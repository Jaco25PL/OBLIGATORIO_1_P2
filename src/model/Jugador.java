package model;

public class Jugador {

//    private final String nombre;
    private final String username; 
    private final int edad;

    private int partidasGanadas;
    private int rachaActualVictorias;
    private int mejorRachaVictorias;

    public Jugador(String username, int edad) {
        if (username == null) {
            throw new NullPointerException("El nombre no puede ser nulo.");
        }
        if (username == null) {
            throw new NullPointerException("El username no puede ser nulo.");
        }
        if (username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (username.trim().isEmpty()) {
             throw new IllegalArgumentException("El username no puede estar vacío."); // Corregido
        }
        if (edad < 0) {
            throw new IllegalArgumentException("La edad no puede ser negativa.");
        }

//        this.nombre = nombre;
        this.username = username;
        this.edad = edad;

        this.partidasGanadas = 0;
        this.rachaActualVictorias = 0;
        this.mejorRachaVictorias = 0;
    }

//    public String getNombre() {
//        return nombre;
//    }

    public String getUsername() {
        return username;
    }

    public int getEdad() {
        return edad;
    }

    public int getPartidasGanadas() {
        return partidasGanadas;
    }

    public int getRachaActualVictorias() {
        return rachaActualVictorias;
    }

    public int getMejorRachaVictorias() {
        return mejorRachaVictorias;
    }

    public void incrementarPartidasGanadas() {
        this.partidasGanadas++;
        this.rachaActualVictorias++;
        if (this.rachaActualVictorias > this.mejorRachaVictorias) {
            this.mejorRachaVictorias = this.rachaActualVictorias;
        }
    }

    
    public void incrementarRachaActual() {
        this.rachaActualVictorias++;
    }

    public void actualizarRachaMaxima() {
        if (this.rachaActualVictorias > this.mejorRachaVictorias) {
            this.mejorRachaVictorias = this.rachaActualVictorias;
        }
    }

    public void resetRachaActual() { 
        this.rachaActualVictorias = 0;
    }

    @Override
    public boolean equals(Object o) {
        boolean sonIguales = false;
        if (this == o) {
            sonIguales = true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Jugador otroJugador = (Jugador) o;
            if (this.username != null) {
                 sonIguales = this.username.equals(otroJugador.username);
            } else {
                 sonIguales = (otroJugador.username == null);
            }
        }
        return sonIguales;
    }

    @Override
    public int hashCode() {
        return (username == null) ? 0 : username.hashCode();
    }

    @Override
    public String toString() {
        return username + " (Edad: " + edad
                + ", Victorias: " + partidasGanadas
                + ", Racha Actual: " + rachaActualVictorias
                + ", Mejor Racha: " + mejorRachaVictorias + ")";
    }
}
