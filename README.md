# Library Management System (LBMS)

**Course:** Software Design & Analysis - Assignment 4
**Name:** Zaryab Khan
**Registration (Roll) Number:** 23P-0514
**Section:** BCS-9A

A console-based Library Management System written in plain Java (no outside libraries).
It turns the Assignment 3 class diagram into working code and demonstrates classes, constructors
(overloading), composition, arrays, inheritance, abstract classes, interfaces and polymorphism.

## List of classes

| Package | Class | Description |
|---------|-------|-------------|
| model | `Searchable` (interface) | `matches(keyword)` rule, implemented by `Book` and `Member` |
| model | `User` (abstract) | Parent of all users; abstract `showDashboard()` |
| model | `Member` | Borrower; eligibility check |
| model | `Librarian` | Adds books; own dashboard |
| model | `Administrator` | Suspends members; generates report from an array |
| model | `Book` | Book data; holds an array of `BookCopy` (composition) |
| model | `BookCopy` | One physical copy (available / issued / damaged) |
| model | `IssueRecord` | Who borrowed which copy, due and return day |
| model | `Fine` | Late-fee calculation (overloaded `calculateFine`) |
| model | `Notification` (abstract) | Parent of reminders; abstract `send()` |
| model | `EmailNotification` | Sends a reminder by e-mail |
| model | `SmsNotification` | Sends a reminder by SMS |
| model | `IssueController` | Runs Issue Book and Return Book |
| main | `Main` | Console menu and program entry point |

## Project layout

```
src/model/    all model classes
src/main/     Main.java (entry point)
single-file/  Main.java - the whole project in ONE file (no packages)
```

## How to run

### Option 1: Single file (easiest, any IDE)
1. Open `single-file/Main.java`.
2. In Eclipse: create a Java Project, then paste it into a new class named `Main` (or copy the file into `src`).
3. Run `Main` as a Java Application.

Command line:
```
cd single-file
javac Main.java
java Main
```

### Option 2: Full project with packages (Eclipse)
1. Create a Java Project, then create packages `model` and `main` under `src`.
2. Copy the files from `src/model` and `src/main` into the matching packages.
3. Right-click `Main.java` > Run As > Java Application.

Command line (from the repository root):
```
mkdir bin
javac -d bin src/model/*.java src/main/Main.java
java -cp bin main.Main
```

## Menu
1. Add a book  2. Search books  3. Issue a book  4. Return a book
5. Show all dashboards  6. Send reminders (Email + SMS)  7. Generate report  0. Exit

Sample data: members `M101`, `M102`, `M103` (M103 has an unpaid fine), books `B1`, `B2`, `B3`,
copy IDs such as `B1-C1`.
