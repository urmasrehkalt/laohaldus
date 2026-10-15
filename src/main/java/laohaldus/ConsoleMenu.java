package laohaldus;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Konsoolimenüü: loeb kasutaja sisendi ja kutsub välja Inventory toiminguid. */
public class ConsoleMenu {

    private final Inventory inventory;
    private final Scanner in;
    private final PrintStream out;

    public ConsoleMenu(Inventory inventory, Scanner in, PrintStream out) {
        this.inventory = inventory;
        this.in = in;
        this.out = out;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                running = handleChoice(readLine("Vali tegevus: "));
            } catch (IllegalArgumentException e) {
                // Kasutaja viga (nt vale arv või negatiivne kogus): näitame teadet ja jätkame
                out.println("Viga: " + e.getMessage());
            } catch (NoSuchElementException e) {
                // Sisend sai otsa (nt Ctrl+D), lõpetame viisakalt
                running = false;
            }
        }
        out.println("Head aega!");
    }

    private void printMenu() {
        out.println();
        out.println("=== Laohaldus ===");
        out.println("1. Näita kõiki tooteid");
        out.println("2. Lisa uus toode");
        out.println("3. Suurenda toote kogust");
        out.println("4. Vähenda toote kogust");
        out.println("5. Otsi toodet nime järgi");
        out.println("6. Näita laoseisu koguväärtust");
        out.println("0. Välju");
    }

    /** Tagastab false, kui kasutaja tahab väljuda. */
    private boolean handleChoice(String choice) {
        switch (choice.trim()) {
            case "1" -> showAllProducts();
            case "2" -> addProduct();
            case "3" -> changeQuantity(true);
            case "4" -> changeQuantity(false);
            case "5" -> searchProducts();
            case "6" -> showTotalValue();
            case "0" -> {
                return false;
            }
            default -> out.println("Tundmatu valik, proovi uuesti.");
        }
        return true;
    }

    private void showAllProducts() {
        List<Product> products = inventory.getAllProducts();
        if (products.isEmpty()) {
            out.println("Ladu on tühi.");
            return;
        }
        for (Product product : products) {
            // Polümorfism: iga toode kirjeldab ennast oma viisil
            out.println(product);
        }
    }

    private void addProduct() {
        String type = readLine("Tüüp (1 = toit, 2 = elektroonika): ").trim();
        if (!type.equals("1") && !type.equals("2")) {
            throw new IllegalArgumentException("Tüüp peab olema 1 või 2");
        }
        String name = readLine("Nimi: ");
        double price = readDouble("Hind (€): ");
        int quantity = readInt("Kogus: ");

        Product product;
        if (type.equals("1")) {
            product = new FoodProduct(name, price, quantity, readDate("Kõlblik kuni (AAAA-KK-PP): "));
        } else {
            product = new ElectronicProduct(name, price, quantity, readInt("Garantii (kuudes): "));
        }
        inventory.addProduct(product);
        out.println("Lisatud: " + product);
    }

    private void changeQuantity(boolean increase) {
        String name = readLine("Toote nimi: ");
        int amount = readInt(increase ? "Mitu tükki juurde: " : "Mitu tükki maha: ");
        if (increase) {
            inventory.increaseQuantity(name, amount);
        } else {
            inventory.decreaseQuantity(name, amount);
        }
        out.println("Uus seis: " + inventory.findByName(name).orElseThrow());
    }

    private void searchProducts() {
        List<Product> matches = inventory.searchByName(readLine("Otsi: "));
        if (matches.isEmpty()) {
            out.println("Ühtegi toodet ei leitud.");
            return;
        }
        out.println("Leitud " + matches.size() + " toodet:");
        for (Product product : matches) {
            out.println(product);
        }
    }

    private void showTotalValue() {
        out.println(String.format(Locale.ROOT, "Laoseisu koguväärtus: %.2f €",
                inventory.getTotalValue()));
    }

    private String readLine(String prompt) {
        out.print(prompt);
        return in.nextLine();
    }

    private int readInt(String prompt) {
        String text = readLine(prompt).trim();
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("\"" + text + "\" ei ole täisarv");
        }
    }

    private double readDouble(String prompt) {
        String text = readLine(prompt).trim();
        try {
            // Lubame ka koma, sest eestlane kirjutab 1,20
            return Double.parseDouble(text.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("\"" + text + "\" ei ole arv");
        }
    }

    private LocalDate readDate(String prompt) {
        String text = readLine(prompt).trim();
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("\"" + text + "\" ei ole kuupäev kujul AAAA-KK-PP");
        }
    }
}
