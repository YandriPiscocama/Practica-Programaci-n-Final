package ejercicio_cuatro_casas;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Casa {
    private double ancho;
    private double alto;
    private int numPisos;

    public Casa(double ancho, double alto, int numPisos) {
        this.ancho = ancho;
        this.alto = alto;
        this.numPisos = numPisos;
    }

    public double getAncho() {
        return ancho;
    }

    public void setAncho(double ancho) {
        this.ancho = ancho;
    }

    public double getAlto() {
        return alto;
    }

    public void setAlto(double alto) {
        this.alto = alto;
    }

    public int getNumPisos() {
        return numPisos;
    }

    public void setNumPisos(int numPisos) {
        this.numPisos = numPisos;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Casa casa = (Casa) obj;
        return Double.compare(casa.ancho, ancho) == 0 &&
               Double.compare(casa.alto, alto) == 0 &&
               numPisos == casa.numPisos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ancho, alto, numPisos);
    }

   public static List<Casa> encontrarCasasHomonimas(List<Casa> casas) {
    Map<Casa, Integer> frecuencia = new HashMap<>();
    List<Casa> homonimas = new ArrayList<>();

    for (Casa casa : casas) {
        frecuencia.put(casa, frecuencia.getOrDefault(casa, 0) + 1);
    }

    for (Map.Entry<Casa, Integer> entry : frecuencia.entrySet()) {
        if (entry.getValue() > 1) {
            homonimas.add(entry.getKey());
        }
    }

    return homonimas;
}

}
