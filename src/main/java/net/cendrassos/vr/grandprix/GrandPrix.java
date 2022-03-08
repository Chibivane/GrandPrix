package net.cendrassos.vr.grandprix;

import java.util.*;

public class GrandPrix {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //PRESENTACIÓN E INTRODUCCIÓN DE DATOS
        System.out.println("¡¡¡Bienvenidos al Grand Prix!!!");
        int numCasillas = 0;

        //NUMERO DE CASILLAS
        while (numCasillas == 0) {
            System.out.println("¿Cuantas casillas quieres que tenga tu tablero? (Entre 10 y 30)");
            numCasillas = scanner.nextInt();
            if (numCasillas > 30 || numCasillas < 10) {
                numCasillas = 0;
            }
        }

        //NUMERO DE VUELTAS
        int numVueltas = 0;
        while (numVueltas == 0) {
            System.out.println("¿Cuantas vueltas quieres que tenga tu Grand Prix? (Entre 1 y 5)");
            numVueltas = scanner.nextInt();
            if (numVueltas > 5 || numVueltas < 1) {
                numVueltas = 0;
            }
        }

        //NUMERO DE PARTICIPANTES
        int jugadores = 0;
        while (jugadores == 0) {
            System.out.println("Finalmente indica el número de motoristas que van a participar. (Entre 2 y 8)");
            jugadores = scanner.nextInt();
            if (jugadores > 8 || jugadores < 2) {
                jugadores = 0;
            }
        }
        scanner.close();

        //CREAMOS LISTAS PARA LAS FICHAS DE LOS JUGADORES PARTICIPANTES Y LOS COLORES PARA EL TEXTO
        String[] fichas = new String[]{"yellow", "green", "blue", "red", "white", "grey", "purple", "cyan"};
        String[] colores = new String[]{"\033[0;33m", "\033[0;32m", "\033[0;34m", "\033[0;31m", "\033[0m", "\033[0;37m", "\033[0;35m", "\033[0;36m"};

        //CREAMOS LAS FICHAS NECESARIAS
        Motorista[] lista = new Motorista[jugadores];
        for (int i = 0; i < jugadores; i++) {
            lista[i] = new Motorista(fichas[i], colores[i]);
        }

        //CREAMOS LA LISTA DE CASILLAS QUE TIENEN ARENA EN ESTA PARTIDA Y UNA LISTA PARA LOS MOTORISTAS QUE CAIGAN EN ELLA
        List<Integer> partida = tablero(numCasillas);
        List<Motorista> arena = new ArrayList<>();

        //CREAMOS UNA LISTA PARA LOS MOTORISTAS QUE VAYAN CRUZANDO LA META
        List<Motorista> cruzarMeta = new ArrayList<>();

