package laohaldus;

import java.util.Locale;

/**
 * Üldine toode. Abstraktne, sest poes on alati konkreetne toode (toit, elektroonika),
 * mitte "üldine toode". Iga alamklass peab ise ütlema, milline lisainfo temast näidatakse.
 */
public abstract class Product {

    // Väljad on private: väljastpoolt saab neid muuta ainult allolevate meetodite kaudu
    private final String name;
    private double price;
    private int quantity;

    protected Product(String name, double price, int quantity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Toote nimi ei tohi olla tühi");
        }
        this.name = name.trim();
        setPrice(price);
        setQuantity(quantity);
    }

    /** Iga tootetüüp kirjeldab oma lisainfot ise (polümorfism). */
    public abstract String getExtraInfo();

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Hind ei tohi olla negatiivne");
        }
        this.price = price;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Kogus ei tohi olla negatiivne");
        }
        this.quantity = quantity;
    }

    public void increaseQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Suurendatav kogus peab olema positiivne");
        }
        setQuantity(quantity + amount);
    }

    public void decreaseQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Vähendatav kogus peab olema positiivne");
        }
        if (amount > quantity) {
            throw new IllegalArgumentException(
                    "Laos on ainult " + quantity + " tk, rohkem ei saa vähendada");
        }
        setQuantity(quantity - amount);
    }

    public double getTotalValue() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s | hind: %.2f € | kogus: %d | %s",
                name, price, quantity, getExtraInfo());
    }
}
