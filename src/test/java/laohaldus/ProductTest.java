package laohaldus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ProductTest {

    // Product on abstraktne, seega testimiseks on vaja lihtsat konkreetset alamklassi
    private static class SimpleProduct extends Product {
        SimpleProduct(String name, double price, int quantity) {
            super(name, price, quantity);
        }

        @Override
        public String getExtraInfo() {
            return "lisainfo";
        }
    }

    @Test
    void storesNamePriceAndQuantity() {
        Product product = new SimpleProduct("Piim", 1.20, 10);

        assertEquals("Piim", product.getName());
        assertEquals(1.20, product.getPrice(), 0.0001);
        assertEquals(10, product.getQuantity());
    }

    @Test
    void totalValueIsPriceTimesQuantity() {
        Product product = new SimpleProduct("Piim", 1.50, 4);

        assertEquals(6.0, product.getTotalValue(), 0.0001);
    }

    @Test
    void increaseQuantityAddsToCurrentQuantity() {
        Product product = new SimpleProduct("Piim", 1.0, 5);

        product.increaseQuantity(3);

        assertEquals(8, product.getQuantity());
    }

    @Test
    void decreaseQuantitySubtractsFromCurrentQuantity() {
        Product product = new SimpleProduct("Piim", 1.0, 5);

        product.decreaseQuantity(2);

        assertEquals(3, product.getQuantity());
    }

    @Test
    void decreaseQuantityCanReachZero() {
        Product product = new SimpleProduct("Piim", 1.0, 5);

        product.decreaseQuantity(5);

        assertEquals(0, product.getQuantity());
    }

    @Test
    void quantityCannotBecomeNegative() {
        Product product = new SimpleProduct("Piim", 1.0, 2);

        assertThrows(IllegalArgumentException.class, () -> product.decreaseQuantity(3));
        assertEquals(2, product.getQuantity());
    }

    @Test
    void rejectsNegativeQuantityInConstructorAndSetter() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleProduct("Piim", 1.0, -1));

        Product product = new SimpleProduct("Piim", 1.0, 1);
        assertThrows(IllegalArgumentException.class, () -> product.setQuantity(-5));
    }

    @Test
    void rejectsNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleProduct("Piim", -0.5, 1));

        Product product = new SimpleProduct("Piim", 1.0, 1);
        assertThrows(IllegalArgumentException.class, () -> product.setPrice(-1));
    }

    @Test
    void rejectsNonPositiveChangeAmounts() {
        Product product = new SimpleProduct("Piim", 1.0, 5);

        assertThrows(IllegalArgumentException.class, () -> product.increaseQuantity(0));
        assertThrows(IllegalArgumentException.class, () -> product.decreaseQuantity(-1));
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleProduct("  ", 1.0, 1));
        assertThrows(IllegalArgumentException.class, () -> new SimpleProduct(null, 1.0, 1));
    }

    @Test
    void toStringContainsNameAndExtraInfo() {
        Product product = new SimpleProduct("Piim", 1.2, 3);

        String text = product.toString();

        assertTrue(text.contains("Piim"));
        assertTrue(text.contains("lisainfo"));
    }
}
