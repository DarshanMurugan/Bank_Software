# Java Console Banking Application ("Baratie Bank")

A lightweight, console-based banking management application built in Java. The project demonstrates core Object-Oriented Programming (OOP) principles, custom navigation routing, page-based UI handling, in-memory data management, and basic authentication.

---

## 🚀 Features

- **Account Creation**: Interactive account registration with password setup and automated account ID generation.
- **Authentication**: Simple ID and password verification for secure transactions.
- **Deposit & Withdrawal**: Perform real-time account balance updates with validation for insufficient funds.
- **Balance Inquiry**: Securely inspect account balances.
- **Fund Transfer**: Seamless money transfer between two existing bank accounts.
- **Custom Page Navigator**: Dynamic menu routing mechanism using an interface-driven architecture (`Pages` and `Navigator`).

---

## 🛠️ Tech Stack & Concepts

- **Language**: Java
- **Data Structures**: `HashMap` for in-memory account storage, password mappings, and dynamic menu routes.
- **Design Patterns & OOP Concepts**:
  - **Abstraction**: Interfaces (`Pages`) and Abstract Classes (`AccountActionPage`).
  - **Encapsulation**: Private state management in core entities like `BankAccount` and `Authentication`.
  - **Polymorphism**: Unified routing engine processing various menu types dynamically.

---

## 📁 Class Structure Overview

| Class / Interface | Description |
| :--- | :--- |
| `bank` | Main class containing the application entry point (`main` method) and route setups. |
| `BankAccount` | Represents individual user accounts, tracking `ACC_ID`, `account_holder`, and `balance`. |
| `bankService` | Business logic layer handling accounts creation, transfers, deposits, and balance queries. |
| `Authentication` | Manages credential storage and password validation. |
| `Navigator` | State machine governing screen transitions and input loop execution. |
| `Pages` | Base interface defining UI display rules and user input handling. |
| `GenericMenu` | Choice-based menu implementation for top-level navigation. |
| `AccountActionPage` | Abstract base class requiring authentication before processing account actions. |

---

## 💻 How to Run

### Prerequisites
- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) (version 8 or higher) installed on your system.

### Steps

1. **Clone or Download the Repository**
   Save the source file as `bank.java`.

2. **Compile the Code**
   Open your terminal/command prompt, navigate to the directory where `bank.java` is located, and execute:
   ```bash
   javac bank.java
   ```

3. **Run the Application**
   Execute the compiled bytecode:
   ```bash
   java bank
   ```

---

## 📝 Example Usage Flow

1. **Main Menu**: Choose option `1` to **Create Account**.
2. **Account Creation**:
   - Enter account holder's name.
   - Set a secure password.
   - Take note of the generated **Account Number** (e.g., `1`).
3. **Transactions**:
   - Return to the Main Menu and select actions like **Deposit**, **Withdraw**, **Check Balance**, or **Transfer**.
   - Input your **Account ID** and **Password** when prompted to authorize transactions.

---

## ⚠️ Known Limitations & Future Improvements

- **In-Memory Storage**: Data resides in memory and resets when the program terminates. Persistent storage (via file I/O or a database like SQL) can be added in future iterations.
- **Single-Session Design**: Currently handles input via standard console inputs (`System.in`).