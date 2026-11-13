package cafeteria;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class ProductoDAO {

    public ObservableList<Producto> listar() {
        ObservableList<Producto> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM productos";
        try (Connection conn = Conexion.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getString("tipo"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getString("tamano"),
                        rs.getString("disponibilidad")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrar(Producto p) {
        String sql = "INSERT INTO productos (nombre, categoria, tipo, precio, stock, tamano, disponibilidad) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCategoria());
            ps.setString(3, p.getTipo());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setString(6, p.getTamano());
            ps.setString(7, p.getDisponibilidad());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre=?, categoria=?, tipo=?, precio=?, stock=?, tamano=?, disponibilidad=? WHERE id_producto=?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCategoria());
            ps.setString(3, p.getTipo());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setString(6, p.getTamano());
            ps.setString(7, p.getDisponibilidad());
            ps.setInt(8, p.getIdProducto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idProducto) {
        String sql = "DELETE FROM productos WHERE id_producto=?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }
    public void generarReportePDF() throws Exception {
        try (java.sql.Connection conn = Conexion.conectar();
             java.io.InputStream is = getClass().getResourceAsStream("/reporte_cafeteria.jrxml")) {
            net.sf.jasperreports.engine.JasperReport jasperReport =
                    net.sf.jasperreports.engine.JasperCompileManager.compileReport(is);
            net.sf.jasperreports.engine.JasperPrint jasperPrint =
                    net.sf.jasperreports.engine.JasperFillManager.fillReport(jasperReport, null, conn);
            java.io.File pdfFile = new java.io.File("Reporte_Cafeteria.pdf");
            net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile.getAbsolutePath());
            java.awt.Desktop.getDesktop().open(pdfFile);
        }
    }
}