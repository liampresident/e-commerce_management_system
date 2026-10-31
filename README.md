# Game Store Management Pro

A command-line Java application for managing a board game and gaming accessories store produced as part of my university coursework. It supports two kinds of user, **customers** and **admins**, each with their own menu. Data is stored in plain text files, so no database or external libraries are needed.

## Features

### Customer
- Create a new customer account or log in to an existing one
- View all products (sorted by price, highest first)
- Add items to and remove items from a shopping basket (stock is reserved while items are in the basket)
- View basket contents and cost
- Pay by **Credit Card** or **PayPal** and receive a printed receipt
- Cancel the basket (stock is returned)
- Look up products by ID or search by name, category, type, price, stock or compatibility
- Delete own account

### Admin
- View all products, including purchase cost
- Add a new product (board game or accessory) with an auto-assigned ID
- Remove a product
- Change a product's price
- Order more stock
- Search/filter products (includes purchase cost in the search)
- Create new admin accounts
- View all users
- Delete own account

At any prompt, entering `0` cancels the current action and goes back.

## Requirements

- Java 14 or later (the code uses switch expressions)
- Any Java IDE (e.g. Eclipse, IntelliJ) or the command line

## Getting Started

### Project layout

Source files live in the `test` package, so keep them in a `test/` folder:

```
.
├── README.md
├── src/
│   └── test/
│       ├── Main.java
│       ├── CLI.java
│       ├── AdminCLI.java
│       ├── CustomerCLI.java
│       ├── Users.java
│       ├── Admin.java
│       ├── Customer.java
│       ├── Product.java
│       ├── BoardGame.java
│       ├── Accessory.java
│       ├── AccessoryType.java
│       ├── ProductCategory.java
│       ├── DBFunctions.java
│       ├── PaymentMethod.java
│       ├── PaymentSystem.java
│       ├── Receipt.java
│       ├── CreditCardReceipt.java
│       ├── PayPalReceipt.java
│       └── Address.java
├── Stock.txt
└── UserAccounts.txt
```

The data files (`Stock.txt` and `UserAccounts.txt`) are read from the **working directory**, so run the program from the folder that contains them.

### Compile and run

```bash
# from the project root
javac -d bin src/test/*.java
java -cp bin test.Main
```

### Data file formats

Fields are separated by semicolons.

**UserAccounts.txt**

```
id;name;house number;postcode;city;role
101;Alex;12;LE11 3TU;Loughborough;admin
102;Sam;5;LE11 2AB;Loughborough;customer
```

`role` is either `admin` or `customer`. You need at least one admin in this file to be able to log in as one.

**Stock.txt**

```
id;category;type;name;price;stock;purchase cost;additional info
```

- Board game: `1;board game;strategy;Catan;29.99;10;18.00;4` (additional info = max players)
- Accessory: `2;accessory;dice;Metal Dice Set;12.50;25;6.00;D&D` (additional info = compatibility)

Accessory types must be one of `dice`, `miniature` or `accessory kit`.

## Design Overview

| Class | Responsibility |
|---|---|
| `Main` | Entry point, main menu, login / new customer |
| `CLI` | Reusable input prompts with validation (int, double, string, postcode, category) |
| `CustomerCLI`, `AdminCLI` | Menu loops for each user type |
| `Users` (abstract) | Shared user data, login, logout, delete account, view users |
| `Customer`, `Admin` | Role-specific behaviour (basket and payment / stock management) |
| `Product` (abstract) | Shared product data |
| `BoardGame`, `Accessory` | Product subclasses with their own fields |
| `ProductCategory`, `AccessoryType` | Enums |
| `DBFunctions` | File reading and writing, product loading, searching, ID generation |
| `PaymentSystem` | Collects and validates payment details |
| `Receipt`, `CreditCardReceipt`, `PayPalReceipt` | Receipt hierarchy (polymorphic `printReceipt`) |
| `Address` | Billing address formatting |

OOP concepts demonstrated: inheritance, abstract classes, polymorphism, interfaces, enums and encapsulation.

## Known Limitations

- Credit card validation currently checks for **6 digits** (the code comment says 16), so adjust `PaymentSystem` if you want real-world card lengths.
- Accessory type menu in `AdminCLI` checks option `2` twice, so "Accessory kit" (option 3) cannot be selected.
- `Admin.addProduct` silently returns if the accessory type is invalid.
- Search only considers the product list loaded at login, so changes made by other sessions are not seen until the next login.
- No authentication: users select their account from a list.
- Card details are not stored; only the masked number is printed on the receipt.

## Possible Improvements

- Add password authentication
- Save the basket between sessions
- Replace text files with a database (e.g. SQLite)
- Add unit tests (JUnit)
- Add order history

## Author

Your Name, Your Course / Module, University of ...
