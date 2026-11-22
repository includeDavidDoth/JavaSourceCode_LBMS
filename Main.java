import java.util.Scanner;

// ============================== Searchable ==============================
interface Searchable {
    boolean matches(String keyword);
}

// ============================== User ==============================
abstract class User {
    private String userId;
    private String name;
    private String email;
    private String password;

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    public boolean login(String id, String pwd) {
        return userId.equals(id) && password.equals(pwd);
    }

    // concrete method
    public void displayInfo() {
        System.out.println("ID: " + userId + " | Name: " + name + " | Email: " + email);
    }

    // abstract method: every child must override it
    public abstract void showDashboard();
}

// ============================== Member ==============================
class Member extends User implements Searchable {
    private String memberType;
    private int borrowLimit;
    private String status;
    private double fineBalance;

    // constructor 1
    public Member(String userId, String name, String email, String password,
                  String memberType, int borrowLimit) {
        super(userId, name, email, password);
        this.memberType = memberType;
        this.borrowLimit = borrowLimit;
        this.status = "active";
        this.fineBalance = 0;
    }

    // constructor 2 (overloaded): default Student with limit 3
    public Member(String userId, String name, String email, String password) {
        this(userId, name, email, password, "Student", 3);
    }

    public String getMemberType() { return memberType; }
    public int getBorrowLimit() { return borrowLimit; }
    public String getStatus() { return status; }
    public double getFineBalance() { return fineBalance; }
    public void setMemberType(String memberType) { this.memberType = memberType; }
    public void setBorrowLimit(int borrowLimit) { this.borrowLimit = borrowLimit; }
    public void setStatus(String status) { this.status = status; }
    public void setFineBalance(double fineBalance) { this.fineBalance = fineBalance; }

    // relational + logical operators
    public boolean checkEligibility(int currentLoans) {
        return status.equals("active") && fineBalance == 0 && currentLoans < borrowLimit;
    }

    public String getEligibilityReason(int currentLoans) {
        if (!status.equals("active")) {
            return "Account is not active";
        } else if (fineBalance > 0) {
            return "Member has an unpaid fine of Rs. " + fineBalance;
        } else if (currentLoans >= borrowLimit) {
            return "Borrow limit reached";
        } else {
            return "Eligible";
        }
    }

    @Override
    public boolean matches(String keyword) {
        return getName().toLowerCase().contains(keyword.toLowerCase())
                || getUserId().equals(keyword);
    }

    @Override
    public void displayInfo() {
        super.displayInfo();   // super used in a method
        System.out.println("Type: " + memberType + " | Limit: " + borrowLimit
                + " | Status: " + status + " | Fine: Rs. " + fineBalance);
    }

    @Override
    public void showDashboard() {
        System.out.println("=== Member Dashboard ===");
        displayInfo();
        System.out.println("Options: search books, reserve books, view fines");
    }
}

// ============================== Librarian ==============================
class Librarian extends User {
    private String employeeId;
    private String shift;
    private int counterNo;
    private int booksAdded;

    public Librarian(String userId, String name, String email, String password,
                     String employeeId, String shift, int counterNo) {
        super(userId, name, email, password);
        this.employeeId = employeeId;
        this.shift = shift;
        this.counterNo = counterNo;
        this.booksAdded = 0;
    }

    // overloaded constructor: default Morning shift, counter 1
    public Librarian(String userId, String name, String email, String password,
                     String employeeId) {
        this(userId, name, email, password, employeeId, "Morning", 1);
    }

    public String getEmployeeId() { return employeeId; }
    public String getShift() { return shift; }
    public int getCounterNo() { return counterNo; }
    public int getBooksAdded() { return booksAdded; }
    public void setShift(String shift) { this.shift = shift; }
    public void setCounterNo(int counterNo) { this.counterNo = counterNo; }

    public boolean addBook(IssueController controller, Book book) {
        boolean added = controller.addBook(book);
        if (added) {
            booksAdded++;
        }
        return added;
    }

    @Override
    public void showDashboard() {
        System.out.println("=== Librarian Dashboard ===");
        super.displayInfo();   // super used in a method
        System.out.println("Shift: " + shift + " | Counter: " + counterNo
                + " | Books added: " + booksAdded);
        System.out.println("Options: issue book, return book, manage book records");
    }
}

