package laohaldus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsoleMenuTest {

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        SampleData.load(inventory);
    }

    /** Käivitab menüü etteantud sisendiga (read eraldatud reavahetusega) ja tagastab väljundi. */
    private String runMenu(String... lines) {
        String input = String.join("\n", lines) + "\n";
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        Scanner in = new Scanner(
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

        new ConsoleMenu(inventory, in, out).run();

        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void sampleDataHasAtLeastThreeProductsOfDifferentTypes() {
        assertTrue(inventory.size() >= 3);
        assertTrue(inventory.getAllProducts().stream().anyMatch(p -> p instanceof FoodProduct));
        assertTrue(inventory.getAllProducts().stream().anyMatch(p -> p instanceof ElectronicProduct));
    }

    @Test
    void exitEndsTheProgram() {
        String output = runMenu("0");

        assertTrue(output.contains("Head aega!"));
    }

    @Test
    void endOfInputEndsTheProgramWithoutCrashing() {
        String output = runMenu();

        assertTrue(output.contains("Head aega!"));
    }

    @Test
    void listShowsAllProductsWithTheirOwnExtraInfo() {
        String output = runMenu("1", "0");

        assertTrue(output.contains("Piim"));
        assertTrue(output.contains("Kõlblik kuni: 2026-12-31"));
        assertTrue(output.contains("Telefon"));
        assertTrue(output.contains("Garantii: 24 kuud"));
    }

    @Test
    void addFoodProduct() {
        String output = runMenu("2", "1", "Juust", "7,50", "3", "2027-02-01", "0");

        assertTrue(output.contains("Lisatud: Juust"));
        Product cheese = inventory.findByName("Juust").orElseThrow();
        assertEquals(7.50, cheese.getPrice(), 0.0001);
        assertEquals(3, cheese.getQuantity());
        assertTrue(cheese instanceof FoodProduct);
    }

    @Test
    void addElectronicProduct() {
        String output = runMenu("2", "2", "Hiir", "19.90", "4", "6", "0");

        assertTrue(output.contains("Lisatud: Hiir"));
        Product mouse = inventory.findByName("Hiir").orElseThrow();
        assertTrue(mouse instanceof ElectronicProduct);
        assertEquals(6, ((ElectronicProduct) mouse).getWarrantyMonths());
    }

    @Test
    void addingDuplicateShowsErrorAndKeepsRunning() {
        String output = runMenu("2", "1", "piim", "1", "1", "2027-01-01", "0");

        assertTrue(output.contains("Viga:"));
        assertEquals(4, inventory.size());
        assertTrue(output.contains("Head aega!"));
    }

    @Test
    void increaseQuantity() {
        String output = runMenu("3", "Piim", "5", "0");

        assertEquals(15, inventory.findByName("Piim").orElseThrow().getQuantity());
        assertTrue(output.contains("Uus seis: Piim"));
    }

    @Test
    void decreaseQuantity() {
        runMenu("4", "Piim", "4", "0");

        assertEquals(6, inventory.findByName("Piim").orElseThrow().getQuantity());
    }

    @Test
    void decreasingBelowZeroShowsErrorAndDoesNotChangeQuantity() {
        String output = runMenu("4", "Piim", "11", "0");

        assertTrue(output.contains("Viga:"));
        assertEquals(10, inventory.findByName("Piim").orElseThrow().getQuantity());
    }

    @Test
    void unknownProductShowsError() {
        String output = runMenu("3", "Banaan", "1", "0");

        assertTrue(output.contains("Viga:"));
    }

    @Test
    void notANumberShowsErrorAndReturnsToMenu() {
        String output = runMenu("3", "Piim", "abc", "0");

        assertTrue(output.contains("ei ole täisarv"));
        assertEquals(10, inventory.findByName("Piim").orElseThrow().getQuantity());
        assertTrue(output.contains("Head aega!"));
    }

    @Test
    void invalidDateShowsError() {
        String output = runMenu("2", "1", "Juust", "7", "3", "homme", "0");

        assertTrue(output.contains("ei ole kuupäev"));
        assertTrue(inventory.findByName("Juust").isEmpty());
    }

    @Test
    void searchFindsProduct() {
        String output = runMenu("5", "tele", "0");

        assertTrue(output.contains("Leitud 1 toodet"));
        assertTrue(output.contains("Telefon"));
    }

    @Test
    void searchWithoutResultsSaysSo() {
        String output = runMenu("5", "banaan", "0");

        assertTrue(output.contains("Ühtegi toodet ei leitud."));
    }

    @Test
    void totalValueIsShown() {
        // 12.00 + 10.00 + 599.98 + 149.70 = 771.68
        String output = runMenu("6", "0");

        assertTrue(output.contains("Laoseisu koguväärtus: 771.68 €"));
    }

    @Test
    void unknownMenuChoiceIsReported() {
        String output = runMenu("9", "0");

        assertTrue(output.contains("Tundmatu valik"));
        assertFalse(output.contains("Viga:"));
    }
}
