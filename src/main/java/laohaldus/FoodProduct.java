package laohaldus;

import java.time.LocalDate;

/** Toidukaup: lisaks üldandmetele on tal kõlblikkusaeg. */
public class FoodProduct extends Product {

    private final LocalDate expiryDate;

    public FoodProduct(String name, double price, int quantity, LocalDate expiryDate) {
        super(name, price, quantity);
        if (expiryDate == null) {
            throw new IllegalArgumentException("Kõlblikkusaeg on kohustuslik");
        }
        this.expiryDate = expiryDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    @Override
    public String getExtraInfo() {
        return "Kõlblik kuni: " + expiryDate;
    }
}