// ============================== Administrator ==============================
class Administrator extends User {
    private String department;
    private int permissionLevel;
    private boolean reportAccess;

    public Administrator(String userId, String name, String email, String password,
                         String department, int permissionLevel) {
        super(userId, name, email, password);
        this.department = department;
        this.permissionLevel = permissionLevel;
        this.reportAccess = true;
    }

    public String getDepartment() { return department; }
    public int getPermissionLevel() { return permissionLevel; }
    public boolean hasReportAccess() { return reportAccess; }
    public void setDepartment(String department) { this.department = department; }
    public void setPermissionLevel(int permissionLevel) { this.permissionLevel = permissionLevel; }
    public void setReportAccess(boolean reportAccess) { this.reportAccess = reportAccess; }

    public void suspendMember(Member m) {
        m.setStatus("suspended");
        System.out.println("Member " + m.getName() + " has been suspended.");
    }

    // an ARRAY is passed to this method
    public void generateReport(IssueRecord[] records, int count) {
        if (!reportAccess) {
            System.out.println("You do not have permission to create reports.");
            return;
        }
        int open = 0;
        int returned = 0;
        int late = 0;
        for (int i = 0; i < count; i++) {
            if (records[i].isOpen()) {
                open++;
            } else {
                returned++;
                if (records[i].getDaysLate() > 0) {
                    late++;
                }
            }
        }
        System.out.println("===== Monthly Report =====");
        System.out.println("Total issued: " + count);
        System.out.println("Still open: " + open);
        System.out.println("Returned: " + returned);
        System.out.println("Returned late: " + late);
    }

    @Override
    public void showDashboard() {
        System.out.println("=== Administrator Dashboard ===");
        super.displayInfo();   // super used in a method
        System.out.println("Department: " + department + " | Permission level: " + permissionLevel);
        System.out.println("Options: manage member accounts, generate reports");
    }
}

// ============================== Book ==============================
class Book implements Searchable {
    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private BookCopy[] copies;     // composition + array
    private int copyCount;

