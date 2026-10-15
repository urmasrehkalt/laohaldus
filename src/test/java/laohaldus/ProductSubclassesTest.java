package laohaldus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ProductSubclassesTest {

    @Test
    void foodProductInheritsBaseBehaviour() {
        FoodProduct milk = new FoodProduct("Piim", 1.20, 10, LocalDate.of(2026, 12, 31));

        assertEquals("Piim", milk.getName());
        assertEquals(12.0, milk.getTotalValue(), 0.0001);
        milk.decreaseQuantity(4);
        assertEquals(6, milk.getQuantity());
    }

    @Test
    void foodProductShowsExpiryDate() {
        FoodProduct milk = new FoodProduct("Piim", 1.20, 10, LocalDate.of(2026, 12, 31));

        assertEquals("Kõlblik kuni: 2026-12-31", milk.getExtraInfo());
    }

    @Test
    void foodProductRequiresExpiryDate() {
        assertThrows(IllegalArgumentException.class, () -> new FoodProduct("Piim", 1.0, 1, null));
    }

    @Test
    void electronicProductInheritsBaseBehaviour() {
        ElectronicProduct phone = new ElectronicProduct("Telefon", 299.99, 2, 24);

        assertEquals("Telefon", phone.getName());
        assertEquals(599.98, phone.getTotalValue(), 0.0001);
        phone.increaseQuantity(3);
        assertEquals(5, phone.getQuantity());
    }

    @Test
    void electronicProductShowsWarranty() {
        ElectronicProduct phone = new ElectronicProduct("Telefon", 299.99, 2, 24);

        assertEquals("Garantii: 24 kuud", phone.getExtraInfo());
    }

    @Test
    void electronicProductRejectsNegativeWarranty() {
        assertThrows(IllegalArgumentException.class,
                () -> new ElectronicProduct("Telefon", 1.0, 1, -1));
    }

    @Test
    void sameCallGivesDifferentResultDependingOnRealType() {
        Product food = new FoodProduct("Piim", 1.0, 1, LocalDate.of(2026, 12, 31));
        Product device = new ElectronicProduct("Telefon", 1.0, 1, 12);

        assertTrue(food.getExtraInfo().startsWith("Kõlblik"));
        assertTrue(device.getExtraInfo().startsWith("Garantii"));
    }

    @Test
    void subclassesKeepBaseValidation() {
        assertThrows(IllegalArgumentException.class,
                () -> new ElectronicProduct("Telefon", 1.0, -1, 12));
        assertThrows(IllegalArgumentException.class,
                () -> new FoodProduct(" ", 1.0, 1, LocalDate.of(2026, 12, 31)));
    }
}
