package reporteventas;

/**
 * Clase para representar las ventas mensuales.
 */
public class Ventas {
    private int anio;
    private String mes;
    private double ingreso;
    private double egreso;

    // Constructor
    public Ventas(int anio, String mes, double ingreso, double egreso) {
        this.anio = anio;
        this.mes = mes;
        this.ingreso = ingreso;
        this.egreso = egreso;
    }

    // Getter y Setter para el año
    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    // Getter y Setter para el mes
    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    // Getter y Setter para el ingreso
    public double getIngreso() {
        return ingreso;
    }

    public void setIngreso(double ingreso) {
        this.ingreso = ingreso;
    }

    // Getter y Setter para el egreso
    public double getEgreso() {
        return egreso;
    }

    public void setEgreso(double egreso) {
        this.egreso = egreso;
    }

    // Método para calcular la utilidad
    public double getUtilidad() {
        return ingreso - egreso;
    }
}
