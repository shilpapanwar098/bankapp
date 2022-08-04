import com.mysql.cj.xdevapi.PreparableStatement;

import java.sql.*;
import java.util.*;
public class Account {
    int  previousTransaction = -1;
    double balance = -1;
    String userName, userId;
    conn c = new  conn();
    Account(String uname, String uid) throws SQLException {
        userName = uname;
        userId = uid;
        String balQuery = "select balance from accountdetails where userId = '" + userId + "'" ;
        String previousTransactionQuery = "select lastTransaction from accountdetails where userId = '" + userId + "'" ;
        ResultSet balres = c.s.executeQuery(balQuery);
        while(balres.next()) {
            balance = Double.parseDouble(balres.getString("balance"));
        }
        ResultSet transactionres = c.s.executeQuery(previousTransactionQuery);
        while(transactionres.next()) {
            previousTransaction = Integer.parseInt(transactionres.getString("previousTransaction"));
        }

        if (balance == -1 || previousTransaction == -1 || !(balance >=  0)) {
            System.out.println("New account Created with userName : " + userName + " and userId = " + userId);
            String query = "insert into accountdetails values('" + userId + "', '" + userName + "', '" + 0 + "', '" + 0 +"')";
            try {
                c.s.executeUpdate(query);
                balance = 0;
                previousTransaction = 0;
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            System.out.println("Old account found with userName : " + userName + ", userId = " + userId + " and balance = " + balance );
        }
    }

    void deposit(int amount) {
        if(amount!=0) {
            balance += amount;
            previousTransaction = amount;
        }

        String query = "update accountdetails set  balance = '" + balance + "', lastTransaction = '" + previousTransaction +"' where userId = '" + userId + "'" ;
        try {
            PreparedStatement statement = c.con.prepareStatement(query);
            statement.executeUpdate();
            String balQuery = "select * from accountdetails where userId = '" + userId + "'" ;
            ResultSet balres = c.s.executeQuery(balQuery);
            System.out.println("QUERY UPDATED , balres : " + balres);
            while(balres.next()) {
                double temp;
                temp = Double.parseDouble(balres.getString("balance"));
                System.out.println("ROUNAK : " + temp);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("deposited : " + amount);
        System.out.println("Updated balance : " + balance);
    }

    void withdraw(int amount) {
        if(amount!=0) {
            balance -= amount;
            previousTransaction = -amount;
        }

        String query = "update accountdetails set balance = '" + balance + "', lastTransaction = '" + previousTransaction +"' where userId = '" + userId + "'" ;
        try {
            PreparedStatement statement = c.con.prepareStatement(query);
            statement.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("withdrawn : " + amount);
        System.out.println("Updated balance : " + balance);
    }

    void getLastTransaction() {
        if(previousTransaction > 0) {
            System.out.println("Deposited: " + previousTransaction);
        }
        else if(previousTransaction < 0){
            System.out.println("Withdrawn: " + Math.abs(previousTransaction));
        }
        else  System.out.println("No transaction occurred yet ");
    }

    void calculateIntrest(int years) {
        double rate = 0.02;
        double newBalace = (balance * rate *years) + balance;
        System.out.println("Current interest rate is : " + 100*rate);
        System.out.println("After " + years  + " years, your balance will be : " + newBalace);
    }

    void checkBalance() {
        System.out.println("Current balance : " + balance);
    }

    void showMenu() {
        System.out.println();
        System.out.println();
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose among the options given");
        System.out.println("1. View Balance");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Calculate Interest");
        System.out.println("5. Exit");
        int input = sc.nextInt();
        switch (input) {
            case 1 -> {
                checkBalance();
                System.out.println();
                showMenu();
                System.out.println();
            }
            case 2 -> {
                System.out.println("Please enter the amount");
                int mon = sc.nextInt();
                deposit(mon);
                System.out.println();
                System.out.println();

                showMenu();
            }
            case 3 -> {
                System.out.println("Please enter the amount");
                int mon = sc.nextInt();
                withdraw(mon);
                System.out.println();
                System.out.println();
                showMenu();
            }
            case 4 -> {
                System.out.println("Please enter the time (in years)");
                int time = sc.nextInt();
                calculateIntrest(time);
                System.out.println();
                System.out.println();
                showMenu();
            }
            case 5 -> {
                System.out.println("Thanks for banking with Rounak Bank");
            }
            default -> {
                System.out.println();
                System.out.println();
                showMenu();
            }
        }
    }
}
