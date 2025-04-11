import org.junit.Assert;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public class Expense_Tracker_Tests {
    //helper function to clear test file after each test
    public void clearFile(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("");
            writer.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Test for Expense.encode
    @Test
    public void encode() {
        Expense e = new Expense(19.99, LocalDate.of(2025, 4, 10), "test", "This is a description");
        Assert.assertEquals(e.encode(), "19.99 2025-04-10 test This is a description");
    }
    //Test for Expense.toString
    @Test
    public void toStringTest() {
        Expense e = new Expense(19.99, LocalDate.of(2025, 4, 10), "test", "This is a description");
        Assert.assertEquals(e.toString(), "Amount: $19.99\nDate: 2025-04-10\nCategory: test\nDescription: This is a description");
    }

    /* Tests for getUsers */

    //Test using normal data
    @Test
    public void getUsers1() {
        Expense_Tracker t = new Expense_Tracker();
        Dictionary<String, String[]> dict = t.getUsers("users.txt");
        Assert.assertEquals(dict.size(), 2);
        Assert.assertEquals(dict.get("User")[0], "password");
        Assert.assertEquals(dict.get("User")[1], "user");
        Assert.assertEquals(dict.get("Admin")[0], "password123");
        Assert.assertEquals(dict.get("Admin")[1], "admin");
    }
    //Test with empty file
    @Test
    public void getUsers2() {
        Expense_Tracker t = new Expense_Tracker();
        Dictionary<String, String[]> dict = t.getUsers("empty.txt");
        Assert.assertTrue(dict.isEmpty());
    }
    //Test with nonexistant file
    @Test
    public void getUsers3() {
        Expense_Tracker t = new Expense_Tracker();
        Dictionary<String, String[]> dict = t.getUsers("notreal.txt");
        Assert.assertTrue(dict.isEmpty());
    }

    /* Tests for getExpenses */
    //Test using normal data
    @Test
    public void getExpenses1() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = t.getExpenses("expenses_input_test.txt");
        Assert.assertEquals(expenses.size(), 3);

        Assert.assertEquals(expenses.get(0).amount, 99.99, 0.005);
        Assert.assertEquals(expenses.get(0).date, LocalDate.of(2002, 5, 3));
        Assert.assertEquals(expenses.get(0).category, "test");
        Assert.assertEquals(expenses.get(0).description, "here is a description");

        Assert.assertEquals(expenses.get(1).amount, 253.00, 0.005);
        Assert.assertEquals(expenses.get(1).date, LocalDate.of(2013, 1, 9));
        Assert.assertEquals(expenses.get(1).category, "cat");
        Assert.assertEquals(expenses.get(1).description, "another description");

        Assert.assertEquals(expenses.get(2).amount, 19.87, 0.005);
        Assert.assertEquals(expenses.get(2).date, LocalDate.of(2020, 3, 20));
        Assert.assertEquals(expenses.get(2).category, "tag");
        Assert.assertEquals(expenses.get(2).description, "even more description");
    }
    //Test with empty file
    @Test
    public void getExpenses2() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = t.getExpenses("empty.txt");
        Assert.assertTrue(expenses.isEmpty());
    }
    //Test with nonexistant file (note: will generate file not_real.txt upon completion. Be sure to delete this afterward)
    @Test
    public void getExpenses3() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = t.getExpenses("not_real.txt");
        Assert.assertTrue(expenses.isEmpty());
    }

    /* Tests for addExpense */
    //Test adding to empty list (amount has decimal)
    @Test
    public void addExpense1() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = new ArrayList<>();
        String[] data = {"add", "1", "19.99", "2002-05-03", "test", "This", "is", "a", "description"};
        t.addExpense(expenses, data, "test.txt");
        clearFile("test.txt");
        Assert.assertEquals(expenses.size(), 1);
        Expense e = expenses.get(0);
        Assert.assertEquals(e.amount, 19.99, 0.005);
        Assert.assertEquals(e.date, LocalDate.of(2002, 5, 3));
        Assert.assertEquals(e.category, "test");
        Assert.assertEquals(e.description, "This is a description");
    }
    //Test adding repeat expenses: one to the beginning of the list, one in the middle, one at the end
    @Test
    public void addExpense2() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(1.0, LocalDate.of(2002, 5, 20), "test", "des"));
        expenses.add(new Expense(1.0, LocalDate.of(2002, 7, 1), "test", "des"));

        String[] data = {"add", "3", "19.99", "2002-05-03", "test", "This", "is", "a", "description"};
        t.addExpense(expenses, data, "test.txt");
        clearFile("test.txt");
        Assert.assertEquals(expenses.size(), 5);

        Expense e1 = expenses.get(0);
        Assert.assertEquals(e1.amount, 19.99, 0.005);
        Assert.assertEquals(e1.date, LocalDate.of(2002, 5, 3));
        Assert.assertEquals(e1.category, "test");
        Assert.assertEquals(e1.description, "This is a description");

        Expense e2 = expenses.get(2);
        Assert.assertEquals(e2.amount, 19.99, 0.005);
        Assert.assertEquals(e2.date, LocalDate.of(2002, 6, 3));
        Assert.assertEquals(e2.category, "test");
        Assert.assertEquals(e2.description, "This is a description");

        Expense e3 = expenses.get(4);
        Assert.assertEquals(e3.amount, 19.99, 0.005);
        Assert.assertEquals(e3.date, LocalDate.of(2002, 7, 3));
        Assert.assertEquals(e3.category, "test");
        Assert.assertEquals(e3.description, "This is a description");
    }
    //Test invalid repeat (not int)
    @Test
    public void addExpense3() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = new ArrayList<>();
        String[] data = {"add", "a", "19.99", "2002-05-03", "test", "This", "is", "a", "description"};
        t.addExpense(expenses, data, "test.txt");
        clearFile("test.txt");
        Assert.assertEquals(expenses.size(), 0);
    }
    //Test invalid repeat (negative)
    @Test
    public void addExpense4() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = new ArrayList<>();
        String[] data = {"add", "-1", "19.99", "2002-05-03", "test", "This", "is", "a", "description"};
        t.addExpense(expenses, data, "test.txt");
        clearFile("test.txt");
        Assert.assertEquals(expenses.size(), 0);
    }
    //Test invalid amount (not a number)
    @Test
    public void addExpense5() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = new ArrayList<>();
        String[] data = {"add", "1", "a", "2002-05-03", "test", "This", "is", "a", "description"};
        t.addExpense(expenses, data, "test.txt");
        clearFile("test.txt");
        Assert.assertEquals(expenses.size(), 0);
    }
    //Test invalid amount (negative)
    @Test
    public void addExpense6() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = new ArrayList<>();
        String[] data = {"add", "1", "-10.00", "2002-05-03", "test", "This", "is", "a", "description"};
        t.addExpense(expenses, data, "test.txt");
        clearFile("test.txt");
        Assert.assertEquals(expenses.size(), 0);
    }
    //Test invalid date
    @Test
    public void addExpense7() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = new ArrayList<>();
        String[] data = {"add", "1", "19.99", "20020503", "test", "This", "is", "a", "description"};
        t.addExpense(expenses, data, "test.txt");
        clearFile("test.txt");
        Assert.assertEquals(expenses.size(), 0);
    }

    /* Tests for analyze */
    //Test with all expenses in range
    @Test
    public void analyze1() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(1.00, LocalDate.of(2002, 5, 20), "test", "des"));
        expenses.add(new Expense(1.00, LocalDate.of(2002, 6, 10), "test2", "des"));
        expenses.add(new Expense(1.00, LocalDate.of(2002, 7, 1), "test", "des"));
        Map<String, Double> map = t.analyze(expenses, LocalDate.of(2002, 4, 1), LocalDate.of(2002, 8, 1));
        Assert.assertEquals(map.size(), 2);
        Assert.assertEquals(map.get("test"), 2.00, 0.005);
        Assert.assertEquals(map.get("test2"), 1.00, 0.005);
    }
    //Test with some expenses not in range
    @Test
    public void analyze2() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(1.00, LocalDate.of(2002, 5, 20), "test", "des"));
        expenses.add(new Expense(1.00, LocalDate.of(2002, 6, 10), "test2", "des"));
        expenses.add(new Expense(1.00, LocalDate.of(2002, 7, 1), "test", "des"));
        Map<String, Double> map = t.analyze(expenses, LocalDate.of(2002, 4, 1), LocalDate.of(2002, 6, 30));
        Assert.assertEquals(map.size(), 2);
        Assert.assertEquals(map.get("test"), 1.00, 0.005);
        Assert.assertEquals(map.get("test2"), 1.00, 0.005);
    }
    //Test with no expenses in range
    @Test
    public void analyze3() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(1.00, LocalDate.of(2002, 5, 20), "test", "des"));
        expenses.add(new Expense(1.00, LocalDate.of(2002, 6, 10), "test2", "des"));
        expenses.add(new Expense(1.00, LocalDate.of(2002, 7, 1), "test", "des"));
        Map<String, Double> map = t.analyze(expenses, LocalDate.of(2022, 4, 1), LocalDate.of(2022, 8, 1));
        Assert.assertEquals(map.size(), 0);
    }
    //Test with empty list
    @Test
    public void analyze4() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = new ArrayList<>();
        Map<String, Double> map = t.analyze(expenses, LocalDate.of(2022, 4, 1), LocalDate.of(2022, 8, 1));
        Assert.assertEquals(map.size(), 0);
    }

    /* Tests for expenditureInRange */
    //Test with all expenses in range
    @Test
    public void expenditureInRange1() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(1.00, LocalDate.of(2002, 5, 20), "test", "des"));
        expenses.add(new Expense(1.00, LocalDate.of(2002, 6, 10), "test2", "des"));
        expenses.add(new Expense(1.00, LocalDate.of(2002, 7, 1), "test", "des"));
        double d = t.expenditureInRange(expenses, LocalDate.of(2002, 4, 1), LocalDate.of(2002, 8, 1));
        Assert.assertEquals(d, 3.00, 0.005);
    }
    //Test with some expenses not in range
    @Test
    public void expenditureInRange2() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(1.00, LocalDate.of(2002, 5, 20), "test", "des"));
        expenses.add(new Expense(1.00, LocalDate.of(2002, 6, 10), "test2", "des"));
        expenses.add(new Expense(1.00, LocalDate.of(2002, 7, 1), "test", "des"));
        double d = t.expenditureInRange(expenses, LocalDate.of(2002, 4, 1), LocalDate.of(2002, 6, 30));
        Assert.assertEquals(d, 2.00, 0.005);
    }
    //Test with no expenses in range
    @Test
    public void expenditureInRange3() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(1.00, LocalDate.of(2002, 5, 20), "test", "des"));
        expenses.add(new Expense(1.00, LocalDate.of(2002, 6, 10), "test2", "des"));
        expenses.add(new Expense(1.00, LocalDate.of(2002, 7, 1), "test", "des"));
        double d = t.expenditureInRange(expenses, LocalDate.of(2022, 4, 1), LocalDate.of(2022, 8, 1));
        Assert.assertEquals(d, 0.00, 0.005);
    }
    //Test with empty list
    @Test
    public void expenditureInRange4() {
        Expense_Tracker t = new Expense_Tracker();
        List<Expense> expenses = new ArrayList<>();
        double d = t.expenditureInRange(expenses, LocalDate.of(2022, 4, 1), LocalDate.of(2022, 8, 1));
        Assert.assertEquals(d, 0.00, 0.005);
    }

    /* Final Integration Test
     * This test will simulate the following actions performed by a user, starting from an empty file:
     * -Log in (getUsers and getExpenses are called on startup)
     * -Add an expense repeated 3 times
     * -Add 2 non-repeated expenses
     * -Summarize all expenses
     * -Compare expenses for two months
     * These actions cover all non-print functions
     */

     @Test
     public void integrationTest() {
        Expense_Tracker t = new Expense_Tracker();

        Dictionary<String, String[]> dict = t.getUsers("users.txt");
        Assert.assertEquals(dict.size(), 2);
        Assert.assertEquals(dict.get("User")[0], "password");
        Assert.assertEquals(dict.get("User")[1], "user");
        Assert.assertEquals(dict.get("Admin")[0], "password123");
        Assert.assertEquals(dict.get("Admin")[1], "admin");

        List<Expense> expenses = t.getExpenses("test.txt");
        Assert.assertTrue(expenses.isEmpty());

        String[] data1 = {"add", "3", "19.99", "2002-05-03", "test", "This", "is", "a", "description"};
        String[] data2 = {"add", "1", "59.99", "2002-05-20", "test2", "This", "is", "a", "description"};
        String[] data3 = {"add", "1", "29.99", "2002-06-20", "test3", "This", "is", "a", "description"};
        t.addExpense(expenses, data1, "test.txt");
        t.addExpense(expenses, data2, "test.txt");
        t.addExpense(expenses, data3, "test.txt");

        //clear file before moving on, in case of error. File reading and writing is unneccessary from here on
        clearFile("test.txt");

        Assert.assertEquals(expenses.size(), 5);

        Expense e1 = expenses.get(0);
        Assert.assertEquals(e1.amount, 19.99, 0.005);
        Assert.assertEquals(e1.date, LocalDate.of(2002, 5, 3));
        Assert.assertEquals(e1.category, "test");
        Assert.assertEquals(e1.description, "This is a description");

        Expense e2 = expenses.get(1);
        Assert.assertEquals(e2.amount, 59.99, 0.005);
        Assert.assertEquals(e2.date, LocalDate.of(2002, 5, 20));
        Assert.assertEquals(e2.category, "test2");
        Assert.assertEquals(e2.description, "This is a description");

        Expense e3 = expenses.get(2);
        Assert.assertEquals(e3.amount, 19.99, 0.005);
        Assert.assertEquals(e3.date, LocalDate.of(2002, 6, 3));
        Assert.assertEquals(e3.category, "test");
        Assert.assertEquals(e3.description, "This is a description");

        Expense e4 = expenses.get(3);
        Assert.assertEquals(e4.amount, 29.99, 0.005);
        Assert.assertEquals(e4.date, LocalDate.of(2002, 6, 20));
        Assert.assertEquals(e4.category, "test3");
        Assert.assertEquals(e4.description, "This is a description");

        Expense e5 = expenses.get(4);
        Assert.assertEquals(e5.amount, 19.99, 0.005);
        Assert.assertEquals(e5.date, LocalDate.of(2002, 7, 3));
        Assert.assertEquals(e5.category, "test");
        Assert.assertEquals(e5.description, "This is a description");

        Map<String, Double> summary = t.analyze(expenses, LocalDate.MIN, LocalDate.MAX);
        Assert.assertEquals(summary.size(), 3);
        Assert.assertEquals(summary.get("test"), 59.97, 0.005);
        Assert.assertEquals(summary.get("test2"), 59.99, 0.005);
        Assert.assertEquals(summary.get("test3"), 29.99, 0.005);

        double amount1 = t.expenditureInRange(expenses, LocalDate.of(2002, 5, 1), LocalDate.of(2002, 5, 31));
        Assert.assertEquals(amount1, 79.98, 0.005);

        double amount2 = t.expenditureInRange(expenses, LocalDate.of(2002, 6, 1), LocalDate.of(2002, 6, 30));
        Assert.assertEquals(amount2, 49.98, 0.005);
     }
}