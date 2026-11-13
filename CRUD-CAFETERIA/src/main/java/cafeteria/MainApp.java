package cafeteria;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.Connection;

public class MainApp extends Application {

    private TextField txtId, txtNombre, txtTipo, txtPrecio, txtStock;
    private ComboBox<String> cbCategoria, cbTamano, cbDisponibilidad;
    private TableView<Producto> tabla;
    private Label lblEstadoConexion;
    private final ProductoDAO dao = new ProductoDAO();

    @Override
    public void start(Stage stage) {
        Label lblTitulo = new Label("☕ SISTEMA DE GESTIÓN - CAFETERÍA");
        lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #4E342E;");

        lblEstadoConexion = new Label("Verificando conexión...");
        lblEstadoConexion.setStyle("-fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 5;");

        HBox header = new HBox(20, lblTitulo, lblEstadoConexion);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 0, 10, 0));

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.setStyle("-fx-background-color: #EFEBE9; -fx-background-radius: 8;");

        txtId = new TextField();
        txtId.setPromptText("Auto");
        txtId.setEditable(false);
        txtId.setPrefWidth(80);

        txtNombre = new TextField();
        txtNombre.setPromptText("Ej. Café Latte");

        cbCategoria = new ComboBox<>();
        cbCategoria.getItems().addAll("Bebidas Calientes", "Bebidas Frías", "Panadería", "Postres", "Sandwiches");
        cbCategoria.setPromptText("Seleccione...");

        txtTipo = new TextField();
        txtTipo.setPromptText("Ej. Café, Té, Dulce");

        txtPrecio = new TextField();
        txtPrecio.setPromptText("0.00");

        txtStock = new TextField();
        txtStock.setPromptText("0");

        cbTamano = new ComboBox<>();
        cbTamano.getItems().addAll("Pequeño", "Mediano", "Grande", "Único", "Porción");
        cbTamano.setPromptText("Seleccione...");

        cbDisponibilidad = new ComboBox<>();
        cbDisponibilidad.getItems().addAll("Disponible", "Agotado");
        cbDisponibilidad.setValue("Disponible");

        grid.add(new Label("ID:"), 0, 0);
        grid.add(txtId, 1, 0);
        grid.add(new Label("Nombre:"), 2, 0);
        grid.add(txtNombre, 3, 0);
        grid.add(new Label("Categoría:"), 4, 0);
        grid.add(cbCategoria, 5, 0);
        grid.add(new Label("Tipo:"), 6, 0);
        grid.add(txtTipo, 7, 0);

        grid.add(new Label("Precio (S/):"), 0, 1);
        grid.add(txtPrecio, 1, 1);
        grid.add(new Label("Stock:"), 2, 1);
        grid.add(txtStock, 3, 1);
        grid.add(new Label("Tamaño:"), 4, 1);
        grid.add(cbTamano, 5, 1);
        grid.add(new Label("Estado:"), 6, 1);
        grid.add(cbDisponibilidad, 7, 1);

        Button btnRegistrar = new Button("Registrar");
        btnRegistrar.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-weight: bold;");

        Button btnActualizar = new Button("Actualizar");
        btnActualizar.setStyle("-fx-background-color: #1565C0; -fx-text-fill: white; -fx-font-weight: bold;");

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setStyle("-fx-background-color: #C62828; -fx-text-fill: white; -fx-font-weight: bold;");

        Button btnLimpiar = new Button("Limpiar Campos");
        Button btnRefrescar = new Button("Consultar / Refrescar");

        Button btnPdf = new Button("Reporte PDF");
        btnPdf.setStyle("-fx-background-color: #6A1B9A; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox boxBotones = new HBox(12, btnRegistrar, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar, btnPdf);
        boxBotones.setAlignment(Pos.CENTER);
        boxBotones.setPadding(new Insets(10, 0, 10, 0));

        tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Producto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));

        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Producto, String> colCat = new TableColumn<>("Categoría");
        colCat.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        TableColumn<Producto, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn<Producto, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Producto, String> colTam = new TableColumn<>("Tamaño");
        colTam.setCellValueFactory(new PropertyValueFactory<>("tamano"));

        TableColumn<Producto, String> colDisp = new TableColumn<>("Disponibilidad");
        colDisp.setCellValueFactory(new PropertyValueFactory<>("disponibilidad"));

        tabla.getColumns().addAll(colId, colNombre, colCat, colTipo, colPrecio, colStock, colTam, colDisp);

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, p) -> {
            if (p != null) {
                txtId.setText(String.valueOf(p.getIdProducto()));
                txtNombre.setText(p.getNombre());
                cbCategoria.setValue(p.getCategoria());
                txtTipo.setText(p.getTipo());
                txtPrecio.setText(String.valueOf(p.getPrecio()));
                txtStock.setText(String.valueOf(p.getStock()));
                cbTamano.setValue(p.getTamano());
                cbDisponibilidad.setValue(p.getDisponibilidad());
            }
        });

        btnRegistrar.setOnAction(e -> registrarProducto());
        btnActualizar.setOnAction(e -> actualizarProducto());
        btnEliminar.setOnAction(e -> eliminarProducto());
        btnLimpiar.setOnAction(e -> limpiarCampos());
        btnRefrescar.setOnAction(e -> cargarTabla());
        btnPdf.setOnAction(e -> generarPdf());

        VBox root = new VBox(10, header, grid, boxBotones, tabla);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #FAFAFA;");

        verificarConexion();
        cargarTabla();

        Scene scene = new Scene(root, 980, 560);
        stage.setTitle("Evaluación Final - CRUD Cafetería (Luis Palomino)");
        stage.setScene(scene);
        stage.show();
    }

    private void verificarConexion() {
        try (Connection c = Conexion.conectar()) {
            if (c != null) {
                lblEstadoConexion.setText("● Conectado a MySQL (db_cafeteria)");
                lblEstadoConexion.setStyle("-fx-background-color: #C8E6C9; -fx-text-fill: #1B5E20; -fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 5;");
            }
        } catch (Exception e) {
            lblEstadoConexion.setText("● Error de conexión MySQL");
            lblEstadoConexion.setStyle("-fx-background-color: #FFCDD2; -fx-text-fill: #B71C1C; -fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 5;");
        }
    }

    private void cargarTabla() {
        tabla.setItems(dao.listar());
    }

    private void registrarProducto() {
        try {
            if (txtNombre.getText().isEmpty() || cbCategoria.getValue() == null || cbTamano.getValue() == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Completa todos los campos obligatorios.");
                return;
            }
            Producto p = new Producto(
                    0,
                    txtNombre.getText(),
                    cbCategoria.getValue(),
                    txtTipo.getText(),
                    Double.parseDouble(txtPrecio.getText()),
                    Integer.parseInt(txtStock.getText()),
                    cbTamano.getValue(),
                    cbDisponibilidad.getValue()
            );
            if (dao.registrar(p)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto registrado correctamente.");
                limpiarCampos();
                cargarTabla();
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de formato", "Precio y Stock deben ser valores numéricos.");
        }
    }

    private void actualizarProducto() {
        if (txtId.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Selecciona un producto de la tabla para actualizar.");
            return;
        }
        try {
            Producto p = new Producto(
                    Integer.parseInt(txtId.getText()),
                    txtNombre.getText(),
                    cbCategoria.getValue(),
                    txtTipo.getText(),
                    Double.parseDouble(txtPrecio.getText()),
                    Integer.parseInt(txtStock.getText()),
                    cbTamano.getValue(),
                    cbDisponibilidad.getValue()
            );
            if (dao.actualizar(p)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Actualizado", "Registro modificado correctamente.");
                limpiarCampos();
                cargarTabla();
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de formato", "Precio y Stock deben ser valores numéricos.");
        }
    }

    private void eliminarProducto() {
        if (txtId.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Selecciona un producto de la tabla para eliminar.");
            return;
        }
        int id = Integer.parseInt(txtId.getText());
        if (dao.eliminar(id)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminado", "Producto eliminado de la base de datos.");
            limpiarCampos();
            cargarTabla();
        }
    }

    private void generarPdf() {
        try {
            dao.generarReportePDF();
        } catch (Exception ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al generar PDF", ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        cbCategoria.setValue(null);
        txtTipo.clear();
        txtPrecio.clear();
        txtStock.clear();
        cbTamano.setValue(null);
        cbDisponibilidad.setValue("Disponible");
        tabla.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}