        //CREAMOS UN BUCLE QUE REPITA TURNOS HASTA QUE ALGUIEN CRUCE LA META
        boolean haGanadoAlguien = false;
        while (!haGanadoAlguien) {

            //CREAMOS UN BUCLE QUE REPITA TURNOS ENTEROS PARA QUE JUEGUE CADA PARTICIPANTE
            for (Motorista motorista : lista) {

                //COMPROBAMOS SI EL JUGADOR ACTUAL NO ESTÁ EN LA ARENA
                if (!arena.contains(motorista)) {

                    //TIRADA 1D6 Y MOVEMOS LA FICHA
                    int tirada = (int) (Math.random() * 5 + 1);
                    motorista.setPosicion(motorista.getPosicion() + tirada, numCasillas);

                    //COMPROBAMOS SI CAE EN ARENA O NO Y MOSTRAMOS EL RESULTADO
                    if (partida.contains(motorista.getPosicion())) {
                        arena.add(motorista);
                        System.out.println(motorista.getColor() + "La moto " + motorista.getNombreMotorista() + " se situa en la casilla número " + motorista.getPosicion() + " que hay arena, de su vuelta número " + (motorista.getvuelta()) + ", pierde un turno.");
                    } else {
                        System.out.println(motorista.getColor() + "La moto " + motorista.getNombreMotorista() + " se situa en la casilla número " + motorista.getPosicion() + " de su vuelta número " + (motorista.getvuelta()) + ".");
                    }

                    //COMPROBAMOS SI EL JUGADOR ACTUAL HA CRUZADO LA META
                    if (motorista.getvuelta() >= numVueltas) {
                        haGanadoAlguien = true;
                        cruzarMeta.add(motorista);
                    }
                } else { //EN CASO QUE EL JUGADOR ESTÉ EN LA ARENA
                    System.out.println(motorista.getColor()+"La moto "+ motorista.getNombreMotorista() + " pierde el turno saliendo de la arena.");
                    arena.remove(motorista);
                }
            }
        }
        //FILTRAMOS LA POSICIÓN DE LOS JUGADORES PARA MOSTRAR EL RESULTADO FINAL
        if (cruzarMeta.size() > 1) {  //SI HAY VARIAS PERSONAS TRAS LA META COMPROBAMOS SI HAY UN SOLO GANADOR O EMPATE
            List<Motorista> ganador = new ArrayList<>();
            ganador.add(cruzarMeta.get(0));
            for (int i = 1; i < cruzarMeta.size(); i++) {
                if (cruzarMeta.get(i).getPosicion() > ganador.get(0).getPosicion()) {
                    if (cruzarMeta.get(i).getPosicion() != (ganador.get(0).getPosicion())) {
                        ganador.clear();
                    }
                    ganador.add(cruzarMeta.get(i));
                }
            }
            System.out.println("\033[0m¡¡Ha finalizado el Grand Prix!!");
            if (ganador.size()>1){
                System.out.println("¡¡Ha habido empate!! Los cruzarMeta son:");
                for (Motorista motorista : ganador) {
                    System.out.println("- " + motorista.getNombreMotorista());
                }
            } else {
                System.out.println("¡¡La moto ganadora ha sido " + ganador.get(0).getNombreMotorista() + "!!");
            }
        } else { //SI SOLO UNA PERSONA HA CRUZADO LA META
            System.out.println("\033[0m¡¡Ha finalizado el Grand Prix!!");
            if (cruzarMeta.size() == 1) {
                System.out.println("¡¡La moto ganadora ha sido " + cruzarMeta.get(0).getNombreMotorista() + "!!");
            }
        }
    }


    //OBJETO MOTORISTA CONTENDRÁ EL NOMBRE QUE CORRESPONDE A UN COLOR, LA POSICIÓN QUE OCUPA, EN QUE VUELTA ESTÁ, Y UN COLOR PARA MOSTRAR TEXTO
    //CON SUS GETTERS Y SETTERS Y CONSTRUCTOR
    public static class Motorista {

        private final String nombreMotorista;
        private int posicion;
        private int vuelta;
        private final String color;

        public Motorista(String nombreMotorista, String color) {
            this.nombreMotorista = nombreMotorista;
            this.posicion = 0;
            this.vuelta = 0;
            this.color = color;
        }

        public String getNombreMotorista() {
            return nombreMotorista;
        }

        public String getColor(){
            return color;
        }

        public int getPosicion() {
            return posicion;
        }

        public void setPosicion(int nuevaPosicion, int numCasillas) {
            if (nuevaPosicion > numCasillas) {
                setVuelta(getvuelta() + 1);
                posicion = nuevaPosicion - numCasillas;
            } else {
                posicion = nuevaPosicion;
            }
        }

        public int getvuelta() {
            return vuelta;
        }

        public void setVuelta(int nuevaVuelta) {
            vuelta = nuevaVuelta;
        }

    }

    //MÉTODO PARA DEFINIR QUÉ CASILLAS TIENEN ARENA
    public static List<Integer> tablero(int numCasillas) {

        List<Integer> partida = new ArrayList<>();
        for (int i = 0; i < numCasillas; i++) {
            if (Math.random() < 0.10) { //DEFINIMOS UN 10% DE PROBABILIDAD DE QUE UNA CASILLA SEA ARENA
                partida.add(i);
            }
        }

        return partida;
    }

}
