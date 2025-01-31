/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package reporteventas;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author yandri
 */
public class Reporte extends javax.swing.JFrame {

    // Arreglo de meses
    private final String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    private int mesActual = 0; // Índice para el mes actual
    private int anioActual = 0; // Año ingresado por el usuario

    // Lista para almacenar las ventas
    private final List<Ventas> ventasList = new ArrayList<>();

    public Reporte() {
        initComponents();
        actualizarMeses(); // Actualizar los labels al iniciar
        actualizarTabla(); // Inicializar la tabla vacía
    }

    // Método para actualizar los labels de los meses
    private void actualizarMeses() {
        txtMostrarMesIngreso.setText("(" + meses[mesActual] + ")");
        txtMostrarMesEgreso.setText("(" + meses[mesActual] + ")");
    }//Para que sirve el set text ne

    private void avanzarMes() {
        mesActual++;
        if (mesActual >= meses.length) {
            mesActual = 0; // Reiniciar a enero
        }
        actualizarMeses();
    }

    private void guardarAnio() {
        try {
            anioActual = Integer.parseInt(txtAnio.getText());
        } catch (NumberFormatException e) {
            System.err.println("Error: Año no válido");
        }
    }

    private void actualizarTabla() {
        DefaultTableModel model = (DefaultTableModel) tableReporte.getModel();
        //Primero obtengo el modelo de  la tabla (obtiene
        model.setRowCount(0);
         //tualizar los datos de la tabla desde cero
        //Para que sriven setRowCount
        model.setColumnIdentifiers(new Object[]{"Año", "Mes", "Ingresos", "Egresos", "Utilidad"});
//establecer el nombre de las columnas.
        for (Ventas venta : ventasList) {
            model.addRow(new Object[]{
                //contiene vaIores de Ia fiIa que se agrega a Ia tabIa
                venta.getAnio(),
                venta.getMes(),
                String.format("%.2f", venta.getIngreso()),
                String.format("%.2f", venta.getEgreso()),
                String.format("%.2f", venta.getUtilidad())
            });
        }
    }

    private void actualizarEstadisticas() {
        // Total ventas anuales
        double totalVentas = calcularTotalVentas();
        txtVentasAnual.setText(String.format("%.2f", totalVentas));

        // Promedio de ventas
        double promedioVentas = calcularPromedioVentas();
        txtPromedioVentasAnual.setText(String.format("%.2f", promedioVentas));

        // Mes con mayores ingresos
        Ventas maxVentas = obtenerMesMayoresVentas();
        txtMesMasIngresos.setText(maxVentas.getMes());

        // Mes con menores ingresos
        Ventas minVentas = obtenerMesMenoresVentas();
        txtMesMenosIngresos.setText(minVentas.getMes());

        // Mes con mayores egresos
        Ventas maxEgresos = obtenerMesMayoresEgresos();
        txtMesMasEgresos.setText(maxEgresos.getMes());

        // Mes con menores egresos
        Ventas minEgresos = obtenerMesMenoresEgresos();
        txtMesMenosEgresos.setText(minEgresos.getMes());
    }

