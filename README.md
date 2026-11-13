
# Programming in Java - Assignment 3 (CO3)

## Student Details

- **Student A:** K Suthesika
- **Register Number:** 192511070
- **Student B:** Harisha S.
- **Partner Register Number:** 192324300
- **Course Code:** UBA0920
- **Java Version:** JDK 21.0.13 (Temurin)

## GitHub Repository

https://github.com/192511070simats-code/CSA09-192511070-Assignment3

## Assignment Tasks

| Task | Topic | Status |
|---|---|---|
| Task 01 | ATM Exception Handling | Completed - 6 tests executed |
| Task 02 | User-Defined Exceptions and Propagation | Completed |
| Task 03 | Resource Safety and Debugging | Completed |
| Task 04 | Thread Creation and Life Cycle | Pending |
| Task 05 | Race Conditions and Synchronization | Pending |
| Task 06 | Deadlock and Producer-Consumer | Pending |
| Task 07 | Concurrent Fault-Tolerant Processor | Pending |

## Compile and Run

Open the relevant task folder in the terminal.

```powershell
javac FileName.java
java FileName
```

## Task 01 Results

The ATM withdrawal program was implemented using checked and unchecked exceptions, a user-defined exception, multi-catch and finally.

Six test conditions were executed:
1. Invalid account number
2. Invalid withdrawal input
3. Negative withdrawal amount
4. Insufficient account balance
5. Successful withdrawal
6. Missing card-reader file

## Task Results

Source code, test results, experimental data and output screenshots will be added for each completed task.

## Task 02 – User-Defined Exceptions and Exception Propagation

**Status:** Completed

**Programs:**
- `StorageDemo.java` – Custom storage exception and exception chaining.
- `PortalExceptionDemo.java` – Three examination portal failure scenarios.
- `ChainingDemo.java` – Demonstration of getCause() and chained stack traces.

**Concepts Covered:**
- User-defined checked and unchecked exceptions.
- Exception hierarchy.
- throw and throws keywords.
- Exception propagation across storage, service and controller layers.
- Exception chaining using getCause().
- Stack trace analysis.
- Time and space complexity of stack traces.

**Test Results:**
- Successful file submission – Passed.
- File exceeding 500 KB – Passed.
- Examination window closed – Passed.
- Student not registered – Passed.
- Exception chaining with original cause – Passed.
- Exception without original cause – Passed.

All executed test cases produced the expected behaviour.

### Task 03 – Resource Safety, Debugging and Robust Code
Status: Completed

- Compared manual resource closing with try-with-resources.
- Tested valid and invalid store input files.
- Identified a resource leak in manual resource management.
- Demonstrated automatic resource closing and suppressed exceptions.
- Added test screenshots and program output.