    public Book(String bookId, String title, String author, String isbn, String category) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.copies = new BookCopy[5];
        this.copyCount = 0;
    }

    // overloaded constructor
    public Book(String bookId, String title, String author) {
        this(bookId, title, author, "N/A", "General");
    }

    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public String getCategory() { return category; }
    public int getCopyCount() { return copyCount; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setCategory(String category) { this.category = category; }

    // method overloading: same name, different parameters
    public boolean addCopy(BookCopy copy) {
        if (copyCount < copies.length) {
            copies[copyCount] = copy;
            copyCount++;
            return true;
        }
        return false;
    }

    public boolean addCopy(String copyId, String shelf) {
        return addCopy(new BookCopy(copyId, shelf));
    }

    public BookCopy findAvailableCopy() {
        for (int i = 0; i < copyCount; i++) {
            if (copies[i].isAvailable()) {
                return copies[i];
            }
        }
        return null;
    }

    public BookCopy findCopyById(String copyId) {
        for (int i = 0; i < copyCount; i++) {
            if (copies[i].getCopyId().equals(copyId)) {
                return copies[i];
            }
        }
        return null;
    }

    public int countAvailableCopies() {
        int count = 0;
        for (int i = 0; i < copyCount; i++) {
            if (copies[i].isAvailable()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean matches(String keyword) {
        String k = keyword.toLowerCase();
        return title.toLowerCase().contains(k) || author.toLowerCase().contains(k)
                || isbn.toLowerCase().contains(k) || category.toLowerCase().contains(k);
    }
}

// ============================== BookCopy ==============================
class BookCopy {
    private String copyId;
    private String status;        // available, issued, damaged
    private String shelfLocation;
    private String condition;

    public BookCopy(String copyId, String shelfLocation, String condition) {
        this.copyId = copyId;
        this.shelfLocation = shelfLocation;
        this.condition = condition;
        this.status = "available";
    }

    // overloaded constructor
    public BookCopy(String copyId, String shelfLocation) {
        this(copyId, shelfLocation, "good");
    }

    public String getCopyId() { return copyId; }
    public String getStatus() { return status; }
    public String getShelfLocation() { return shelfLocation; }
    public String getCondition() { return condition; }
    public void setStatus(String status) { this.status = status; }
    public void setShelfLocation(String shelfLocation) { this.shelfLocation = shelfLocation; }
    public void setCondition(String condition) { this.condition = condition; }

    public boolean isAvailable() { return status.equals("available"); }
    public void markIssued() { status = "issued"; }
    public void markAvailable() { status = "available"; }
    public void markDamaged() { status = "damaged"; }
}

// ============================== IssueRecord ==============================
class IssueRecord {
    private String recordId;
    private String memberId;
    private String copyId;
    private int issueDay;    // day number (issue day = 0)
    private int dueDay;
    private int returnDay;   // -1 means not returned yet

    public IssueRecord(String recordId, String memberId, String copyId, int issueDay) {
        this.recordId = recordId;
        this.memberId = memberId;
        this.copyId = copyId;
        this.issueDay = issueDay;
        this.dueDay = issueDay;
        this.returnDay = -1;
    }

    // overloaded constructor: issue day is 0
    public IssueRecord(String recordId, String memberId, String copyId) {
        this(recordId, memberId, copyId, 0);
    }

    public String getRecordId() { return recordId; }
    public String getMemberId() { return memberId; }
    public String getCopyId() { return copyId; }
    public int getIssueDay() { return issueDay; }
    public int getDueDay() { return dueDay; }
    public int getReturnDay() { return returnDay; }

    public void setDueDate(int days) { dueDay = issueDay + days; }
    public void setReturnDate(int day) { returnDay = day; }

    public boolean isOpen() { return returnDay == -1; }

    public int getDaysLate() {
        int late = returnDay - dueDay;
        if (isOpen() || late < 0) {
            return 0;
        }
        return late;
    }
}

// ============================== Fine ==============================
class Fine {
    private String fineId;
    private double amount;
    private int daysLate;
    private boolean paid;
    private double ratePerDay;

    public Fine(String fineId) {
        this.fineId = fineId;
        this.amount = 0;
        this.daysLate = 0;
        this.paid = false;
        this.ratePerDay = 10.0;
    }

    // overloaded constructor
    public Fine(String fineId, int daysLate) {
        this(fineId);
        calculateFine(daysLate);
    }

    public String getFineId() { return fineId; }
    public double getAmount() { return amount; }
    public int getDaysLate() { return daysLate; }
    public boolean isPaid() { return paid; }
    public double getRatePerDay() { return ratePerDay; }
    public void setRatePerDay(double ratePerDay) { this.ratePerDay = ratePerDay; }

    // method overloading (compile-time / static binding)
    public double calculateFine(int daysLate) {
        if (daysLate < 0) {
            daysLate = 0;
        }
        this.daysLate = daysLate;
        this.amount = daysLate * ratePerDay;   // arithmetic operator
        return amount;
    }

    public double calculateFine(int daysLate, double rate) {
        this.ratePerDay = rate;
        return calculateFine(daysLate);
    }

    public void markPaid() { paid = true; }

    // type casting: double to int
    public int getWholeAmount() {
        return (int) amount;
    }
}

// ============================== Notification ==============================
abstract class Notification {
    private String notificationId;
    private String message;
    private String status;

    public Notification(String notificationId, String message) {
        this.notificationId = notificationId;
        this.message = message;
        this.status = "pending";
    }

    public String getNotificationId() { return notificationId; }
    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public void setMessage(String message) { this.message = message; }
    public void setStatus(String status) { this.status = status; }

    // abstract method: children must override
    public abstract boolean send();
}

// ============================== EmailNotification ==============================
class EmailNotification extends Notification {
    private String emailAddress;
    private String subject;
    private String smtpServer;

    public EmailNotification(String id, String message, String emailAddress,
                             String subject, String smtpServer) {
        super(id, message);
        this.emailAddress = emailAddress;
        this.subject = subject;
        this.smtpServer = smtpServer;
    }

    // overloaded constructor
    public EmailNotification(String id, String message, String emailAddress, String subject) {
        this(id, message, emailAddress, subject, "smtp.university.edu");
    }

    public String getEmailAddress() { return emailAddress; }
    public String getSubject() { return subject; }
    public String getSmtpServer() { return smtpServer; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setSmtpServer(String smtpServer) { this.smtpServer = smtpServer; }

    public boolean checkAddress() {
        return emailAddress.contains("@") && emailAddress.length() > 5;
    }

    @Override
    public boolean send() {
        if (checkAddress()) {
            super.setStatus("sent");
            System.out.println("[EMAIL to " + emailAddress + "] Subject: " + subject
                    + " | " + getMessage());
            return true;
        }
        super.setStatus("failed");
        System.out.println("Email failed: invalid address " + emailAddress);
        return false;
    }
}

// ============================== SmsNotification ==============================
class SmsNotification extends Notification {
    private String phoneNumber;
    private String senderId;
    private int maxLength;

    public SmsNotification(String id, String message, String phoneNumber,
                           String senderId, int maxLength) {
        super(id, message);
        this.phoneNumber = phoneNumber;
        this.senderId = senderId;
        this.maxLength = maxLength;
    }

    // overloaded constructor
    public SmsNotification(String id, String message, String phoneNumber, String senderId) {
        this(id, message, phoneNumber, senderId, 100);
    }

    public String getPhoneNumber() { return phoneNumber; }
    public String getSenderId() { return senderId; }
    public int getMaxLength() { return maxLength; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public void setMaxLength(int maxLength) { this.maxLength = maxLength; }

    public boolean checkNumber() {
        return phoneNumber.length() >= 10;
    }

    public String formatMessage() {
        if (getMessage().length() > maxLength) {
            return getMessage().substring(0, maxLength);
        }
        return getMessage();
    }

    @Override
    public boolean send() {
        if (checkNumber()) {
            super.setStatus("sent");
            System.out.println("[SMS from " + senderId + " to " + phoneNumber + "] "
                    + formatMessage());
            return true;
        }
        super.setStatus("failed");
        System.out.println("SMS failed: invalid number " + phoneNumber);
        return false;
    }
}

// ============================== IssueController ==============================
class IssueController {
    private Member[] members;
    private int memberCount;
    private Book[] books;
    private int bookCount;
    private IssueRecord[] records;
    private int recordCount;
    private int fineCount;
    private Scanner input;

    public IssueController(Scanner input) {
        this.input = input;
        members = new Member[20];
        books = new Book[20];
        records = new IssueRecord[100];
        memberCount = 0;
        bookCount = 0;
        recordCount = 0;
        fineCount = 0;
    }

    public IssueRecord[] getRecords() { return records; }
    public int getRecordCount() { return recordCount; }
    public int getBookCount() { return bookCount; }
    public int getMemberCount() { return memberCount; }

    public boolean addMember(Member m) {
        if (memberCount < members.length) {
            members[memberCount] = m;
            memberCount++;
            return true;
        }
        return false;
    }

    public boolean addBook(Book b) {
        if (bookCount < books.length) {
            books[bookCount] = b;
            bookCount++;
            return true;
        }
        return false;
    }

    public Member findMember(String memberId) {
        for (int i = 0; i < memberCount; i++) {
            if (members[i].getUserId().equals(memberId)) {
                return members[i];
            }
        }
        return null;
    }

    public Book findBook(String bookId) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getBookId().equals(bookId)) {
                return books[i];
            }
        }
        return null;
    }

    private BookCopy findCopy(String copyId) {
        for (int i = 0; i < bookCount; i++) {
            BookCopy c = books[i].findCopyById(copyId);
            if (c != null) {
                return c;
            }
        }
        return null;
    }

    private IssueRecord findOpenRecord(String copyId) {
        for (int i = 0; i < recordCount; i++) {
            if (records[i].getCopyId().equals(copyId) && records[i].isOpen()) {
                return records[i];
            }
        }
        return null;
    }

    // an ARRAY is passed to this method
    public int countOpenLoans(IssueRecord[] list, int count, String memberId) {
        int loans = 0;
        for (int i = 0; i < count; i++) {
            if (list[i].getMemberId().equals(memberId) && list[i].isOpen()) {
                loans++;
            }
        }
        return loans;
    }

    public int searchBooks(String keyword) {
        int found = 0;
        for (int i = 0; i < bookCount; i++) {
            if (books[i].matches(keyword)) {
                found++;
                System.out.println(books[i].getBookId() + " | " + books[i].getTitle()
                        + " | " + books[i].getAuthor() + " | " + books[i].getCategory()
                        + " | Available copies: " + books[i].countAvailableCopies()
                        + "/" + books[i].getCopyCount());
            }
        }
        return found;
    }

    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }

    // Use case: Issue Book
    public void issueBook(String memberId, String bookId) {
        Member m = findMember(memberId);
        Book b = findBook(bookId);
        if (m == null || b == null) {
            showError("ID not found, enter again");
            return;
        }
        System.out.println("Member: " + m.getName() + " | Book: " + b.getTitle());

        int loans = countOpenLoans(records, recordCount, memberId);
        if (!m.checkEligibility(loans)) {
            showError(m.getEligibilityReason(loans) + ". Issue cancelled.");
            return;
        }

        BookCopy copy = b.findAvailableCopy();
        if (copy == null) {
            showError("No copy available, you can reserve");
            return;
        }
        if (recordCount >= records.length) {
            showError("Record storage is full");
            return;
        }

        IssueRecord r = new IssueRecord("R" + (recordCount + 1), memberId, copy.getCopyId());
        r.setDueDate(14);
        System.out.print("Issue copy " + copy.getCopyId() + " with due date day "
                + r.getDueDay() + "? Confirm (y/n): ");
        String answer = input.nextLine();
        if (answer.equalsIgnoreCase("y")) {
            records[recordCount] = r;
            recordCount++;
            copy.markIssued();
            System.out.println("Book issued. Record " + r.getRecordId()
                    + ". Due date: day " + r.getDueDay() + " (14 days).");
        } else {
            System.out.println("Issue cancelled by librarian.");
        }
    }

    // Use case: Return Book
    public void returnBook(String copyId, int daysKept) {
        IssueRecord r = findOpenRecord(copyId);
        if (r == null) {
            showError("No open issue record found for copy " + copyId);
            return;
        }
        if (daysKept < 0 || daysKept > 365) {
            showError("Invalid number of days");
            return;
        }
        BookCopy copy = findCopy(copyId);
        Member m = findMember(r.getMemberId());
        System.out.println("Open record " + r.getRecordId() + " found. Member: "
                + r.getMemberId() + ", due on day " + r.getDueDay());

        r.setReturnDate(daysKept);

        System.out.print("Is the book damaged or lost? (y/n): ");
        String damaged = input.nextLine();
        if (damaged.equalsIgnoreCase("y")) {
            copy.markDamaged();
            System.out.println("Copy marked damaged/lost. A replacement fee must be added.");
            return;
        }

        int daysLate = r.getDaysLate();
        if (daysLate > 0) {
            fineCount++;
            Fine f = new Fine("F" + fineCount);
            double amount = f.calculateFine(daysLate);
            System.out.println("Book is " + daysLate + " day(s) late. Fine: Rs. " + amount
                    + " (whole rupees: " + f.getWholeAmount() + ")");
            System.out.print("Pay fine now? (y/n): ");
            String pay = input.nextLine();
            if (pay.equalsIgnoreCase("y")) {
                f.markPaid();
                System.out.println("Fine paid online. Receipt for " + f.getFineId() + " generated.");
            } else {
                m.setFineBalance(m.getFineBalance() + f.getAmount());
                System.out.println("Fine added to the member's balance: Rs. " + m.getFineBalance());
            }
        } else {
            System.out.println("Returned on time. No fine.");
        }

        copy.markAvailable();
        System.out.println("Return confirmed. Copy " + copyId + " is available again.");
    }
}

// ============================== Main (program entry point) ==============================
public class Main {

    // repeats the prompt until the user types a valid number
    public static int readInt(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = input.nextLine().trim();
            boolean ok = s.length() > 0 && s.length() <= 6;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) < '0' || s.charAt(i) > '9') {
                    ok = false;
                }
            }
            if (ok) {
                return Integer.parseInt(s);
            }
            System.out.println("Please enter a valid number.");
        }
    }

    public static String readText(Scanner input, String prompt) {
        System.out.print(prompt);
        return input.nextLine().trim();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        IssueController controller = new IssueController(input);

        // ---------- sample data ----------
        Member m1 = new Member("M101", "Ali Raza", "ali@uni.edu", "pass1");
        Member m2 = new Member("M102", "Sara Ahmed", "sara@uni.edu", "pass2", "Teacher", 5);
        Member m3 = new Member("M103", "Bilal Khan", "bilal@uni.edu", "pass3");
        m3.setFineBalance(200);   // has an unpaid fine, so not eligible
        controller.addMember(m1);
        controller.addMember(m2);
        controller.addMember(m3);

        Librarian librarian = new Librarian("L01", "Ayesha Noor", "ayesha@uni.edu",
                "lib123", "E101", "Morning", 2);
        Administrator admin = new Administrator("A01", "Usman Ali", "usman@uni.edu",
                "admin123", "Library", 3);

        Book b1 = new Book("B1", "Java Programming", "Deitel", "9780134", "Programming");
        b1.addCopy("B1-C1", "Shelf-A");
        b1.addCopy("B1-C2", "Shelf-A");
        Book b2 = new Book("B2", "Database Systems", "Elmasri", "9780133", "Databases");
        b2.addCopy("B2-C1", "Shelf-B");
        Book b3 = new Book("B3", "Software Engineering", "Sommerville");
        b3.addCopy("B3-C1", "Shelf-C");
        controller.addBook(b1);
        controller.addBook(b2);
        controller.addBook(b3);

        // parent-class array holding child objects (runtime polymorphism)
        User[] users = new User[4];
        users[0] = m1;
        users[1] = m2;
        users[2] = librarian;
        users[3] = admin;

        // ---------- menu ----------
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("===== Library Management System =====");
            System.out.println("1. Add a book (Manage Book Records)");
            System.out.println("2. Search books");
            System.out.println("3. Issue a book");
            System.out.println("4. Return a book");
            System.out.println("5. Show all dashboards");
            System.out.println("6. Send reminders (Email + SMS)");
            System.out.println("7. Generate report");
            System.out.println("0. Exit");
            String choice = readText(input, "Enter your choice: ");

            switch (choice) {
                case "1":
                    String id = readText(input, "Book ID: ");
                    if (controller.findBook(id) != null) {
                        System.out.println("ERROR: This book ID already exists.");
                        break;
                    }
                    String title = readText(input, "Title: ");
                    String author = readText(input, "Author: ");
                    String isbn = readText(input, "ISBN: ");
                    String category = readText(input, "Category: ");
                    int copies = readInt(input, "Number of copies (max 5): ");
                    Book nb = new Book(id, title, author, isbn, category);
                    for (int i = 1; i <= copies; i++) {
                        nb.addCopy(id + "-C" + i, "Shelf-New");
                    }
                    if (librarian.addBook(controller, nb)) {
                        System.out.println("Book added with " + nb.getCopyCount() + " copies.");
                    } else {
                        System.out.println("ERROR: Book storage is full.");
                    }
                    break;

                case "2":
                    String keyword = readText(input, "Search by title/author/ISBN/category: ");
                    int found = controller.searchBooks(keyword);
                    if (found == 0) {
                        System.out.println("No books found.");
                    } else {
                        System.out.println(found + " book(s) found.");
                    }
                    break;

                case "3":
                    String memberId = readText(input, "Member ID (M101, M102, M103): ");
                    String bookId = readText(input, "Book ID: ");
                    controller.issueBook(memberId, bookId);
                    break;

                case "4":
                    String copyId = readText(input, "Copy ID (e.g. B1-C1): ");
                    int days = readInt(input, "Days after issue the book is returned: ");
                    controller.returnBook(copyId, days);
                    break;

                case "5":
                    for (int i = 0; i < users.length; i++) {
                        users[i].showDashboard();   // each object behaves differently
                        System.out.println();
                    }
                    break;

                case "6":
                    String phone = readText(input, "Enter phone number for SMS: ");
                    Notification[] notes = new Notification[2];
                    notes[0] = new EmailNotification("N1",
                            "Your book is due in 2 days.", m1.getEmail(), "Due date reminder");
                    notes[1] = new SmsNotification("N2",
                            "Your book is due in 2 days.", phone, "LIBRARY");
                    for (int i = 0; i < notes.length; i++) {
                        notes[i].send();   // email or SMS depending on the object
                    }
                    break;

                case "7":
                    admin.generateReport(controller.getRecords(), controller.getRecordCount());
                    break;

                case "0":
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
        input.close();
    }
}
