import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.io.*;


class BankAccount {
  int ACC_ID;
  private double balance;
  String account_holder;



  BankAccount(int ACC_ID,String account_holder, double balance){
      this.ACC_ID = ACC_ID;
      this.account_holder = account_holder;
      this.balance = balance;
  }



  public String deposit(double amount){
    balance += amount;
    return "Amount Deposited!, new balance is:" + balance;
  }

  public  String withdraw(double amount){
      if (balance >= amount){
        balance -= amount;
        return "Amount Withdrawn,Current balance is:" + balance;
      }
      else{
        return "Insufficiant Funds";
      }
    }

  public String getBalance(){
    return "Current Balance is: "+ balance;
  }
}

class  bankService {
  BankAccount newAcc;
  private static Map<Integer, BankAccount> accounts = new HashMap<>();
  private static int nextId = 1;
  private static String invalidIdMsg = "Invalid ID";

  private Map<Integer, Pages> routes = new HashMap<>();
  public BankAccount createAccount(String name,String type,int balance) {
    if (type.equalsIgnoreCase("Savings")) {
      newAcc = new BankAccount(nextId++,name,0);

    }

    accounts.put(newAcc.ACC_ID,newAcc);
    return newAcc;


  }

   private boolean  checkValidId(int id){
    
    if (accounts.containsKey(id)) {
      return true;
    }
    else {
      return false;
    }
  }

  public String balanceFromId(int id) {
      if ( checkValidId(id)) {     
        return accounts.get(id).getBalance();
      }
      else {
        return invalidIdMsg;
      }
  }
  public String depositFromId(int id,double amount) {
    if (checkValidId(id)){
      return accounts.get(id).deposit(amount);
    }
    else {
      return invalidIdMsg;
    }
  }
  public String withdrawFromId(int id,double amount) {
    if (checkValidId(id)){
      return accounts.get(id).withdraw(amount);
    }
    else {
      return invalidIdMsg;
    }
  }

  public String transfer(int senderId,int recieverId,double amount){
    if (accounts.containsKey(senderId) && accounts.containsKey(recieverId)){
      BankAccount sender = accounts.get(senderId);
      String sendermsg = sender.withdraw(amount);

      if (sendermsg.equalsIgnoreCase("Insufficiant Funds")) {
        return "Sender has Insufficant funds";
      }

      else {
        BankAccount reciever = accounts.get(recieverId);
        String recievermsg = reciever.deposit(amount);
        return "transfer from " + sender.account_holder + " to " + reciever.account_holder + "\n Sender:" + sendermsg + "\nReciever:" + recievermsg;
      }
      
    }

    else {
      return "Invalid Sender Id or Reciever Id";
    }
  }
}


class GenericMenu implements Pages {
  private String title;
  private String[] options;
  private Navigator nav;
  private Map<Integer, Pages> routes = new HashMap<>();
  public static boolean choiceBased = true;

  public GenericMenu(Navigator nav,String title,String[] options) {
    this.nav = nav;
    this.title = title;
    this.options = options;
  }

  public void addRoute(int choice, Pages target){
    routes.put(choice,target);
  }
  
  @Override
  public boolean isChoiceBased(){
    return true;
  }

  @Override
  public void display(){
    System.out.println("\n---" + title + "---");
    for (int i =0;i < options.length; i++) {
      System.out.println((i+1) + ". " + options[i]);
    }
  }

  @Override
  public void handle(int choice) {
    if (routes.containsKey(choice)) {
      nav.run(routes.get(choice));
    }
    else {
      System.out.println("Invalid choice");
    }
  }
}






interface Pages {
  void display();
  void handle(int choice);
  boolean isChoiceBased();
}


abstract class AccountActionPage implements Pages {
  protected Scanner scanner = new Scanner(System.in);
  protected bankService bankservice;
  protected Navigator nav;
  protected static Map<Integer, Pages> routes = new HashMap<>();
  protected static Authentication authentication = new Authentication();

  public AccountActionPage(Navigator nav,bankService bankservice) {
    this.nav = nav;
    this.bankservice = bankservice;
 }

  @Override
  public boolean isChoiceBased() {
    return false;
  }
  @Override
  public void handle(int choice){
    int id = scanner.nextInt();
    scanner.nextLine();
    System.out.println("Enter Password:");
    String pass = scanner.nextLine();
    if (authentication.checkPassword(id,pass)){
      
      process(id);
    }
    else {
      System.out.println("Invalid Password or Id");
    }
    nav.run(routes.get(0));
  }
  
  protected abstract void process(int id);

}


class showBalanceMenu extends AccountActionPage {
  public showBalanceMenu(Navigator nav, bankService bankservice){
    super(nav,bankservice);
  }

  @Override
  public void display(){
    
    System.out.println("\n--- BALANCE ---");
    System.out.println("Enter Account number:        ");
  }

  @Override 
  protected void process(int id) {
    System.out.println(bankservice.balanceFromId(id));
  }


  public void addRoute(int choice, Pages target){
    routes.put(choice,target);
  }
}

