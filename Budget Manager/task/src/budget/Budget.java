package budget;

import java.util.*;
import java.util.stream.Collectors;
import java.io .*;

enum BudgetType {
    FOOD("Food", 1),
    CLOTHES("Clothes", 2),
    ENTERTAINMENT("Entertainment", 3),
    OTHER("Other", 4);

    private final String name;
    private final int id;

    BudgetType(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static BudgetType getByInt(int id) {
        for (BudgetType item : BudgetType.values()) {
            if (item.getId() == id) {
                return item;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + id);
    }
}

class BudgetEntry {
    double amount;
    String name;
    BudgetType category;

    public BudgetEntry(String str, double amount){
        this.amount = amount;
        this.name = str;
        this.category = BudgetType.OTHER;
    }

    public BudgetEntry(String name, double amount, BudgetType category){
        this.amount = amount;
        this.name = name;
        this.category = category;
    }

    public static double getAmount(Object o) {
        return ((BudgetEntry)o).amount;
    }

}

public class Budget{
    List<BudgetEntry> soll;
    List<BudgetEntry> haben;
    Scanner scanner;

    public Budget(){
        soll = new ArrayList<>();
        haben = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void addIncome() {
        System.out.println();
        System.out.println("Enter income:");
        double amount = Float.parseFloat(scanner.nextLine());
        soll.add(new BudgetEntry("Income", amount));
        System.out.print("Income was added!\n");
        System.out.println();
    }

    public void showBalance() {
        double balance = 0;
        for (BudgetEntry entry : soll) {
            balance += entry.amount;
        }
        for (BudgetEntry entry : haben) {
            balance -= entry.amount;
        }
        System.out.println();
        System.out.printf("Balance: $%.2f\n", balance);
        System.out.println();
    }

    public void showPurchase() {
        int choice;
        System.out.println();
        if(haben.isEmpty()){
            System.out.println("The purchase list is empty");
            System.out.println();
            return;
        }
        do {
            choice = showPurchaseMenu(true);
            switch (choice) {
                case 6:
                    break;
                case 1, 2, 3, 4, 5:
                    showPurchase(choice);
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }while(choice != 6);
        System.out.println();
    }

    public void showPurchase(int category) {
        System.out.println();
        List<BudgetEntry> filteredEntries;
        if(category == 5){
            filteredEntries = haben;
        }else {
            filteredEntries = haben.stream()
                    .filter(budgetEntry -> budgetEntry.category.getId() == category)
                    .collect(Collectors.toList());
        }
        if(category == 5){
            System.out.println("All:");
        }else{
            System.out.printf("%s:\n", BudgetType.getByInt(category).getName());
        }
        if(filteredEntries.isEmpty()){
            System.out.println("The purchase list is empty");
            System.out.println();
            return;
        }
        double total = 0;
        for (BudgetEntry entry : filteredEntries) {
            System.out.printf("%s $%.2f\n", entry.name, entry.amount);
            total += entry.amount;
        }
        System.out.printf("Total sum: $%.2f\n", total);
        System.out.println();
    }

    public int showPurchaseMenu(boolean includeAll){
        System.out.println("Choose the type of purchase");
        for (BudgetType item : BudgetType.values()) {
            System.out.printf("%d) %s\n", item.getId(), item.getName());
        }
        if(includeAll){
            System.out.println("5) All");
            System.out.println("6) Back");
        }else {
            System.out.println("5) Back");
        }
        return Integer.parseInt(scanner.nextLine());
    }

    public void addPurchase(){
        System.out.println();
        int choice;
        do {
            choice = showPurchaseMenu(false);
            switch (choice) {
                case 5:
                    break;
                case 1, 2, 3, 4:
                    addPurchase(BudgetType.getByInt(choice));
                    break;
                default:
                    System.out.println("Invalid choice");
            }
            System.out.println();
        }while(choice != 5);
    }

    private void addPurchase(BudgetType category) {
        System.out.println();
        System.out.println("Enter purchase name:");
        String name = scanner.nextLine();
        System.out.println("Enter its price:");
        double price = Float.parseFloat(scanner.nextLine());
        haben.add(new BudgetEntry(name, price, category));
        System.out.println("Purchase was added!");
        //System.out.println();
    }

    public void saveLists() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("purchases.txt"))) {
            String sollList = soll.stream()
                    .map(entry -> String.format("%s;%.2f;%s", entry.name, entry.amount, entry.category.getName()))
                    .collect(Collectors.joining(System.lineSeparator()));
            String habenList = haben.stream()
                    .map(entry -> String.format("%s;%.2f;%s", entry.name, entry.amount, entry.category.getName()))
                    .collect(Collectors.joining(System.lineSeparator()));

            writer.write("==== SOLLLIST ====\n");
            writer.write(sollList);
            writer.write("\n==== HABENLIST ====\n");
            writer.write(habenList);
            writer.flush();
            System.out.println();
            System.out.println("Purchases were saved!");
            System.out.println();
        } catch (IOException e) {
            System.out.println("An error occurred while saving the lists.");
        }
    }

    public void loadLists() {
        try (BufferedReader reader = new BufferedReader(new FileReader("purchases.txt"))) {
            String line;
            List<BudgetEntry> currentList = null;

            while ((line = reader.readLine()) != null) {
                if (line.equals("==== SOLLLIST ====")) {
                    currentList = soll;
                    continue;
                } else if (line.equals("==== HABENLIST ====")) {
                    currentList = haben;
                    continue;
                }

                // Assuming the format is "name amount category"
                String[] parts = line.split(";");
                String name = parts[0];
                double amount = Double.parseDouble(parts[1]);
                BudgetType category = BudgetType.valueOf(parts[2].toUpperCase());

                if (currentList != null)
                    currentList.add(new BudgetEntry(name, amount, category));

            }
            System.out.println();
            System.out.println("Purchases were loaded!");
            System.out.println();

        } catch (IOException e) {
            System.out.println("An error occurred while loading the lists.");
        }
    }

    public int showSortMenu(){
        System.out.println("How do you want to sort?");
        System.out.println("1) Sort all purchases");
        System.out.println("2) Sort by type");
        System.out.println("3) Sort certain type");
        System.out.println("4) Back");
        return Integer.parseInt(scanner.nextLine());
    }

    public int showPurchaseSortMenu(){
        System.out.println();
        System.out.println("Choose the type of purchase");
        for (BudgetType item : BudgetType.values()) {
            System.out.printf("%d) %s\n", item.getId(), item.getName());
        }
        return Integer.parseInt(scanner.nextLine());
    }

    public void analyze(){
        System.out.println();
        int choice;
        do {
            choice = showSortMenu();
            switch (choice) {
                case 4:
                    break;
                case 1:
                    showPurchsesSorted();
                    break;
                case 2:
                    showPurchsesSortedByType();
                    break;
                case 3:
                    showPurchaseCertainType();
                    break;
                default:
                    System.out.println("Invalid choice");
            }
            System.out.println();
        }while(choice != 4);

    }

    private void showPurchaseCertainType() {
        int choice = showPurchaseSortMenu();
        List<BudgetEntry> filteredEntries;

        double total = 0;
        filteredEntries = haben.stream()
                .filter(budgetEntry -> budgetEntry.category.getId() == choice)
                .collect(Collectors.toList());

        System.out.println();
        if(filteredEntries.size() == 0){
            System.out.println("The purchase list is empty");
            return;
        }

        for (BudgetEntry entry : filteredEntries) {
            System.out.printf("%s $%.2f\n", entry.name, entry.amount);
            total += entry.amount;
        }

        System.out.printf("Total sum: $%.2f\n", total);
        System.out.println();
    }

    private void showPurchsesSorted() {
        System.out.println();
        if(haben.size() == 0){
            System.out.println("The purchase list is empty");
            return;
        }
        Collections.sort(haben, Comparator.comparingDouble(BudgetEntry::getAmount).reversed());
        printList(haben);
    }

    private double printSummary(BudgetType category) {
        double sum = 0;
        List<BudgetEntry> filteredEntries;

        filteredEntries = haben.stream()
                .filter(budgetEntry -> budgetEntry.category.getId() == category.getId())
                .collect(Collectors.toList());

        for (BudgetEntry entry : filteredEntries) {
                sum += entry.amount;
        }
        System.out.printf("%s - $%.2f\n", category.getName(), sum);
        return sum;
    }

    private void showPurchsesSortedByType() {
        double total = 0;
        System.out.println();
        System.out.println("Types:");
        total += printSummary(BudgetType.FOOD);
        total += printSummary(BudgetType.ENTERTAINMENT);
        total += printSummary(BudgetType.CLOTHES);
        total += printSummary(BudgetType.OTHER);
        System.out.printf("Total sum: $%.2f\n", total);
        System.out.println();
    }

    void printList(List<BudgetEntry> list) {
        double total = 0;
        for (BudgetEntry entry : list) {
            System.out.printf("%s $%.2f\n", entry.name, entry.amount);
            total += entry.amount;
        }
        System.out.printf("Total: $%.2f", total);
        System.out.println();
    }
}
