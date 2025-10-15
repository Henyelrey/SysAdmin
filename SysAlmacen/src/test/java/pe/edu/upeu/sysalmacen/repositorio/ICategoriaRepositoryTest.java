package pe.edu.upeu.sysalmacen.repositorio;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.upeu.sysalmacen.modelo.Categoria;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@ActiveProfiles("test") //Si usas un perfil de BD de pruebas
public class ICategoriaRepositoryTest {

    @Autowired
    private ICategoriaRepository categoriaRepository;

    private static Long categoriaId;

    @BeforeEach
    public void setUp() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Electrónica");
        Categoria guardada = categoriaRepository.save(categoria);
        categoriaId = guardada.getIdCategoria(); // Ajusta si tu getter tiene otro nombre
    }

    @Test
    @Order(1)
    public void testGuardarCategoria() {
        Categoria nuevaCat = new Categoria();
        nuevaCat.setNombre("Hogar");
        Categoria guardada = categoriaRepository.save(nuevaCat);
        assertNotNull(guardada.getIdCategoria());
        assertEquals("Hogar", guardada.getNombre());
    }

    @Test
    @Order(2)
    public void testBuscarPorId() {
        Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);
        assertTrue(categoria.isPresent());
        assertEquals("Electrónica", categoria.get().getNombre());
    }

    @Test
    @Order(3)
    public void testActualizarCategoria() {
        Categoria categoria = categoriaRepository.findById(categoriaId).orElseThrow();
        categoria.setNombre("Electrónica y Gadgets");
        Categoria actualizada = categoriaRepository.save(categoria);
        assertEquals("Electrónica y Gadgets", actualizada.getNombre());
    }

    @Test
    @Order(4)
    public void testListarCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        assertFalse(categorias.isEmpty());
        System.out.println("Total categorías registradas: " + categorias.size());
        for (Categoria c : categorias) {
            System.out.println(c.getNombre() + "\t" + c.getIdCategoria());
        }
    }

    @Test
    @Order(5)
    public void testEliminarCategoria() {
        categoriaRepository.deleteById(categoriaId);
        Optional<Categoria> eliminada = categoriaRepository.findById(categoriaId);
        assertFalse(eliminada.isPresent(), "La categoría debería haber sido eliminada");
    }
}
