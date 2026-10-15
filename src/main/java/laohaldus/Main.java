package laohaldus;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        SampleData.load(inventory);

        ConsoleMenu menu = new ConsoleMenu(inventory, new Scanner(System.in), System.out);
        menu.run();
    }
}
