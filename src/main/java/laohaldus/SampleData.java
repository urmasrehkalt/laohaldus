package laohaldus;

import java.time.LocalDate;

/** Algandmed, et rakendus töötaks kohe mitme tootega. */
public final class SampleData {

    private SampleData() {
    }

    public static void load(Inventory inventory) {
        inventory.addProduct(new FoodProduct("Piim", 1.20, 10, LocalDate.of(2026, 12, 31)));
        inventory.addProduct(new FoodProduct("Leib", 2.00, 5, LocalDate.of(2026, 11, 1)));
        inventory.addProduct(new ElectronicProduct("Telefon", 299.99, 2, 24));
        inventory.addProduct(new ElectronicProduct("Kõrvaklapid", 49.90, 3, 12));
    }
}
