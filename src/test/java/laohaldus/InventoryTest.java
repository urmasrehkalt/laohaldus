package laohaldus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    private Product milk() {
        return new FoodProduct("Piim", 1.20, 10, LocalDate.of(2026, 12, 31));
    }

    private Product phone() {
        return new ElectronicProduct("Telefon", 299.99, 2, 24);
    }

    @Test
    void startsEmpty() {
        assertEquals(0, inventory.size());
        assertTrue(inventory.getAllProducts().isEmpty());
    }

    @Test
    void addedProductsAreListedInInsertionOrder() {
        Product bread = new FoodProduct("Leib", 2.0, 5, LocalDate.of(2026, 11, 1));
        inventory.addProduct(milk());
        inventory.addProduct(phone());
        inventory.addProduct(bread);

        List<Product> all = inventory.getAllProducts();

        assertEquals(3, inventory.size());
        assertEquals("Piim", all.get(0).getName());
        assertEquals("Telefon", all.get(1).getName());
        assertEquals("Leib", all.get(2).getName());
    }

    @Test
    void duplicateNameIsRejectedIgnoringCase() {
        inventory.addProduct(milk());

        Product duplicate = new FoodProduct("  PIIM ", 1.0, 1, LocalDate.of(2026, 12, 31));

        assertThrows(IllegalArgumentException.class, () -> inventory.addProduct(duplicate));
        assertEquals(1, inventory.size());
    }

    @Test
    void nullProductIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> inventory.addProduct(null));
    }

    @Test
    void returnedListCannotBeUsedToChangeTheInventory() {
        inventory.addProduct(milk());

        List<Product> all = inventory.getAllProducts();

        assertThrows(UnsupportedOperationException.class, () -> all.add(phone()));
        assertEquals(1, inventory.size());
    }
}
