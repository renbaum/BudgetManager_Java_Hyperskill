package budget;

import java.util.Scanner;

public class Menu{

    Scanner sc;

    public Menu(){
        sc = new Scanner(System.in);
    }
    

    void print(){
        System.out.println("Choose your action:");
        System.out.println("1) Add income");
        System.out.println("2) Add purchase");
        System.out.println("3) Show list of purchases");
        System.out.println("4) Balance");
        System.out.println("5) Save");
        System.out.println("6) Load");
        System.out.println("7) Analyze (Sort)");
        System.out.println("0) Exit");
    }
    
    int getChoice(){
        int choice = -1;
        try{
            choice = Integer.parseInt(sc.nextLine());
            //sc.nextLine();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return choice;
    }

    public void show() {
        int choice = -1;
        do {
            try {
                print();
                choice = getChoice();
                switch (choice) {
                    case 1:
                        SingletonGlobal.getInstance().getBudget().addIncome();
                        break;
                    case 2:
                        SingletonGlobal.getInstance().getBudget().addPurchase();
                        break;
                    case 3:
                        SingletonGlobal.getInstance().getBudget().showPurchase();
                        break;
                    case 4:
                        SingletonGlobal.getInstance().getBudget().showBalance();
                        break;
                    case 5:
                        SingletonGlobal.getInstance().getBudget().saveLists();
                        break;
                    case 6:
                        SingletonGlobal.getInstance().getBudget().loadLists();
                        break;
                    case 7:
                        SingletonGlobal.getInstance().getBudget().analyze();
                        break;
                    case 0:
                        System.out.println();
                        System.out.println("Bye!");
                        break;
                    default:
                        System.out.println("Invalid choice");
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }while(choice != 0);
    }
}
