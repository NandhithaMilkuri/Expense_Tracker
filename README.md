# 💰 Expense Tracker (Java Swing)

A GUI-based Expense Tracker application with user authentication built using Java.


##  Features

- User Registration & Login (with password hashing)
- Add, Edit, Delete Expenses
- Search Expenses
- Total Expense Calculation
- Month-wise Expense Calculation
- Save Expenses to CSV file
- Load Expenses from file
- User-wise data storage
- Logout functionality

##  Technologies Used

- Java
- Java Swing (GUI)
- File Handling (BufferedReader, FileWriter)
- SHA-256 Password Hashing


##  Project Structure

Expense-Tracker-Java/
│
├── ExpenseTracker.java
├── users.txt
├── README.md
├── .gitignore


##  How to Run

1. Open Command Prompt in project folder

2. Compile:
javac ExpenseTracker.java


3. Run:
java ExpenseTracker


##  Usage

1. Register a new user  
2. Login with same credentials  
3. Add expense:
   - Title
   - Amount
   - Category
   - Date (dd-MM-yyyy)

4. Use buttons:
   - Add → insert data
   - Edit → update selected row
   - Delete → remove expense
   - Total → overall expense
   - Month Total → enter MM-YYYY
   - Save → store data
   - Load → retrieve data
   - Search → find by title
   - Logout → return to login

##  Data Storage

- User credentials → `users.txt`
- Expense data → `username_expenses.csv`


##  Security

Passwords are stored using SHA-256 hashing instead of plain text.


##  Validations

- Date must be in format `dd-MM-yyyy`
- Amount must be numeric
- Invalid inputs are handled with error messages


##  Limitations

- Uses file storage (no database)
- Basic UI design


##  Future Enhancements

- Database integration (MySQL / MongoDB)
- Charts and analytics
- Web or mobile version


##  Conclusion

This project demonstrates Java GUI development, authentication, file handling, and real-world application design.
