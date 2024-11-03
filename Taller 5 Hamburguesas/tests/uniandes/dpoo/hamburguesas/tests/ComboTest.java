package uniandes.dpoo.hamburguesas.tests;

import uniandes.dpoo.hamburguesas.mundo.Combo;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

@DisplayName("Pruebas para la clase Combo")
public class ComboTest {
    
    private Combo combo;
    private ArrayList<ProductoMenu> itemsCombo;
    private ProductoMenu producto1;
    private ProductoMenu producto2;
    
    @BeforeEach
    public void setUp() {
        producto1 = new ProductoMenu("Hamburguesa", 10000);
        producto2 = new ProductoMenu("Papas", 5000);
        
        itemsCombo = new ArrayList<>();
        itemsCombo.add(producto1);
        itemsCombo.add(producto2);
        
        combo = new Combo("Combo Familiar", 0.20, itemsCombo);
    }

    @Test
    @DisplayName("Prueba de obtención del nombre del combo")
    public void testGetNombre() {
        assertEquals("Combo Familiar", combo.getNombre(), "El nombre del combo debe ser 'Combo Familiar'");
    }

    @Test
    @DisplayName("Prueba de obtención del descuento del combo")
    public void testGetDescuento() {
        assertEquals(0.20, combo.getDescuento(), "El descuento debe ser 0.20");
    }

    @Test
    @DisplayName("Prueba de modificación del descuento")
    public void testSetDescuento() {
        combo.setDescuento(0.15);
        assertEquals(0.15, combo.getDescuento(), "El descuento debe haber sido actualizado a 0.15");
    }

    @Test
    @DisplayName("Prueba de cálculo del precio del combo con descuento del 20%")
    public void testGetPrecio() {
        int precioEsperado = (int) Math.round((10000 + 5000) * (1 - 0.20));
        assertEquals(precioEsperado, combo.getPrecio(), "El precio del combo debe aplicar el descuento correctamente");
    }

    @Test
    @DisplayName("Prueba de cálculo del precio del combo sin descuento")
    public void testGetPrecioSinDescuento() {
        combo.setDescuento(0.0);
        int precioEsperado = 10000 + 5000;
        assertEquals(precioEsperado, combo.getPrecio(), "El precio del combo sin descuento debe ser la suma de los productos");
    }

    @Test
    @DisplayName("Prueba de cálculo del precio del combo con descuento completo")
    public void testGetPrecioConDescuentoCompleto() {
        combo.setDescuento(1.0);
        int precioEsperado = 0;
        assertEquals(precioEsperado, combo.getPrecio(), "El precio del combo con descuento del 100% debe ser 0");
    }

    @Test
    @DisplayName("Prueba de combo sin productos")
    public void testComboSinProductos() {
        Combo comboVacio = new Combo("Combo Vacío", 0.20, new ArrayList<>());
        assertEquals(0, comboVacio.getPrecio(), "El precio de un combo sin productos debe ser 0");
    }

    @Test
    @DisplayName("Prueba de generación de texto para la factura")
    public void testGenerarTextoFactura() {
        String textoFacturaEsperado = "Combo Combo Familiar\n Descuento: 0.2\n            " + combo.getPrecio() + "\n";
        assertEquals(textoFacturaEsperado, combo.generarTextoFactura(), "El texto de la factura no es el esperado");
    }

    @Test
    @DisplayName("Prueba de encapsulamiento de la lista de productos")
    public void testEncapsulamientoListaProductos() {

        itemsCombo.add(new ProductoMenu("Refresco", 3000));
        int precioEsperado = (int) Math.round((10000 + 5000) * (1 - 0.20));
        assertEquals(precioEsperado, combo.getPrecio(), "El combo debe encapsular su lista de productos");
    }
}
