package uniandes.Consola;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class Consola {
    static Restaurante restaurante = new Restaurante();
    private static Scanner input;

    public static void main(String[] args) {
        input = new Scanner(System.in);
        
        try {
            System.out.println("Bienvenido al sistema de hamburguesas");

            // Cargar los datos de los archivos
            System.out.println("Datos subiendose al sistema...");
            restaurante.cargarInformacionRestaurante(
                new File("data/ingredientes.txt"),
                new File("data/menu.txt"),
                new File("data/combos.txt")
            );

            int respuesta = 0;
            while (respuesta != 6) { // 6 para salir
                MostrarMenu();
                System.out.println("Por favor seleccione una de las siguientes opciones: ");
                
                if (input.hasNextInt()) {
                    respuesta = input.nextInt();
                    input.nextLine(); // Consumir la nueva línea pendiente
                } else {
                    input.nextLine(); // Limpiar entrada incorrecta
                    System.out.println("Por favor ingrese un número válido.");
                    continue;
                }

                if (respuesta < 1 || respuesta > 6) {
                    System.out.println("El número que ha ingresado no está en las opciones disponibles. Intente de nuevo.");
                } else {
                    switch (respuesta) {
                        case 1:
                            System.out.println(restaurante.mostrarMenu());
                            break;
                        case 2:
                            System.out.print("Ingrese el nombre del cliente: ");
                            String nombreCliente = input.nextLine();
                            System.out.print("Ingrese la dirección del cliente: ");
                            String direccionCliente = input.nextLine();
                    
                            try {
                                restaurante.iniciarPedido(nombreCliente, direccionCliente);
                                System.out.println("Nuevo pedido iniciado para " + nombreCliente + ".");
                            } catch (YaHayUnPedidoEnCursoException e) {
                                System.out.println("Error: Ya hay un pedido en curso. Debe cerrarse antes de iniciar uno nuevo.");
                            }
                            break; 
                        case 3:
                            System.out.println("Agregar un elemento a un pedido, incluya ajuste: ");
                            if (restaurante.getPedidoEnCurso() == null) {
                                System.out.println("Error: No hay un pedido en curso. Inicie un nuevo pedido antes de agregar elementos.");
                            } else {
                                System.out.println("Seleccione un producto del menú para agregar al pedido:");
                                System.out.println(restaurante.mostrarMenu());
                                System.out.print("Ingrese el nombre del producto que desea agregar: ");
                                String nombreProducto = input.nextLine();
                        
                                Combo comboSeleccionado = null;
                                for (Combo combo : restaurante.getMenuCombos()) {
                                    if (combo.getNombre().equalsIgnoreCase(nombreProducto)) {
                                        comboSeleccionado = combo;
                                        break;
                                    }
                                }
                        
                                if (comboSeleccionado != null) {
                                    System.out.println("No puede hacer ajustes a un combo.");
                                    break;
                                }
                        
                                ArrayList<Ingrediente> ingredientesAgregar = new ArrayList<>();
                                ArrayList<Ingrediente> ingredientesEliminar = new ArrayList<>();
                        
                                String opcionAjuste;
                                do {
                                    System.out.println("¿Desea agregar o quitar ingredientes? (1 para agregar, 2 para quitar, 3 para finalizar ajustes): ");
                                    opcionAjuste = input.next();
                                    input.nextLine(); // Consumir la nueva línea pendiente
                        
                                    if (opcionAjuste.equals("1")) {
                                        System.out.println("Ingredientes disponibles:");
                                        for (Ingrediente ingrediente : restaurante.getIngredientes()) {
                                            System.out.println("- " + ingrediente.getNombre() + " (+$" + ingrediente.getCostoAdicional() + ")");
                                        }
                                        System.out.print("Ingrese el nombre del ingrediente que desea agregar: ");
                                        String nombreIngrediente = input.nextLine();
                                        
                                        Ingrediente ingredienteAAgregar = null;
                                        for (Ingrediente ing : restaurante.getIngredientes()) {
                                            if (ing.getNombre().equalsIgnoreCase(nombreIngrediente)) {
                                                ingredienteAAgregar = ing;
                                                break;
                                            }
                                        }
                        
                                        if (ingredienteAAgregar != null) {
                                            ingredientesAgregar.add(ingredienteAAgregar);
                                            System.out.println("Ingrediente agregado.");
                                        } else {
                                            System.out.println("Ingrediente no encontrado.");
                                        }
                        
                                    } else if (opcionAjuste.equals("2")) {
                                        System.out.print("Ingrese el nombre del ingrediente que desea quitar: ");
                                        String nombreIngrediente = input.nextLine();
                        
                                        Ingrediente ingredienteAEliminar = null;
                                        for (Ingrediente ing : restaurante.getIngredientes()) {
                                            if (ing.getNombre().equalsIgnoreCase(nombreIngrediente)) {
                                                ingredienteAEliminar = ing;
                                                break;
                                            }
                                        }
                        
                                        if (ingredienteAEliminar != null) {
                                            ingredientesEliminar.add(ingredienteAEliminar);
                                            System.out.println("Ingrediente eliminado.");
                                        } else {
                                            System.out.println("Ingrediente no encontrado.");
                                        }
                                    }
                                } while (!opcionAjuste.equals("3"));
                        
                                try {
                                    restaurante.agregarProductoAPedido(nombreProducto, ingredientesAgregar, ingredientesEliminar);
                                } catch (NoHayPedidoEnCursoException e) {
                                    System.out.println("Error: No hay un pedido en curso.");
                                }
                            }
                            break;
                        case 4:
                            System.out.println("Cerrando el pedido actual y guardando la factura...");
                            try {
                                restaurante.cerrarYGuardarPedido();
                                System.out.println("Pedido cerrado y factura guardada exitosamente.");
                            } catch (NoHayPedidoEnCursoException e) {
                                System.out.println("Error: No hay un pedido en curso para cerrar.");
                            } catch (IOException e) {
                                System.out.println("Error al guardar la factura: " + e.getMessage());
                            }
                            break;
                        case 5:
                            System.out.print("Ingrese el identificador del pedido que desea consultar: ");
                            int idPedido = input.nextInt();
                            input.nextLine(); // Consumir la nueva línea pendiente
                            Pedido pedido = restaurante.consultarPedidoPorId(idPedido);

                            if (pedido != null) {
                                System.out.println("Información del pedido:");
                                System.out.println(pedido.generarTextoFactura());
                            } else {
                                System.out.println("No se encontró ningún pedido con el identificador especificado.");
                            }
                            break;
                        case 6:
                            System.out.println("Saliendo del sistema. ¡Gracias por su visita!");
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error en el sistema: " + e.getMessage());
            e.printStackTrace();
        } finally {
            input.close();
        }
    }

    public static void MostrarMenu() {
        System.out.println("Funcionalidades de la aplicación:");
        System.out.println("1. Mostrar el menú del restaurante incluyendo los productos básicos y los combos.");
        System.out.println("2. Iniciar un nuevo pedido.");
        System.out.println("3. Agregar un elemento a un pedido, incluidas opciones de ajuste.");
        System.out.println("4. Cerrar un pedido y guardar la factura.");
        System.out.println("5. Consultar la información de un pedido dado su identificador.");
        System.out.println("6. Salir.");
    }
}
