package laohaldus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    private static String toKey(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }
}
