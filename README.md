# Expense-Tracker
To run the program, simply run Expense_tracker.java

When prompted to log in, use one of the accounts listed in users.txt
For testing purposes, users.txt contains two users:
-User, whose username is "User" and password is "password"
-Admin, whose username is "Admin" and password is "password123"

Once logged in, you can execute the following commands:

quit
Ends program

add (repeat) (amount) (date) (category) (description)
Adds an expense to the data file, repeated monthly as specified (note: only admins can add expenses)
Args:
int repeat: number of times expense is repeated (monthly)
double amount: amount of money in dollars
Date date: date of first expense, formatted yyyy-mm-dd
String category: category of expense (one word)
String description: description of expense (can me multiple words)

view_all
Prints all expenses

view_category (category)
Prints all expenses in the specified category
Args:
String category: category of expense (one word)

view_date_range (start) (end)
Prints all expenses between start and end date
Args:
Date start: date at start of range, formatted yyyy-mm-dd
Date end: date at end of range, formatted yyyy-mm-dd

summarize
Prints total expenditure for each category, as well as overall total expenditure

analytics_day (date)
Prints total expenditure for each category, as well as overall total expenditure, for inputted date
Args:
Date date: date to analyze, formatted yyyy-mm-dd

analytics_month (month)
Prints total expenditure for each category, as well as overall total expenditure, for inputted month
Args:
String month: month to analyze, formatted yyyy-mm

compare_months (month1) (month2)
Prints total expenditure for each month, as well as the difference between them
Args:
String month1: first month to analyze, formatted yyyy-mm
String month2: second month to analyze, formatted yyyy-mm

compare_years (year1) (year2)
Prints total expenditure for each year, as well as the difference between them
Args:
int year1: first year to analyze
int year2: second year to analyze

To execute unit tests, run functions using JUnit in your IDE of choice (I used Visual Studio Code).
After running test getExpenses3, a file called not_real.txt will be created if successful. Be sure to delete this file
to ensure proper testing in the future

The following features were implemented:
-User login, with different permission levels for different users
-Adding expenses to tracker
-Adding monthly repeating expenses
-Printing expenses from tracker, with options to filter by date or category
-A summary of total expenses in each category
-Analytics of total expenses in each category for a given month or year
-Comparison of total expenditure between different months or years

The only technologies used were Java and JUnit. I decided to keep my design simple, with a 
command line interface and file-based storage, in order to ensure I could focus on getting the
core functionality working without needing to worry about integrations between different technologies.
This unfortunately limited my ability to include many of the proposed bonus features.