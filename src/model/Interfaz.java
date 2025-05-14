package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Interfaz {

    private Scanner scanner;
    private ArrayList<Jugador> jugadoresRegistrados;
    private ConfiguracionPartida configuracionActual;

    public Interfaz() {
        this.scanner = new Scanner(System.in);
        this.jugadoresRegistrados = new ArrayList<>();
        this.configuracionActual = new ConfiguracionPartida();
    }

    public void iniciarAplicacion() {
        mostrarTitulo();
        boolean salir = false;
        while (!salir) {
            mostrarMenuPrincipal();
            int opcion = leerOpcionMenu();
            switch (opcion) {
                case 1:
                    registrarNuevoJugador();
                    break;
                case 2:
                    configurarPartida();
                    break;
                case 3:
                    jugarPartida();
                    break;
                case 4:
                    mostrarRanking();
                    break;
                case 5:
                    salir = true;
                    terminarPrograma();
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
            if (!salir) {
                presioneEnterParaContinuar();
            }
        }
        scanner.close();
    }

    private void mostrarTitulo() {
        System.out.println("***************************************************");
        System.out.println("**        TRABAJO DESARROLLADO POR:            **");
        System.out.println("**  [Matías Piedra 354007], [Joaquin Piedra]   **"); 
        System.out.println("***************************************************");
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Registrar un jugador");
        System.out.println("2. Configurar la partida");
        System.out.println("3. Comienzo de partida");
        System.out.println("4. Mostrar ranking y racha");
        System.out.println("5. Terminar el programa");
        System.out.print("Seleccione una opción: ");
    }

    private int leerOpcionMenu() {
        int opcion = -1;
        try {
            opcion = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.");
        } finally {
            scanner.nextLine(); 
        }
        return opcion;
    }

    private void registrarNuevoJugador() {
        System.out.println("\n--- REGISTRO DE NUEVO JUGADOR ---");
        String username;
        int edad = -1;

        while (true) {
            System.out.print("Ingrese el username del jugador (único): ");
            username = scanner.nextLine().trim();
            if (username.isEmpty()) {
                System.out.println("El username no puede estar vacío.");
                continue;
            }
            boolean usernameExiste = false;
            for (Jugador j : jugadoresRegistrados) {
                if (j.getUsername().equalsIgnoreCase(username)) { 
                    usernameExiste = true;
                    break;
                }
            }
            if (!usernameExiste) {
                break;
            }
            System.out.println("El username '" + username + "' ya existe. Por favor, elija otro.");
        }

        while (true) {
            System.out.print("Ingrese la edad del jugador: ");
            try {
                edad = scanner.nextInt();
                scanner.nextLine(); 
                if (edad >= 0) {
                    break;
                }
                System.out.println("La edad no puede ser negativa.");
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida para la edad. Por favor, ingrese un número.");
                scanner.nextLine();
            }
        }

        try {
            Jugador nuevoJugador = new Jugador(username, edad);
            jugadoresRegistrados.add(nuevoJugador);
            System.out.println("Jugador '" + nuevoJugador.getUsername() + "' registrado exitosamente.");
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Error al crear el jugador: " + e.getMessage());
        }
    }

    private void configurarPartida() {
        System.out.println("\n--- CONFIGURACIÓN DE PARTIDA ---");
        System.out.println("Configuración actual: " + configuracionActual);
        System.out.print("¿Desea usar la configuración por defecto (S) o personalizarla (N)? [S/N]: ");
        String respuesta = scanner.nextLine().trim().toUpperCase();

        if (respuesta.equals("S")) {
            configuracionActual.resetToDefaults();
            System.out.println("Configuración restablecida a los valores por defecto.");
            System.out.println("Nueva configuración: " + configuracionActual);
            return;
        }

        if (!respuesta.equals("N")) {
            System.out.println("Opción no válida. Se mantiene la configuración actual.");
            return;
        }

        System.out.println("Configuración personalizada:");
        boolean tempRequiereContacto;
        boolean tempLargoVariable;
        int tempLargoFijo = configuracionActual.getLargoFijo(); 
        int tempCantBandasFin;
        int tempCantTableros;

        System.out.print("¿Se requiere contacto para nuevas bandas (después del 2do mov.)? (S/N) (Actual: " 
                + (configuracionActual.isRequiereContacto() ? "Sí" : "No") + "): ");
        tempRequiereContacto = scanner.nextLine().trim().equalsIgnoreCase("S");

        System.out.print("¿Largo de bandas variable (1-" + ConfiguracionPartida.MAX_LARGO_BANDA 
                + ")? (S/N) (Actual: " + (configuracionActual.isLargoBandasVariable() ? "Variable" : "Fijo " 
                + configuracionActual.getLargoFijo()) + "): ");
        tempLargoVariable = scanner.nextLine().trim().equalsIgnoreCase("S");

        if (!tempLargoVariable) {
            while (true) {
                System.out.print("Ingrese el largo fijo de las bandas (" + ConfiguracionPartida.MIN_LARGO_BANDA 
                        + "-" + ConfiguracionPartida.MAX_LARGO_BANDA + ") (Actual: " 
                        + configuracionActual.getLargoFijo() + "): ");
                try {
                    tempLargoFijo = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Ingrese un número.");
                    scanner.nextLine();
                }
            }
        }

        while (true) {
            System.out.print("Ingrese la cantidad de bandas para finalizar la partida (mínimo " 
                    + ConfiguracionPartida.MIN_BANDAS_FIN + ") (Actual: " 
                    + configuracionActual.getCantidadBandasFin() + "): ");
            try {
                tempCantBandasFin = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Ingrese un número.");
                scanner.nextLine();
            }
        }

        while (true) {
            System.out.print("Ingrese la cantidad de tableros a mostrar (" + ConfiguracionPartida.MIN_TABLEROS_MOSTRAR 
                    + "-" + ConfiguracionPartida.MAX_TABLEROS_MOSTRAR + ") (Actual: " 
                    + configuracionActual.getCantidadTablerosMostrar() + "): ");
            try {
                tempCantTableros = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Ingrese un número.");
                scanner.nextLine();
            }
        }

        try {
            ConfiguracionPartida nuevaConfig = new ConfiguracionPartida(
                tempRequiereContacto,
                tempLargoVariable,
                tempLargoFijo,
                tempCantBandasFin,
                tempCantTableros
            );
            configuracionActual = nuevaConfig;
            System.out.println("Configuración actualizada exitosamente.");
            System.out.println("Nueva configuración: " + configuracionActual);
        } catch (IllegalArgumentException e) {
            System.err.println("Error en la configuración: " + e.getMessage());
            System.out.println("No se guardaron los cambios. Se mantiene la configuración anterior: " 
                    + configuracionActual);
        }
    }

    private void jugarPartida() {
        System.out.println("\n--- COMIENZO DE PARTIDA ---");

        if (jugadoresRegistrados.size() < 2) {
            System.out.println("No hay suficientes jugadores registrados (se necesitan al menos 2).");
            return;
        }

        System.out.println("Jugadores disponibles (ordenados alfabéticamente por username):");

        ArrayList<Jugador> jugadoresOrdenados = new ArrayList<>(jugadoresRegistrados);
        Collections.sort(jugadoresOrdenados, Comparator.comparing(Jugador::getUsername, String.CASE_INSENSITIVE_ORDER));

        Jugador jugadorBlanco = jugadoresOrdenados.get(0);
        Jugador jugadorNegro = jugadoresOrdenados.get(1);
        
        System.out.println("\nJugador Blanco: " + jugadorBlanco.getUsername());
        System.out.println("Jugador Negro: " + jugadorNegro.getUsername());
        System.out.println("\nUsando configuración: " + configuracionActual);
        System.out.println("\n¡Que comience el juego!");

        Tablero tablero = new Tablero();
        
        boolean partidaTerminada = false;
        Jugador jugadorActual = jugadorBlanco;
        int movimientos = 0;

        while (!partidaTerminada) {
            System.out.println("\n--- Tablero Actual ---");
            System.out.println(tablero.toString()); // Mostrar el tablero

            System.out.println("\nTurno de: " + jugadorActual.getUsername() + (jugadorActual == jugadorBlanco ? " (Blancas)" : " (Negras)"));
            System.out.print("Ingrese su jugada (ej: D1C3 para banda, H para historial, X para abandonar): ");
            String entrada = scanner.nextLine().trim().toUpperCase();

            if (entrada.equals("X")) {
                System.out.println(jugadorActual.getUsername() + " ha abandonado la partida.");
                partidaTerminada = true;
                continue;
            } else if (entrada.equals("H")) {
                System.out.println("Historial de jugadas");
                continue; 
            }

            System.out.println("Procesando jugada '" + entrada + "...");
            if (movimientos == 0 && tablero.getPunto('D',1) != null && tablero.getPunto('C',2) != null) {
                try {
                    Banda bandaSimulada = new Banda(tablero.getPunto('D',1), tablero.getPunto('C',2), jugadorActual);
                    tablero.addBanda(bandaSimulada);
                    System.out.println("Banda simulada D1-C2 colocada.");
                } catch (Exception e) { System.out.println("Error al simular banda: " + e.getMessage());}
            } else if (movimientos == 1 && tablero.getPunto('F',1) != null && tablero.getPunto('E',2) != null) {
                 try {
                    Banda bandaSimulada2 = new Banda(tablero.getPunto('F',1), tablero.getPunto('E',2), jugadorActual);
                    tablero.addBanda(bandaSimulada2);
                    System.out.println("Banda simulada F1-E2 colocada.");
                } catch (Exception e) { System.out.println("Error al simular banda: " + e.getMessage());}
            }


            movimientos++;

            if (movimientos >= 5) { 
                System.out.println("\nSimulación de partida terminada después de " + movimientos + " movimientos.");
                partidaTerminada = true;
            }

            if (!partidaTerminada) {
                jugadorActual = (jugadorActual == jugadorBlanco) ? jugadorNegro : jugadorBlanco;
            }
        }

        System.out.println("\n--- Fin de la Partida ---");
        System.out.println(tablero.toString()); // Mostrar tablero final

        
        System.out.println("La lógica completa de juego y determinación de ganador aún no está implementada.");
    }

    private void mostrarRanking() {
        System.out.println("\n--- RANKING DE JUGADORES ---");
        if (jugadoresRegistrados.isEmpty()) {
            System.out.println("No hay jugadores registrados.");
            return;
        }

        ArrayList<Jugador> ranking = new ArrayList<>(jugadoresRegistrados);
        Collections.sort(ranking, new Comparator<Jugador>() {
            @Override
            public int compare(Jugador j1, Jugador j2) {
                int comparacionGanadas = Integer.compare(j2.getPartidasGanadas(), j1.getPartidasGanadas());
                if (comparacionGanadas != 0) {
                    return comparacionGanadas;
                }
                return j1.getUsername().compareToIgnoreCase(j2.getUsername());
            }
        });

        System.out.println("Pos. | Username         | Edad | Ganadas | Racha Act. | Mejor Racha");
        System.out.println("-----------------------------------------------------------------");
        for (int i = 0; i < ranking.size(); i++) {
            Jugador j = ranking.get(i);
            System.out.printf("%-4d | %-16s | %-4d | %-7d | %-10d | %-11d\n",
                    (i + 1),
                    j.getUsername(),
                    j.getEdad(),
                    j.getPartidasGanadas(),
                    j.getRachaActualVictorias(),
                    j.getMejorRachaVictorias());
        }
        System.out.println("-----------------------------------------------------------------");

        int rachaMasLarga = 0;
        for (Jugador j : jugadoresRegistrados) {
            if (j.getMejorRachaVictorias() > rachaMasLarga) {
                rachaMasLarga = j.getMejorRachaVictorias();
            }
        }

        if (rachaMasLarga > 0) {
            System.out.print("\nJugador(es) con la racha ganadora más larga (" + rachaMasLarga + " victorias): ");
            List<String> jugadoresConMejorRacha = new ArrayList<>();
            for (Jugador j : jugadoresRegistrados) {
                if (j.getMejorRachaVictorias() == rachaMasLarga) {
                    jugadoresConMejorRacha.add(j.getUsername());
                }
            }
            System.out.println(String.join(", ", jugadoresConMejorRacha));
        } else {
            System.out.println("\nNadie tiene una racha ganadora registrada aún.");
        }
    }

    private void terminarPrograma() {
        System.out.println("\nGracias por jugar a Triángulos. ¡Hasta pronto!");
    }

    private void presioneEnterParaContinuar() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine(); 
    }
}