    private void guardarEstadisticasEnArchivo() {
        try {
            // Crear el archivo en la raíz del proyecto con el nombre correspondiente al año
            File archivo = new File("reporte_" + anioActual + ".txt");

            // Si el archivo no existe, se crea
            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            // Variables para cálculos
            double totalVentas = 0;
            double totalGastos = 0;
            double maxVenta = Double.MIN_VALUE;
            double minVenta = Double.MAX_VALUE;
            double maxGasto = Double.MIN_VALUE;
            double minGasto = Double.MAX_VALUE;
            String mesMaxVenta = "";
            String mesMinVenta = "";
            String mesMaxGasto = "";
            String mesMinGasto = "";
            int totalMeses = 0;

            // Calcular estadísticas
            for (Ventas venta : ventasList) {
                if (venta.getAnio() == anioActual) {
                    totalVentas += venta.getIngreso();
                    totalGastos += venta.getEgreso();
                    totalMeses++;

                    if (venta.getIngreso() > maxVenta) {
                        maxVenta = venta.getIngreso();
                        mesMaxVenta = venta.getMes();
                    }
                    if (venta.getIngreso() < minVenta) {
                        minVenta = venta.getIngreso();
                        mesMinVenta = venta.getMes();
                    }
                    if (venta.getEgreso() > maxGasto) {
                        maxGasto = venta.getEgreso();
                        mesMaxGasto = venta.getMes();
                    }
                    if (venta.getEgreso() < minGasto) {
                        minGasto = venta.getEgreso();
                        mesMinGasto = venta.getMes();
                    }
                }
            }

            double promedioVentas = (totalMeses > 0) ? totalVentas / totalMeses : 0;

            // Escribir en el archivo
            try (PrintWriter writer = new PrintWriter(new FileWriter(archivo, false))) {
                writer.println("\n--- Reporte Anual: " + anioActual + " ---");
                writer.println("Ventas y gastos mensuales:");
                for (Ventas venta : ventasList) {
                    if (venta.getAnio() == anioActual) {
                        writer.println("Mes: " + venta.getMes() + " | Ventas: " + venta.getIngreso() + " | Gastos: " + venta.getEgreso());
                    }
                }
                writer.println("----------------------------");
                writer.println("Total de ventas en el año: " + totalVentas);
                writer.println("Total de gastos en el año: " + totalGastos);
                writer.println("Promedio de ventas anual: " + promedioVentas);
                writer.println("Mes con mayor venta: " + mesMaxVenta + " (" + maxVenta + ")");
                writer.println("Mes con menor venta: " + mesMinVenta + " (" + minVenta + ")");
                writer.println("Mes con mayor gasto: " + mesMaxGasto + " (" + maxGasto + ")");
                writer.println("Mes con menor gasto: " + mesMinGasto + " (" + minGasto + ")");
                writer.println("----------------------------");
            }

            JOptionPane.showMessageDialog(this, "Reporte guardado exitosamente en: " + archivo.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

// Métodos auxiliares para cálculos estadísticos
    private double calcularTotalVentas() {
        return ventasList.stream()
                .mapToDouble(Ventas::getIngreso)
                .sum();
    }

    private double calcularPromedioVentas() {
        return ventasList.stream()
                .mapToDouble(Ventas::getIngreso)
                .average()
                .orElse(0.0);
    }

    private Ventas obtenerMesMayoresVentas() {
        return ventasList.stream()
                .max((v1, v2) -> Double.compare(v1.getIngreso(), v2.getIngreso()))
                .orElse(new Ventas(anioActual, "", 0, 0));
    }

    private Ventas obtenerMesMenoresVentas() {
        return ventasList.stream()
                .min((v1, v2) -> Double.compare(v1.getIngreso(), v2.getIngreso()))
                .orElse(new Ventas(anioActual, "", 0, 0));
    }

    private Ventas obtenerMesMayoresEgresos() {
        return ventasList.stream()
                .max((v1, v2) -> Double.compare(v1.getEgreso(), v2.getEgreso()))
                .orElse(new Ventas(anioActual, "", 0, 0));
    }

    private Ventas obtenerMesMenoresEgresos() {
        return ventasList.stream()
                .min((v1, v2) -> Double.compare(v1.getEgreso(), v2.getEgreso()))
                .orElse(new Ventas(anioActual, "", 0, 0));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtMostrarMesIngreso = new javax.swing.JLabel();
        txtIngresoMes = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnGuardarIngresosEgresosMes = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtMostrarMesEgreso = new javax.swing.JLabel();
        txtEgresoMes = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtAnio = new javax.swing.JTextField();
        btnGuardarAnio = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableReporte = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        txtVentasAnual = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPromedioVentasAnual = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtMesMasIngresos = new javax.swing.JTextField();
        txtMesMenosIngresos = new javax.swing.JTextField();
        txtMesMasEgresos = new javax.swing.JTextField();
        txtMesMenosEgresos = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("REPORTE DE VENTAS");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, -1, -1));

        txtMostrarMesIngreso.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtMostrarMesIngreso.setText("()");
        jPanel1.add(txtMostrarMesIngreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, -1, -1));

        txtIngresoMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIngresoMesActionPerformed(evt);
            }
        });
        jPanel1.add(txtIngresoMes, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 140, 230, 30));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Ingreso del mes");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        btnGuardarIngresosEgresosMes.setBackground(new java.awt.Color(204, 0, 204));
        btnGuardarIngresosEgresosMes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnGuardarIngresosEgresosMes.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardarIngresosEgresosMes.setText("Guardar datos");
        btnGuardarIngresosEgresosMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarIngresosEgresosMesActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuardarIngresosEgresosMes, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 240, 140, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Egreso del mes");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        txtMostrarMesEgreso.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtMostrarMesEgreso.setText("()");
        jPanel1.add(txtMostrarMesEgreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, -1, -1));

        txtEgresoMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEgresoMesActionPerformed(evt);
            }
        });
        jPanel1.add(txtEgresoMes, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 200, 230, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Año del informe:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        txtAnio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAnioActionPerformed(evt);
            }
        });
        jPanel1.add(txtAnio, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 230, 30));

        btnGuardarAnio.setBackground(new java.awt.Color(0, 153, 0));
        btnGuardarAnio.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnGuardarAnio.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardarAnio.setText("Guardar año");
        btnGuardarAnio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarAnioActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuardarAnio, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, 120, 30));

        tableReporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tableReporte);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 520, 200));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Total ventas anual:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, -1, -1));
        jPanel1.add(txtVentasAnual, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 500, 110, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Promedio de ventas anual:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, -1, -1));
        jPanel1.add(txtPromedioVentasAnual, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 540, 160, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Mes con menos ingresos:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 600, -1, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Mes con más ingresos:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, -1, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Mes con más egresos:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 570, -1, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Mes con menos egresos:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 600, -1, -1));
        jPanel1.add(txtMesMasIngresos, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 570, 110, -1));
        jPanel1.add(txtMesMenosIngresos, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 600, 90, -1));
        jPanel1.add(txtMesMasEgresos, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 570, 120, -1));
        jPanel1.add(txtMesMenosEgresos, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 600, 110, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarAnioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarAnioActionPerformed
        guardarAnio();
    }//GEN-LAST:event_btnGuardarAnioActionPerformed

    private void btnGuardarIngresosEgresosMesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarIngresosEgresosMesActionPerformed
        try {
            double ingreso = txtIngresoMes.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtIngresoMes.getText().trim());
            double egreso = txtEgresoMes.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtEgresoMes.getText().trim());

            if (mesActual >= meses.length) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Ya se han registrado los 12 meses.",
                        "Información",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            boolean actualizado = false;
            for (Ventas venta : ventasList) {
                if (venta.getAnio() == anioActual && venta.getMes().equals(meses[mesActual])) {
                    venta.setIngreso(ingreso);
                    venta.setEgreso(egreso);
                    actualizado = true;
                    break;
                }
            }

            if (!actualizado) {
                Ventas nuevaVenta = new Ventas(anioActual, meses[mesActual], ingreso, egreso);
                ventasList.add(nuevaVenta);
            }

            // Limpiar los campos de ingreso y egreso
            txtIngresoMes.setText("");
            txtEgresoMes.setText("");

            // Avanzar el mes y actualizar estadísticas
            avanzarMes();
            actualizarTabla();
            actualizarEstadisticas();

            // Mensaje de depuración
            System.out.println("Mes actual después de avanzar: " + mesActual);
            System.out.println("Registros en ventasList: " + ventasList.size());

            // Guardar estadísticas si se han ingresado los 12 meses
            if (ventasList.size() == meses.length) {
                System.out.println("Llamando a guardarEstadisticasEnArchivo()...");
                guardarEstadisticasEnArchivo();

                javax.swing.JOptionPane.showMessageDialog(this,
                        "Se han guardado las estadísticas anuales en el archivo correctamente.",
                        "Éxito",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Por favor, ingresa valores numéricos válidos.",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarIngresosEgresosMesActionPerformed

    private void txtAnioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAnioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAnioActionPerformed

    private void txtIngresoMesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIngresoMesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIngresoMesActionPerformed

    private void txtEgresoMesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEgresoMesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEgresoMesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Reporte().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardarAnio;
    private javax.swing.JButton btnGuardarIngresosEgresosMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableReporte;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtEgresoMes;
    private javax.swing.JTextField txtIngresoMes;
    private javax.swing.JTextField txtMesMasEgresos;
    private javax.swing.JTextField txtMesMasIngresos;
    private javax.swing.JTextField txtMesMenosEgresos;
    private javax.swing.JTextField txtMesMenosIngresos;
    private javax.swing.JLabel txtMostrarMesEgreso;
    private javax.swing.JLabel txtMostrarMesIngreso;
    private javax.swing.JTextField txtPromedioVentasAnual;
    private javax.swing.JTextField txtVentasAnual;
    // End of variables declaration//GEN-END:variables
}
