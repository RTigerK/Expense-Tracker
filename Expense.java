/*
 * Expense class: Stores data for each expense. Note: category must be one word, no spaces
 */

import java.time.LocalDate;

public class Expense {
    public final double amount;
    public final LocalDate date;
    public final String category;
    public final String description;

    public Expense(double a, LocalDate d, String c, String des) {
        amount = a;
        date = d;
        category = c;
        description = des;
    }

    //encode: converts data into string form used for file storage
    public String encode() {
        String s = String.format("%.2f", amount);
        s += " " + date.toString() + " " + category + " " + description;
        return s;
    }

    //toString: returns string formatted for display
    public String toString() {
        String s = "Amount: $" + String.format("%.2f", amount);
        s += "\nDate: " + date.toString();
        s += "\nCategory: " + category;
        s += "\nDescription: " + description;
        return s;
    }
}
