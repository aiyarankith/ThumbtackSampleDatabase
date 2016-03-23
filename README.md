# ThumbtackSampleDatabase
---

Language: 
---
Java is used here, as the core language to create and implement a Simple Database. 

Data Structure:
---
1. LinkedList is used to maintain the order of the incremental state changes over each transaction scope. When a new transaction scope starts, a block is created and added to the end of the linked list. Each block holds incremental state changes pertaining to the scope to which the command is issued. 
2. HashMap is used to store the key value pairs. <String, Integer> 

Complexity:
--- 
O(log N)

Run the Solution:
---
1. Download ThumbtackDB folder.
2. Compile both the .java files `javac ThumbtackDatabase.java` and `javac Transaction.java`
3. Run the main class `java ThumbtackDatabase`
4. Issue transactional commands

Commands: 
---

Data Commands

    SET name value – Set the variable name to the value value. Neither variable names nor values will contain spaces.

    GET name – Print out the value of the variable name, or NULL if that variable is not set.

    UNSET name – Unset the variable name, making it just like that variable was never set.

    NUMEQUALTO value – Print out the number of variables that are currently set to value. If no variables equal that value, print 0.

    END – Exit the program. Your program will always receive this as its last command.

Transactional Commands



    BEGIN – Open a new transaction block. Transaction blocks can be nested; a BEGIN can be issued inside of an existing block.

    ROLLBACK – Undo all of the commands issued in the most recent transaction block, and close the block. Print nothing if successful, or print NO TRANSACTION if no transaction is in progress.

    COMMIT – Close all open transaction blocks, permanently applying the changes made in them. Print nothing if successful, or print NO TRANSACTION if no transaction is in progress.

Examples: 
--- 

`SET a 10
SET b 10
NUMEQUALTO 10
NUMEQUALTO 20
SET b 30
NUMEQUALTO 10
END`


`BEGIN
SET a 10
GET a
BEGIN
SET a 20
GET a
ROLLBACK
GET a
ROLLBACK
GET a
END`




