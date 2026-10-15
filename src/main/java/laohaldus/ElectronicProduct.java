package laohaldus;

/** Elektroonikatoode: lisaks üldandmetele on tal garantii kuudes. */
public class ElectronicProduct extends Product {

    private final int warrantyMonths;

    public ElectronicProduct(String name, double price, int quantity, int warrantyMonths) {
        super(name, price, quantity);
        if (warrantyMonths < 0) {
            throw new IllegalArgumentException("Garantii ei tohi olla negatiivne");
        }
        this.warrantyMonths = warrantyMonths;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    @Override
    public String getExtraInfo() {
        return "Garantii: " + warrantyMonths + " kuud";
    }
}
