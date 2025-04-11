import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Expense_Tracker {
    //helper function for adding expenses to list while maintaining sorted order by date
    private void addSorted(List<Expense> expenses, Expense expense) {
        for(int i = 0; i < expenses.size(); i++) {
            if(!expenses.get(i).date.isBefore(expense.date)) {
                expenses.add(i, expense);
                return;
            }
        }
        expenses.add(expense);
    }

    /*
     * getUsers: Gets dictionary of usernames, passwords, and privilages from storage file
     * input: none
     * returns: dictionary of users whose key is the usernames, and whose values are arrays containing the
     * passwords and levels of the users
     */
    public Dictionary<String, String[]> getUsers(String filename) {
        File userFile = new File(filename);
        Dictionary<String, String[]> users = new Hashtable<>();
        try {
            Scanner reader = new Scanner(userFile);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                //Format of each line in file: "username password level"
                String[] data = line.split(" ");
                String[] val = {data[1], data[2]};
                users.put(data[0], val);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: users file not found");
        }
        return users;
    }

    /*
     * getExpenses: Gets list of expenses from storage file
     * input: none
     * returns: list of Expenses
     */
    public List<Expense> getExpenses(String filename) {
        File expenseFile = new File(filename);
        List<Expense> expenses = new ArrayList<>();
        try {
            Scanner reader = new Scanner(expenseFile);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                //Format of each line in file: "amount yyyy-mm-dd category description"
                String[] data = line.split(" ");
                double amount = Double.parseDouble(data[0]);
                LocalDate date = LocalDate.parse(data[1]);
                String category = data[2];
                String description = data[3];
                for(int i = 4; i < data.length; i++) {
                    description += " " + data[i];
                }
                Expense expense = new Expense(amount, date, category, description);
                addSorted(expenses, expense);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            try {
                expenseFile.createNewFile();
            } catch (IOException e2) {
                System.err.println("Error: expenses file not found, failed to create new file");
            }
        }
        return expenses;
    }

    /*
     * updateFile: updates storage file to reflect changes in expense data
     * input: List of expenses, filename of current storage
     * returns: none
     * results: updates file
     */
    
    private void updateFile(List<Expense> expenses, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(""); //clear file
            PrintWriter printer = new PrintWriter(writer);

            for(int i = 0; i < expenses.size(); i++) {
                printer.print(expenses.get(i).encode() + "\n");
            }

            writer.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * addExpense: adds expense to list based on input, repeated monthly a number of times specified in input
     * input: List of expenses, user inputted data, name of file to update
     * returns: none
     * results: adds expense(s) to list, updates data file
     */
    public void addExpense(List<Expense> expenses, String[] data, String filename) {
        int repeat;
        try {
            repeat = Integer.parseInt(data[1]);
        } catch (Exception e) {
            System.out.println("Invalid number of repeats entered");
            return;
        }
        if(repeat < 1) {
            System.out.println("Invalid number of repeats entered");
            return;
        }
        
        String a = data[2];
        //checking if amount is in correct format (either whole number, or with two decimal places)
        for(int i = 0; i < a.length(); i++) {
            if(!Character.isDigit(a.charAt(i))) {
                if(!(i == a.length()-3 && a.charAt(i) == '.')) {
                    System.out.println("Invalid amount entered");
                    return;
                }
            }
        }
        double amount = Double.parseDouble(a);

        LocalDate date;

        try {
            date = LocalDate.parse(data[3]);
        } catch (Exception e) {
            System.out.println("Invalid date entered");
            return;
        }

        String category = data[4];
        String description = data[5];
        for(int i = 6; i < data.length; i++) {
            description += " " + data[i];
        }

        for(int i = 0; i < repeat; i++) {
            Expense expense = new Expense(amount, date.plusMonths(i), category, description);
            addSorted(expenses, expense);
        }
        updateFile(expenses, filename);
    }

    /*
     * analyze: gets map containing total expenditure for all categories within the date range
     * input: List of expenses, start date, end date
     * returns: map of categories and total amounts
     */
    public Map<String, Double> analyze(List<Expense> expenses, LocalDate start, LocalDate end) {
        Map<String, Double> map = new HashMap<>();
        for(int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            if(!e.date.isBefore(start) && !e.date.isAfter(end)) {
                map.put(e.category, map.getOrDefault(e.category, 0.0) + e.amount);
            }
        }
        return map;
    }

    /*
     * expenditureInRange: gets total expenditure within the date range
     * input: List of expenses, start date, end date
     * returns: total expenditure
     */
    public double expenditureInRange(List<Expense> expenses, LocalDate start, LocalDate end) {
        double total = 0.0;
        for(int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            if(!e.date.isBefore(start) && !e.date.isAfter(end)) {
                total += e.amount;
            }
        }
        return total;
    }

    public static void main(String[] args) {
        Expense_Tracker tracker = new Expense_Tracker();
        Dictionary<String, String[]> users = tracker.getUsers("users.txt");
        if(users.isEmpty()) {
            System.out.println("No users found, unable to run");
        }
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(System.in);
        String user_level = "";
        System.out.println("LOGIN");
        while(user_level.equals("")) {
            System.out.println("Username:");
            String username = sc.nextLine();
            System.out.println("Password:");
            String password = sc.nextLine();
            if(users.get(username)[0].equals(password)) {
                user_level = users.get(username)[1];
            }
            else {
                System.out.println("Invalid username or password, please try again");
            }
        }

        List<Expense> expenses = tracker.getExpenses("expenses.txt");
        String filename = "expenses.txt";

        while(true) {
            System.out.println("Enter command:");
            String input = sc.nextLine();
            String[] command = input.split(" ");
            //ends program
            if(command[0].equals("quit")) {
                break;
            }
            //adds inputted expense to list and file storage, repeated monthly a number of times specified in input
            else if(command[0].equals("add")) {
                //only admins can add expenses
                if(user_level.equals("admin")) {
                    if(command.length >= 5) {
                        tracker.addExpense(expenses, command, filename);
                    }
                    else {
                        System.out.println("Invalid arguments entered");
                    }
                }
                else {
                    System.out.println("You do not have permission to add expenses");
                }
            }
            //prints all expenses
            else if(command[0].equals("view_all")) {
                if(command.length == 1) {
                    for(int i = 0; i < expenses.size(); i++) {
                        System.out.println(expenses.get(i).toString());
                        System.out.println("-------------------------------");
                    }
                }
                else {
                    System.out.println("Invalid arguments entered");
                }
            }
            //prints all expenses in inputted category
            else if(command[0].equals("view_category")) {
                if(command.length == 2) {
                    String category = command[1];
                    for(int i = 0; i < expenses.size(); i++) {
                        if(expenses.get(i).category.equals(category)) {
                            System.out.println(expenses.get(i).toString());
                            System.out.println("-------------------------------");
                        }
                    }
                }
                else {
                    System.out.println("Invalid arguments entered");
                }
            }
            //prints all expenses between inputted dates
            else if(command[0].equals("view_date_range")) {
                if(command.length == 3) {
                    LocalDate start = LocalDate.parse(command[1]);
                    LocalDate end = LocalDate.parse(command[2]);
                    for(int i = 0; i < expenses.size(); i++) {
                        if(!expenses.get(i).date.isBefore(start) && !expenses.get(i).date.isAfter(end)) {
                            System.out.println(expenses.get(i).toString());
                            System.out.println("-------------------------------");
                        }
                    }
                }
                else {
                    System.out.println("Invalid arguments entered");
                }
            }
            //prints total expenditure for each category, as well as total expenditure for all categories
            else if(command[0].equals("summarize")) {
                if(command.length == 1) {
                    Map<String, Double> map = tracker.analyze(expenses, LocalDate.MIN, LocalDate.MAX);
                    double total = 0.0;
                    System.out.println("Expenditure by category:");
                    for(Map.Entry<String, Double> category : map.entrySet()) {
                        total += category.getValue();
                        System.out.println(category.getKey() + ": $" + String.format("%.2f", category.getValue()));
                    }
                    System.out.println("Total expenditure: $" + String.format("%.2f", total));
                }
                else {
                    System.out.println("Invalid arguments entered");
                }
            }
            //prints total expenditure for each category on inputted day
            else if(command[0].equals("analytics_day")) {
                if(command.length == 2) {
                    LocalDate date;
                    try {
                        date = LocalDate.parse(command[1]);
                    } catch (Exception e) {
                        System.out.println("Invalid date entered");
                        return;
                    }
                    Map<String, Double> map = tracker.analyze(expenses, date, date);
                    double total = 0.0;
                    System.out.println("Expenditure by category:");
                    for(Map.Entry<String, Double> category : map.entrySet()) {
                        total += category.getValue();
                        System.out.println(category.getKey() + ": $" + String.format("%.2f", category.getValue()));
                    }
                    System.out.println("Total expenditure: $" + String.format("%.2f", total));
                }
                else {
                    System.out.println("Invalid arguments entered");
                }
            }
            //prints total expenditure for each category on inputted month
            else if(command[0].equals("analytics_month")) {
                if(command.length == 2) {
                    command[1] += "-01";
                    LocalDate date;
                    try {
                        date = LocalDate.parse(command[1]);
                    } catch (Exception e) {
                        System.out.println("Invalid date entered");
                        return;
                    }
                    Map<String, Double> map = tracker.analyze(expenses, date, date.plusMonths(1).minusDays(1));
                    double total = 0.0;
                    System.out.println("Expenditure by category:");
                    for(Map.Entry<String, Double> category : map.entrySet()) {
                        total += category.getValue();
                        System.out.println(category.getKey() + ": $" + String.format("%.2f", category.getValue()));
                    }
                    System.out.println("Total expenditure: $" + String.format("%.2f", total));
                }
                else {
                    System.out.println("Invalid arguments entered");
                }
            }
            //prints expenditure comparison between inputted months
            else if(command[0].equals("compare_months")) {
                if(command.length == 3) {
                    command[1] += "-01";
                    LocalDate date1;
                    try {
                        date1 = LocalDate.parse(command[1]);
                    } catch (Exception e) {
                        System.out.println("Invalid date entered");
                        return;
                    }
                    double amount1 = tracker.expenditureInRange(expenses, date1, date1.plusMonths(1).minusDays(1));

                    command[2] += "-01";
                    LocalDate date2;
                    try {
                        date2 = LocalDate.parse(command[2]);
                    } catch (Exception e) {
                        System.out.println("Invalid date entered");
                        return;
                    }
                    double amount2 = tracker.expenditureInRange(expenses, date2, date2.plusMonths(1).minusDays(1));

                    System.out.println("Expenditure for " + date1.getMonth() + " " + date1.getYear() + ": $" + String.format("%.2f", amount1));
                    System.out.println("Expenditure for " + date2.getMonth() + " " + date2.getYear() + ": $" + String.format("%.2f", amount2));

                    double diff = amount2 - amount1;
                    if(diff < 0.01 && diff > -0.01) {
                        System.out.println("No change");
                    }
                    else if(diff < 0) {
                        diff *= -1.0;
                        System.out.println("Decrease of $" + String.format("%.2f", diff));
                    }
                    else if (diff > 0) {
                        System.out.println("Increase of $" + String.format("%.2f", diff));
                    }
                }
                else {
                    System.out.println("Invalid arguments entered");
                }
            }
            //prints expenditure comparison between inputted years
            else if(command[0].equals("compare_years")) {
                if(command.length == 3) {
                    command[1] += "-01-01";
                    LocalDate date1;
                    try {
                        date1 = LocalDate.parse(command[1]);
                    } catch (Exception e) {
                        System.out.println("Invalid year entered");
                        return;
                    }
                    double amount1 = tracker.expenditureInRange(expenses, date1, date1.plusYears(1).minusDays(1));

                    command[2] += "-01-01";
                    LocalDate date2;
                    try {
                        date2 = LocalDate.parse(command[2]);
                    } catch (Exception e) {
                        System.out.println("Invalid year entered");
                        return;
                    }
                    double amount2 = tracker.expenditureInRange(expenses, date2, date2.plusYears(1).minusDays(1));

                    System.out.println("Expenditure for " + date1.getYear() + ": $" + String.format("%.2f", amount1));
                    System.out.println("Expenditure for " + date2.getYear() + ": $" + String.format("%.2f", amount2));

                    double diff = amount2 - amount1;
                    if(diff < 0.01 && diff > -0.01) {
                        System.out.println("No change");
                    }
                    else if(diff < 0) {
                        diff *= -1.0;
                        System.out.println("Decrease of $" + String.format("%.2f", diff));
                    }
                    else if (diff > 0) {
                        System.out.println("Increase of $" + String.format("%.2f", diff));
                    }
                }
                else {
                    System.out.println("Invalid arguments entered");
                }
            }
            else {
                System.out.println("Invalid command");
            }
        }
        sc.close();
    }
}