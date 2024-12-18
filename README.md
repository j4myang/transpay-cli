# transpay-cli

A terminal-specific banking system application

## About the Project

A banking system application that is assigned as a project in our Data Structure and Algorithm 
subject which aims to assess our skills in data structures, specifically Linked List. 
The "backend" of the project is built upon Linked List and interaction with the program is through
the Console, specifically the Windows Terminal.

## Why Windows Terminal?

I wanted my program to look sleek and clean. In order to do that, I thought of flushing the printed texts on the console every time I go to a section of my program. Consoles integrated in IDEs such as Netbeans and Eclipse are a trouble to work with and there is no straightforward solution in achieving my goal, thus, deciding to use the Windows Terminal as I can utilize the commands from it, specifically the one for clearing the console through my program with ease as well as the Windows Terminal's customizability over the integrated consoles which allows me to change the background color, font, and more, with convenience in mind and not having to traverse the IDEs settings and whatnot just to achieve it.

## Features
- [x] Create Accounts
- [x] Update Account Details
- [x] Delete Account
- [x] Support Basic ATM Transactions (Balance, Withdraw, Deposit, Transfer)
- [x] Generate Transaction Receipts (opt in/out)
- [x] View Transaction Histor 
- [x] Generate Account Statement (date specific up to one year)
- [x] Administration
- [x] Generate Ragdoll Accounts
- [x] Delete Ragdoll Accounts

## Admin: How to Use

The ID required to log in to the IT side of the system is modeled after our school's students' and maybe professors' 10-digit ID. In essence, it accepts any 10-digit IDs for convenience of testing. 

The `Fix` option is solely for changing the ATM's randomly generated status of `Under Maintenance`, `Online`, and `Offline`. This is solely for realism and are only generated every time the program runs, not every time the Start page is opened. 

> [!NOTE] 
> `Online` status is for when the ATM is running fine.
> `Under Maintenance` status is for when the ATM is out of cash or unable and unable to generate receipts. 
> `Offline` status is for when the ATM is either broken, or unavailable for undisclosed reasons.

The `Create Ragdoll Account` option creates an out of the box account for IT staff to test the system without creating their own accounts and manually make transactions.

> [!NOTE] 
> The amount of transactions generated for the ragdoll account ranges from `25` to `100`.