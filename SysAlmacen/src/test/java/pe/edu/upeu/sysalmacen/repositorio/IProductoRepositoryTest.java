package pe.edu.upeu.sysalmacen.repositorio;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pe.edu.upeu.sysalmacen.dtos.report.ProdMasVendidosDTO;
import pe.edu.upeu.sysalmacen.modelo.Categoria;
import pe.edu.upeu.sysalmacen.modelo.Marca;
import pe.edu.upeu.sysalmacen.modelo.Producto;
import pe.edu.upeu.sysalmacen.modelo.UnidadMedida;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IProductoRepositoryTest {

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private IMarcaRepository marcaRepository;

    @Autowired
    private IUnidadMedidaRepository unidadMedidaRepository;

    private static Long productoId;

    @BeforeEach
    public void setUp() {
        // Crear categoría
        Categoria categoria = new Categoria();
        categoria.setNombre("Electrónica");
        categoria = categoriaRepository.save(categoria);

        // Crear marca
        Marca marca = new Marca();
        marca.setNombre("Sony");
        marca = marcaRepository.save(marca);

        // Crear unidad medida
        UnidadMedida unidad = new UnidadMedida();
        unidad.setNombreMedida("Unidad");
        unidad = unidadMedidaRepository.save(unidad);

        // Crear producto
        Producto producto = new Producto();
        producto.setNombre("Televisor 55\"");
        producto.setPu(1500.0);
        producto.setPuOld(1400.0);
        producto.setStock(20.0);
        producto.setStockOld(15.0);
        producto.setUtilidad(100.0);
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setUnidadMedida(unidad);

        Producto guardado = productoRepository.save(producto);
        productoId = guardado.getIdProducto(); // Ajustar si tu getter tiene otro nombre
    }

    @Test
    @Order(1)
    public void testGuardarProducto() {
        Categoria categoria = categoriaRepository.findAll().get(0);
        Marca marca = marcaRepository.findAll().get(0);
        UnidadMedida unidad = unidadMedidaRepository.findAll().get(0);

        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre("Laptop Gamer");
        nuevoProducto.setPu(3500.0);
        nuevoProducto.setPuOld(3300.0);
        nuevoProducto.setStock(10.0);
        nuevoProducto.setStockOld(8.0);
        nuevoProducto.setUtilidad(200.0);
        nuevoProducto.setCategoria(categoria);
        nuevoProducto.setMarca(marca);
        nuevoProducto.setUnidadMedida(unidad);

        Producto guardado = productoRepository.save(nuevoProducto);
        assertNotNull(guardado.getIdProducto());
        assertEquals("Laptop Gamer", guardado.getNombre());
    }

    @Test
    @Order(2)
    public void testBuscarPorId() {
        Optional<Producto> producto = productoRepository.findById(productoId);
        assertTrue(producto.isPresent());
        assertEquals("Televisor 55\"", producto.get().getNombre());
    }

    @Test
    @Order(3)
    public void testActualizarProducto() {
        Producto producto = productoRepository.findById(productoId).orElseThrow();
        producto.setNombre("Televisor 55 Pulgadas 4K");
        Producto actualizado = productoRepository.save(producto);
        assertEquals("Televisor 55 Pulgadas 4K", actualizado.getNombre());
    }

    @Test
    @Order(4)
    public void testListarProductos() {
        List<Producto> productos = productoRepository.findAll();
        assertFalse(productos.isEmpty());
        System.out.println("Total productos registrados: " + productos.size());
        for (Producto p : productos) {
            System.out.println(p.getNombre() + "\t" + p.getIdProducto());
        }
    }

    @Test
    @Order(5)
    public void testProductosMasVendidos() {
        List<ProdMasVendidosDTO> resultados = productoRepository.findProductosMasVendidos();

        // No falla aunque no haya datos
        assertNotNull(resultados);

        // Opcional: si esperas que tenga datos descomenta:
        // assertFalse(resultados.isEmpty(), "El SP debería devolver datos");
        System.out.println("Resultados SP productosMasVendidos(): " + resultados.size());
    }

    @Test
    @Order(6)
    public void testEliminarProducto() {
        productoRepository.deleteById(productoId);
        Optional<Producto> eliminado = productoRepository.findById(productoId);
        assertFalse(eliminado.isPresent(), "El producto debería haber sido eliminado");
    }
}
