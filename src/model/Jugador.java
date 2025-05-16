/*
 * Autores: [Matías Piedra 354007], [Joaquin Piedra 304804] 
 */

package model;

public class Jugador {

    private final String nombre; 
    private final int edad;

    private int partidasGanadas;
    private int rachaActualVictorias;
    private int mejorRachaVictorias;

    // crea un nuevo jugador.
    public Jugador(String nombre, int edad) {
        if (nombre == null) {
            throw new NullPointerException("El nombre no puede ser nulo.");
        }
        if (nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (edad < 0) {
            throw new IllegalArgumentException("La edad no puede ser negativa.");
        }

        this.nombre = nombre;
        this.edad = edad;

        this.partidasGanadas = 0;
        this.rachaActualVictorias = 0;
        this.mejorRachaVictorias = 0;
    }

    // obtiene el nombre.
    public String getNombre() {
        return nombre;
    }

    // obtiene la edad.
    public int getEdad() {
        return edad;
    }

    // obtiene partidas ganadas.
    public int getPartidasGanadas() {
        return partidasGanadas;
    }

    // obtiene racha actual victorias.
    public int getRachaActualVictorias() {
        return rachaActualVictorias;
    }

    // obtiene mejor racha victorias.
    public int getMejorRachaVictorias() {
        return mejorRachaVictorias;
    }

    // incrementa ganadas y actualiza rachas.
    public void incrementarPartidasGanadas() {
        this.partidasGanadas++;
        this.rachaActualVictorias++;
        if (this.rachaActualVictorias > this.mejorRachaVictorias) {
            this.mejorRachaVictorias = this.rachaActualVictorias;
        }
    }

    // incrementa racha actual de victorias.
    public void incrementarRachaActual() {
        this.rachaActualVictorias++;
    }

    // actualiza mejor racha de victorias.
    public void actualizarRachaMaxima() {
        if (this.rachaActualVictorias > this.mejorRachaVictorias) {
            this.mejorRachaVictorias = this.rachaActualVictorias;
        }
    }

    // reinicia racha actual de victorias.
    public void resetRachaActual() { 
        this.rachaActualVictorias = 0;
    }

    // compara este jugador con otro.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        return nombre.equals(jugador.nombre); // Asumimos que nombre no es nulo por constructor
    }

    // genera código hash para jugador.
    @Override
    public int hashCode() {
        return nombre.hashCode(); // nombre no puede ser nulo debido a la validación del constructor
    }

    // devuelve datos textuales del jugador.
    @Override
    public String toString() {
        return nombre + " (Edad: " + edad
                + ", Victorias: " + partidasGanadas
                + ", Racha Actual: " + rachaActualVictorias
                + ", Mejor Racha: " + mejorRachaVictorias + ")";
    }
}