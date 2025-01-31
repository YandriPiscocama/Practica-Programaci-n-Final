package matriz;

import java.io.*;
import java.util.Random;
import javax.swing.JOptionPane;

public class Matriz {
    private int filas;
    private int columnas;
    private int[][] matrizOriginal;
    private int[][] matrizResultado;
    private static final String ARCHIVO = "matriz.txt";

    public Matriz(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.matrizOriginal = new int[filas][columnas];
        this.matrizResultado = new int[filas][columnas];
        generarMatriz();
        copiarMatrizOriginal();
        guardarMatriz();
    }

    private void generarMatriz() {
        Random rand = new Random();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matrizResultado[i][j] = matrizOriginal[i][j] = rand.nextInt(90) + 10;
            }
        }
    }

    private void copiarMatrizOriginal() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matrizResultado[i][j] = matrizOriginal[i][j];
            }
        }
    }

    public void guardarMatriz() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO))) {
            // Guardar la matriz original
            writer.write("Matriz Original: \n");
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    writer.write(matrizOriginal[i][j] + " ");
                }
                writer.newLine();
            }

            // Guardar la matriz resultante
            writer.write("Matriz Resultante: \n");
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    writer.write(matrizResultado[i][j] + " ");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar la matriz en el archivo.");
        }
    }

    public static Matriz cargarMatriz() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO))) {
            String[] dimensiones = reader.readLine().split(" ");
            int filas = Integer.parseInt(dimensiones[0]);
            int columnas = Integer.parseInt(dimensiones[1]);
            Matriz matriz = new Matriz(filas, columnas);

            // Leer la matriz original
            reader.readLine();  // Saltar "Matriz Original:"
            for (int i = 0; i < filas; i++) {
                String[] valores = reader.readLine().split(" ");
                for (int j = 0; j < columnas; j++) {
                    matriz.matrizOriginal[i][j] = Integer.parseInt(valores[j]);
                }
            }

            // Leer la matriz resultante
            reader.readLine();  // Saltar "Matriz Resultante:"
            for (int i = 0; i < filas; i++) {
                String[] valores = reader.readLine().split(" ");
                for (int j = 0; j < columnas; j++) {
                    matriz.matrizResultado[i][j] = Integer.parseInt(valores[j]);
                }
            }
            return matriz;
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "No se encontró un archivo válido. Se generará una nueva matriz.");
            return null;
        }
    }

    public String mostrarMatrizOriginal() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                sb.append(matrizOriginal[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String mostrarMatrizResultado() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                sb.append(matrizResultado[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void eliminarMultiplos(int num) {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (matrizResultado[i][j] % num == 0) {
                    matrizResultado[i][j] = 0;
                }
            }
        }
        guardarMatriz();
    }

    public void eliminarPrimos() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (esPrimo(matrizResultado[i][j])) {
                    matrizResultado[i][j] = 0;
                }
            }
        }
        guardarMatriz();
    }

    private boolean esPrimo(int num) {
        if (num < 2) return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }
        return true;
    }
}