class depositMenu extends AccountActionPage {
  public depositMenu(Navigator nav, bankService bankservice){
    super(nav,bankservice);
  }
  @Override
  public void display() {

    System.out.println("\n--- DEPOSIT ---");
    System.out.println("Enter Account number:        ");
  }
  @Override
  protected void process(int id) {
    
    System.out.println("Enter Deposit Amount:        ");
    double amount = scanner.nextDouble();
    scanner.nextLine();
    System.out.println(bankservice.depositFromId(id,amount));
  }
}

class withdrawMenu extends AccountActionPage {
  public withdrawMenu(Navigator nav, bankService bankservice){
    super(nav,bankservice);
  }
  @Override
  public void display() {
    System.out.println("\n--- WITHDRAW ---");
    System.out.println("Enter Account number:        ");
  }
  @Override
  protected void process(int id){
    System.out.println("Enter Withdraw Ammount:");
    double amount = scanner.nextDouble();
    scanner.nextLine();
    System.out.println(bankservice.withdrawFromId(id,amount));
  }
}

class transferMenu extends AccountActionPage {
  public transferMenu(Navigator nav, bankService bankservice){
    super(nav,bankservice);
  }
  @Override
  public void display() {
    System.out.println("\n--- TRANSFER ---");
    System.out.println("Enter Account number:        ");
  }
  @Override
  protected void process(int senderId){
    System.out.println("Enter the recievers Account number:");
    int recieverId = scanner.nextInt();
    scanner.nextLine();
    System.out.println("Enter amount to be transfered:");
    double amount = scanner.nextDouble();
    scanner.nextLine();
    System.out.println(bankservice.transfer(senderId,recieverId,amount));

  }
}



class AccountCreationMenu implements Pages {
  private static Map<Integer, Pages> routes = new HashMap<>();
  private final Navigator nav;
  private final Scanner scanner = new Scanner(System.in);
  private final static Authentication authentication = new Authentication();
  private BankAccount newAcccount;
  private bankService bankservice = new bankService();
  public AccountCreationMenu(Navigator nav){
    this.nav = nav;
    //actictions.put(1, new MainMenu(nav));
  }


  public void addRoute(int choice, Pages target){
    routes.put(choice,target);
  }

  @Override
  public boolean isChoiceBased() {
    return false;
  }

  @Override
  public void display(){

    System.out.println("\n--- ACCOUNT CREATION MENU ---");
    System.out.println("Enter Name:        ");
  }


  @Override
  public void handle(int choice ) {  //not used
    String name = scanner.nextLine();
    newAcccount = bankservice.createAccount(name,"Savings",0);
    int acc_id = newAcccount.ACC_ID;

    System.out.println("\n--- AUTHENTICATION ---");
    System.out.println("Enter new Password:");
    String pass = scanner.nextLine();
    Authentication.setPassword(acc_id,pass);
    System.out.println("Account Created!,Your Account number is: " + newAcccount.ACC_ID);
    nav.run(routes.get(0));
    

}
}

final class Authentication {
  
  static private Map<Integer,String>  passwords =  new HashMap<>();
  
  
  public static void setPassword(int id,String pass){
    passwords.put(id,pass);
  }

  public boolean checkPassword(int id,String pass) {
    if (pass.equals(passwords.get(id))){
      return true;
    }
    else {
      return false;
    }
  }


}





class Navigator {
  private Pages currentPage;
  private final Scanner scanner = new Scanner(System.in);
  

  public void run(Pages currentPage) {
    this.currentPage = currentPage;

    while (currentPage != null) {
      currentPage.display();
      if (currentPage.isChoiceBased()) {
        
        if (scanner.hasNextInt()) {
          int choice = scanner.nextInt();
          scanner.nextLine();
          currentPage.handle(choice);
          
        } else {
          System.out.println("Enter a valid number");
          scanner.nextLine();
        }
      } else {
        currentPage.handle(0);
      }
      
    }
  }

  public void exit() {
    this.currentPage = null;
  }
}






public class  bank {
  public static void main(String args[]){
    Navigator nav = new Navigator();

    GenericMenu mainMenu = new GenericMenu(nav, "BARATIE MAIN MENU", new String[]{"Create Account","Deposit Money","Withdraw Money","Check Balance","Transfer"});
    AccountCreationMenu accCreation = new AccountCreationMenu(nav);

    bankService bankservice = new bankService();
    showBalanceMenu balanceMenu = new showBalanceMenu(nav,bankservice);
    depositMenu depositmenu = new depositMenu(nav,bankservice);
    withdrawMenu withdrawmenu = new withdrawMenu(nav,bankservice);
    transferMenu transfermenu = new transferMenu(nav,bankservice);

    mainMenu.addRoute(1,accCreation);
    mainMenu.addRoute(2,depositmenu);
    mainMenu.addRoute(3,withdrawmenu);
    mainMenu.addRoute(4,balanceMenu);
    mainMenu.addRoute(5,transfermenu);


    accCreation.addRoute(0,mainMenu);

    balanceMenu.addRoute(0,mainMenu);

    nav.run(mainMenu);

    
  }
}
