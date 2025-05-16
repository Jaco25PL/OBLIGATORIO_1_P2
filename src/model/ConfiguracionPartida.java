/*
 * Autores: [Matías Piedra 354007], [Joaquin Piedra 304804] 
 */

package model;

public class ConfiguracionPartida {

    // Changed from public static final to private instance fields, initialized with original values
    private int maxTablerosMostrar = 4;
    private int minTablerosMostrar = 1;
    private int minLargoBanda = 1;
    private int maxLargoBanda = 4;
    private int minBandasFin = 1; 

    private boolean defaultRequiereContacto = false;
    private boolean defaultLargoVariable = false;
    private int defaultLargoFijo = 4;
    private int defaultCantidadBandasFin = 10;
    private int defaultCantidadTableros = 1;

    private boolean requiereContacto;
    private boolean largoBandasVariable;
    private int largoFijo;
    private int cantidadBandasFin;
    private int cantidadTablerosMostrar;

    // inicializa con valores predeterminados.
    public ConfiguracionPartida() {
        this.requiereContacto = this.defaultRequiereContacto;
        this.largoBandasVariable = this.defaultLargoVariable;
        this.largoFijo = this.defaultLargoFijo;
        this.cantidadBandasFin = this.defaultCantidadBandasFin;
        this.cantidadTablerosMostrar = this.defaultCantidadTableros;
    }

    // inicializa con valores personalizados.
    public ConfiguracionPartida(boolean requiereContacto, boolean largoBandasVariable, int largoFijo, int cantidadBandasFin, int cantidadTablerosMostrar) {
        validarLargoFijo(largoFijo); // Validation methods will now use instance fields for limits
        validarCantidadBandasFin(cantidadBandasFin);
        validarCantidadTablerosMostrar(cantidadTablerosMostrar);

        this.requiereContacto = requiereContacto;
        this.largoBandasVariable = largoBandasVariable;
        this.largoFijo = largoFijo; 
        this.cantidadBandasFin = cantidadBandasFin; 
        this.cantidadTablerosMostrar = cantidadTablerosMostrar; 
    }

    // valida largo fijo banda.
    private void validarLargoFijo(int largo) {
        if (largo < this.minLargoBanda || largo > this.maxLargoBanda) {
            throw new IllegalArgumentException("Largo fijo banda entre " + this.minLargoBanda + " y " + this.maxLargoBanda + ".");
        }
    }

    // valida cantidad bandas fin.
    private void validarCantidadBandasFin(int cantidad) {
        if (cantidad < this.minBandasFin) {
            throw new IllegalArgumentException("Cantidad bandas fin al menos " + this.minBandasFin + ".");
        }
    }

    // valida cantidad tableros mostrar.
    private void validarCantidadTablerosMostrar(int cantidad) {
        if (cantidad < this.minTablerosMostrar || cantidad > this.maxTablerosMostrar) {
            throw new IllegalArgumentException("Tableros a mostrar entre " + this.minTablerosMostrar + " y " + this.maxTablerosMostrar + ".");
        }
    }

    // obtiene si requiere contacto.
    public boolean isRequiereContacto() {
        return requiereContacto;
    }

    // establece si requiere contacto.
    public void setRequiereContacto(boolean requiereContacto) {
        this.requiereContacto = requiereContacto;
    }

    // obtiene si largo variable.
    public boolean isLargoBandasVariable() {
        return largoBandasVariable;
    }

    // establece si largo variable.
    public void setLargoBandasVariable(boolean largoBandasVariable) {
        this.largoBandasVariable = largoBandasVariable;
    }

    // obtiene largo fijo banda.
    public int getLargoFijo() { 
        return largoFijo;
    }

    // establece largo fijo banda.
    public void setLargoFijo(int largoFijo) { 
        validarLargoFijo(largoFijo);
        this.largoFijo = largoFijo;
    }

    // obtiene cantidad bandas fin.
    public int getCantidadBandasFin() {
        return cantidadBandasFin;
    }

    // establece cantidad bandas fin.
    public void setCantidadBandasFin(int cantidadBandasFin) {
        validarCantidadBandasFin(cantidadBandasFin);
        this.cantidadBandasFin = cantidadBandasFin;
    }

    // obtiene cantidad tableros mostrar.
    public int getCantidadTablerosMostrar() {
        return cantidadTablerosMostrar;
    }

    // establece cantidad tableros mostrar.
    public void setCantidadTablerosMostrar(int cantidadTablerosMostrar) {
        validarCantidadTablerosMostrar(cantidadTablerosMostrar);
        this.cantidadTablerosMostrar = cantidadTablerosMostrar;
    }
    
    // Getters for the new private fields that were constants, if needed externally
    public int getMinLargoBandaConstant() { return this.minLargoBanda; }
    public int getMaxLargoBandaConstant() { return this.maxLargoBanda; }
    public int getMinBandasFinConstant() { return this.minBandasFin; }
    public int getMinTablerosMostrarConstant() { return this.minTablerosMostrar; }
    public int getMaxTablerosMostrarConstant() { return this.maxTablerosMostrar; }


    // devuelve representación textual configuración.
    @Override
    public String toString() {
        String largoDesc = largoBandasVariable ? 
            "Variable (" + this.minLargoBanda + "-" + this.maxLargoBanda + ")" : 
            "Fijo (" + largoFijo + ")";
        String contactoDesc = requiereContacto ? "Sí" : "No";

        return String.format("ConfiguracionPartida [Requiere Contacto=%s, Largo Bandas=%s, Bandas para Fin=%d, Tableros a Mostrar=%d]",
               contactoDesc, largoDesc, cantidadBandasFin, cantidadTablerosMostrar);
    }

    // restablece a valores predeterminados.
    public void resetToDefaults() {
        this.requiereContacto = this.defaultRequiereContacto;
        this.largoBandasVariable = this.defaultLargoVariable;
        this.largoFijo = this.defaultLargoFijo;
        this.cantidadBandasFin = this.defaultCantidadBandasFin;
        this.cantidadTablerosMostrar = this.defaultCantidadTableros;
    }
}