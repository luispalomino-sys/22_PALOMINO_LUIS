package cafeteria;

public class Producto {
    private int idProducto;
    private String nombre;
    private String categoria;
    private String tipo;
    private double precio;
    private int stock;
    private String tamano;
    private String disponibilidad;

    public Producto(int idProducto, String nombre, String categoria, String tipo, double precio, int stock, String tamano, String disponibilidad) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.categoria = categoria;
        this.tipo = tipo;
        this.precio = precio;
        this.stock = stock;
        this.tamano = tamano;
        this.disponibilidad = disponibilidad;
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getTamano() { return tamano; }
    public void setTamano(String tamano) { this.tamano = tamano; }

    public String getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(String disponibilidad) { this.disponibilidad = disponibilidad; }
}