# Programming in Java – Assignment 3 (CO3)

## Student Details

- **Student A:** K Suthesika
- **Register Number:** 192511070
- **Student B:** Harisha S.
- **Partner Register Number:** 192324300
- **Course Code:** UBA0920
- **Faculty:** Dr. Vijayaragan
- **Academic Year:** 2026–2027

## Repository Information

**GitHub Repository:**  
https://github.com/192511070simats-code/CSA09-192511070-Assignment3

**Java Version:** JDK 21.0.13 (Eclipse Temurin)

## Assignment Overview

This assignment covers Java exception handling, resource management, multithreading, synchronization, deadlock prevention, producer-consumer communication and concurrent fault-tolerant processing.

All seven task implementations and their available test evidence are organized into separate folders.

## Task Completion Status

| Task | Topic | Status |
|---|---|---|
| Task 01 | ATM Exception Handling | Completed |
| Task 02 | User-Defined Exceptions and Exception Chaining | Completed |
| Task 03 | Resource Safety and Debugging | Completed |
| Task 04 | Thread Creation and Thread Life Cycle | Completed |
| Task 05 | Cinema Seat Booking and Synchronization | Completed |
| Task 06 | Deadlock Prevention and Producer-Consumer | Completed |
| Task 07 | Concurrent Fault-Tolerant Processor | Completed |

## Task Descriptions and Results

### Task 01 – ATM Exception Handling

- Demonstrates checked and unchecked exceptions.
- Uses try-catch, multi-catch and finally.
- Tests invalid account numbers, invalid input, negative amounts, insufficient balance, successful withdrawal and missing files.
- Includes an alternative error-code approach and exception-handling evidence.

**Main source:** `ATMWithdrawal.java`

### Task 02 – User-Defined Exceptions and Exception Chaining

- Implements a custom exception hierarchy for an online examination portal.
- Demonstrates exception propagation from storage to controller.
- Preserves original causes using exception chaining.
- Tests closed examination windows, unregistered students and storage failures.

**Main sources:** `StorageDemo.java`, `PortalExceptionDemo.java`, `ChainingDemo.java`

### Task 03 – Resource Safety and Debugging

- Compares manual resource management with try-with-resources.
- Demonstrates a resource leak when an exception occurs.
- Tracks open readers and writers.
- Demonstrates suppressed exceptions and automatic resource closing.

**Main source:** `ReportGenerator.java`

**Note:** The resource leak was demonstrated using tracked resource counts and failing input. A separate debugger breakpoint screenshot was not captured.

### Task 04 – Thread Creation and Thread Life Cycle

- Implements threads using both the Thread class and Runnable interface.
- Demonstrates start(), run(), sleep(), join() and isAlive().
- Tests thread priorities and daemon threads.
- Records ten execution runs and compares thread completion behaviour.

**Main sources:** `ScannerStationDemo.java`, `RunVsStartDemo.java`

### Task 05 – Cinema Seat Booking and Synchronization

- Demonstrates race conditions using unsynchronized booking.
- Compares synchronized methods and synchronized blocks.
- Tests 10,000 booking attempts.
- Compares AtomicBoolean, ReentrantLock and ConcurrentHashMap approaches.

**Main sources:** `SeatBooking.java`, `AlternativeBooking.java`

### Task 06 – Deadlock Prevention and Producer-Consumer

- Demonstrates deadlock using wallet transfers.
- Detects deadlocked threads and prevents deadlock through consistent lock ordering.
- Implements a bounded producer-consumer buffer with capacity five.
- Uses synchronized methods, wait(), notifyAll() and while loops.
- Tests the incorrect use of if instead of while.

**Main source:** `Task06Demo.java`

### Task 07 – Concurrent Fault-Tolerant Processor

- Processes 10,000 expected student score files.
- Uses ExecutorService, Callable, Future and ConcurrentLinkedQueue.
- Handles valid, corrupt and missing files independently.
- Compares thread pool sizes of 1, 2, 4 and 8.

**Measured results:**

| Pool size | Successful files | Failed files | Processing time |
|---:|---:|---:|---:|
| 1 | 9,850 | 150 | 27,347.934 ms |
| 2 | 9,850 | 150 | 19,051.825 ms |
| 4 | 9,850 | 150 | 4,900.666 ms |
| 8 | 9,850 | 150 | 2,442.521 ms |

Every experiment processed 10,000 expected outcomes, and summary verification passed.

**Main source:** `SemesterResultProcessor.java`

## Repository Structure

```text
CSA09-192511070-Assignment3/
│
├── README.md
├── ASSIGNMENT – 3.docx
│
├── Task01/
│   ├── Java source files
│   ├── Input files
│   └── Test screenshots
│
├── Task02/
│   ├── Java source files
│   └── Test screenshots
│
├── Task03/
│   ├── ReportGenerator.java
│   ├── Input files
│   └── Test screenshots and output
│
├── Task04/
│   ├── ScannerStationDemo.java
│   ├── RunVsStartDemo.java
│   ├── Test screenshots
│   └── Run logs
│
├── Task05/
│   ├── SeatBooking.java
│   ├── AlternativeBooking.java
│   ├── Test screenshots
│   └── Performance logs
│
├── Task06/
│   ├── Task06Demo.java
│   ├── Task06IfExperiment.java
│   ├── Test screenshots
│   └── Output logs
│
└── Task07/
    ├── SemesterResultProcessor.java
    ├── Q7_Output.txt
    └── Test screenshots
```

## Compilation and Execution

Use JDK 21 or a compatible later JDK.

Open the required task folder in the terminal and compile the Java source file.

General commands:

```powershell
javac FileName.java
java FileName
```

Main program commands:

| Task | Compile | Run |
|---|---|---|
| Task 01 | `javac ATMWithdrawal.java` | `java ATMWithdrawal` |
| Task 02 | `javac StorageDemo.java` | `java StorageDemo` |
| Task 03 | `javac ReportGenerator.java` | `java ReportGenerator` |
| Task 04 | `javac ScannerStationDemo.java` | `java ScannerStationDemo` |
| Task 05 | `javac SeatBooking.java` | `java SeatBooking` |
| Task 06 | `javac Task06Demo.java` | `java Task06Demo` |
| Task 07 | `javac SemesterResultProcessor.java` | `java SemesterResultProcessor` |

Some tasks contain additional demonstration programs. Compile and run those separately when required.

## Testing and Evidence

Each task folder contains its Java source code and available screenshots or text logs showing program execution and test results.

The Task07 program generates its test datasets when executed. These generated files are not required to be committed to the repository.

## Pair Work Responsibility

This is a pair assignment. Both students are required to understand and independently execute all seven tasks in their respective repositories. The report identifies Student A and Student B responsibilities and includes the joint discussions.

## Conclusion

This assignment demonstrates practical Java programming concepts involving exception handling, resource safety, multithreading, synchronization and concurrent processing. All seven task implementations in this repository have been committed to GitHub with their available test evidence.