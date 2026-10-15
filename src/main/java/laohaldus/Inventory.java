package laohaldus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/** Haldab poe tooteid. */
public class Inventory {

    // Võti on nimi väikeste tähtedega, et "Piim" ja "piim" oleksid sama toode.
    // LinkedHashMap annab kiire otsingu ja hoiab lisamise järjekorda.
    private final Map<String, Product> products = new LinkedHashMap<>();

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Toode puudub");
        }
        String key = toKey(product.getName());
        if (products.containsKey(key)) {
            throw new IllegalArgumentException(
                    "Toode \"" + product.getName() + "\" on juba olemas, muuda selle kogust");
        }
        products.put(key, product);
    }

    /** Tagastab koopia, et väljastpoolt ei saaks laoseisu otse muuta. */
    public List<Product> getAllProducts() {
        return List.copyOf(products.values());
    }

    public int size() {
        return products.size();
    }

    /** Täpne otsing: võtme järgi, ei käi tooteid läbi. */
    public Optional<Product> findByName(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(products.get(toKey(name)));
    }

    /** Osaline otsing: "pii" leiab nii "Piim" kui "Piimašokolaad". */
    public List<Product> searchByName(String query) {
        List<Product> matches = new ArrayList<>();
        if (query == null || query.isBlank()) {
            return matches;
        }
        String wanted = toKey(query);
        for (Map.Entry<String, Product> entry : products.entrySet()) {
            if (entry.getKey().contains(wanted)) {
                matches.add(entry.getValue());
            }
        }
        return matches;
    }

    public void increaseQuantity(String name, int amount) {
        getExistingProduct(name).increaseQuantity(amount);
    }

    public void decreaseQuantity(String name, int amount) {
        getExistingProduct(name).decreaseQuantity(amount);
    }

    /** Kõigi toodete hinna ja koguse korrutiste summa. */
    public double getTotalValue() {
        double total = 0;
        for (Product product : products.values()) {
            total += product.getTotalValue();
        }
        return total;
    }

    private Product getExistingProduct(String name) {
        return findByName(name).orElseThrow(
                () -> new IllegalArgumentException("Toodet \"" + name + "\" ei leitud"));
    }

    private static String toKey(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }
}
