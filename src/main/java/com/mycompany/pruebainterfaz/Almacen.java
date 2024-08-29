package com.mycompany.pruebainterfaz;

import com.mycompany.pruebainterfaz.panel.Pedido;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class Almacen {
    private Queue<Pedido> colaPedidos;
    private int filas; // Número de filas en el almacén
    private int columnas; // Número de columnas en el almacén

    public Almacen(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        colaPedidos = new LinkedList<>();
    }

    // Método para agregar un pedido a la cola
    public void agregarPedido(Pedido pedido) {
        colaPedidos.add(pedido);
    }

    // Método para procesar los pedidos con el algoritmo S-Shape y escribir en el archivo
    public void procesarPedidos() {
        try (FileWriter fileWriter = new FileWriter("resultados.txt");
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            int numeroPedido = 1; // Contador para los pedidos

            while (!colaPedidos.isEmpty()) {
                Pedido pedido = colaPedidos.poll();
                long startTime = System.currentTimeMillis();

                printWriter.println("Pedido " + numeroPedido + ":");
                printWriter.println("Ítems del pedido:");
                
                List<Posicion> posicionesRecolectadas = new ArrayList<>();
                List<Posicion> recorrido = new ArrayList<>();

                for (Integer item : pedido.getItems()) {
                    Posicion posicionItem = calcularPosicion(item);
                    posicionesRecolectadas.add(posicionItem);
                    printWriter.println("Item " + item + ": Fila " + posicionItem.fila + ", Columna " + posicionItem.columna);
                }

                // Aplicar el algoritmo S-Shape para recorrer el almacén
                recorrido = recorrerAlmacen(posicionesRecolectadas);

                long endTime = System.currentTimeMillis();
                long tiempoProceso = endTime - startTime;

                printWriter.println("Tiempo de procesamiento del pedido: " + tiempoProceso + " ms");
                printWriter.println("Posiciones recolectadas:");
                for (Posicion pos : posicionesRecolectadas) {
                    printWriter.println("Item en Fila: " + pos.fila + ", Columna: " + pos.columna);
                }
                printWriter.println(); // Espacio entre pedidos

                numeroPedido++; // Incrementar el número del pedido
            }

        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    // Método para calcular la posición de un ítem en el almacén
    private Posicion calcularPosicion(int item) {
        int fila = (item - 1) / columnas; // Calcular la fila del ítem
        int columna = (item - 1) % columnas; // Calcular la columna del ítem
        return new Posicion(fila, columna);
    }

    // Método para recorrer el almacén utilizando el algoritmo S-Shape
    private List<Posicion> recorrerAlmacen(List<Posicion> posicionesRecolectadas) {
        List<Posicion> recorrido = new ArrayList<>();
        boolean haciaDerecha = true;

        for (int fila = 0; fila < filas; fila++) {
            if (haciaDerecha) {
                for (int columna = 0; columna < columnas; columna++) {
                    recorrido.add(new Posicion(fila, columna));
                    verificarRecoleccion(posicionesRecolectadas, fila, columna);
                }
            } else {
                for (int columna = columnas - 1; columna >= 0; columna--) {
                    recorrido.add(new Posicion(fila, columna));
                    verificarRecoleccion(posicionesRecolectadas, fila, columna);
                }
            }
            haciaDerecha = !haciaDerecha; // Cambiar dirección en la siguiente fila
        }

        return recorrido;
    }

    // Método para verificar si un ítem debe ser recolectado en una posición
    private void verificarRecoleccion(List<Posicion> posicionesRecolectadas, int fila, int columna) {
        for (Posicion pos : posicionesRecolectadas) {
            if (pos.fila == fila && pos.columna == columna) {
                // Imprimir en consola, o puedes manejarlo de otra forma si lo prefieres
                System.out.println("Item recolectado en Fila: " + fila + ", Columna: " + columna);
            }
        }
    }

    // Clase interna para representar una posición en el almacén
    private class Posicion {
        int fila, columna;

        public Posicion(int fila, int columna) {
            this.fila = fila;
            this.columna = columna;
        }
    }

    public boolean tienePedidos() {
        return !colaPedidos.isEmpty();
    }
}
