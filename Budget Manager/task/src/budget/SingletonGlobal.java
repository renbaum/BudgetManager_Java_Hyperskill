package budget;

public class SingletonGlobal{
    private static SingletonGlobal instance = null;

    private Menu menu = null;
    private Budget budget = null;

    private SingletonGlobal(){
        menu = new Menu();
        budget = new Budget();
    }

    public static SingletonGlobal getInstance(){
        if(instance == null){
            instance = new SingletonGlobal();
        }
        return instance;
    }

    public Menu getMenu() {
        return menu;
    }

    public Budget getBudget() {
        return budget;
    }
}
