
# Programming in Java - Assignment 3 (CO3)

## Student Details

- **Student A:** K Suthesika
- **Register Number:** 192511070
- **Student B:** Harisha S.
- **Partner Register Number:** 192324300
- **Course Code:** UBA0920
- **Faculty:** Dr Vijayaragavan
- **Java Version:** JDK 21.0.13 (Temurin)
- **Academic Year:** 2026–2027

## GitHub Repository

[CSA09-192511070-Assignment3](https://github.com/192511070simats-code/CSA09-192511070-Assignment3)

## Assignment Tasks

| Task | Topic | Status |
|---|---|---|
| Task 01 | ATM Exception Handling | Completed - 6 tests executed |
| Task 02 | User-Defined Exceptions and Propagation | Completed |
| Task 03 | Resource Safety and Debugging | Completed - debugger screenshot not captured |
| Task 04 | Thread Creation and Thread Life Cycle | Completed |
| Task 05 | Race Conditions and Synchronization | Completed |
| Task 06 | Deadlock and Producer-Consumer | Completed |
| Task 07 | Concurrent Fault-Tolerant Processor | Pending |

## Compile and Run

Open the relevant task folder in the terminal.

Compile a Java program:

```powershell
javac FileName.java
```

Run the program:

```powershell
java FileName
```

For programs containing multiple Java files, compile the required source files together or compile the main Java file if the other classes are in the same source file.

---

## Task 01 – ATM Exception Handling

**Status:** Completed

**Program:**
- `ATMWithdrawal.java` – ATM withdrawal system using exception handling.
- `UncaughtDemo.java` – Demonstrates an uncaught exception and stack trace.
- `CatchOrderDemo.java` – Demonstrates incorrect catch-block ordering.
- `ATMErrorCodeDemo.java` – Demonstrates an error-code alternative.

**Concepts Covered:**
- Java exception hierarchy.
- Checked and unchecked exceptions.
- User-defined exceptions.
- try, catch and finally.
- Multi-catch.
- Exception propagation.
- Catch-block ordering.
- Stack trace analysis.
- Exception handling versus error-code approach.

**Test Results:**
1. Invalid account number.
2. Invalid withdrawal input.
3. Negative withdrawal amount.
4. Insufficient account balance.
5. Successful withdrawal.
6. Missing card-reader file.

All six test conditions were executed.

**Evidence:**
- Six ATM test screenshots.
- Uncaught exception stack trace.
- Catch-order compilation error.
- Error-code alternative output.

---

## Task 02 – User-Defined Exceptions and Exception Propagation

**Status:** Completed

**Programs:**
- `StorageDemo.java` – Custom storage exception and exception chaining.
- `PortalExceptionDemo.java` – Examination portal failure scenarios.
- `ChainingDemo.java` – Demonstrates `getCause()` and chained stack traces.

**Concepts Covered:**
- User-defined checked and unchecked exceptions.
- Exception hierarchy.
- `throw` and `throws` keywords.
- Exception propagation across storage, service and controller layers.
- Exception chaining using `getCause()`.
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

**Evidence:**
- Storage exception output.
- Three examination portal scenarios.
- Exception chaining output and stack trace.

---

## Task 03 – Resource Safety, Debugging and Robust Code

**Status:** Completed

**Program:**
- `ReportGenerator.java` – Compares manual resource management with try-with-resources.

**Input Files:**
- `store1.txt`
- `store2.txt`
- `store3.txt`
- `store-fail.txt`

**Concepts Covered:**
- Manual try-catch-finally resource management.
- Try-with-resources.
- `AutoCloseable`.
- Resource leaks.
- Automatic resource closing.
- Reverse-order resource closing.
- Suppressed exceptions.
- `getSuppressed()`.

**Test Results:**
- Manual version with valid input – Passed.
- Try-with-resources with valid input – Passed.
- Manual version with invalid input – Resource leak detected.
- Try-with-resources with invalid input – Resources closed automatically.
- Suppressed exception demonstration – Passed.

The manual implementation left one tracked reader open when invalid input caused an exception. The try-with-resources implementation closed the reader and writer automatically.

**Evidence:**
- Manual and try-with-resources comparison screenshot.
- Suppressed exception screenshot.
- Program output showing open-resource counts.

**Note:** The resource leak was investigated using program output and resource counters. A separate debugger breakpoint screenshot was not captured.

---

## Task 04 – Thread Creation and the Thread Life Cycle

**Status:** Completed

**Programs:**
- `ScannerStationDemo.java` – Implements scanner stations using Thread and Runnable.
- `RunVsStartDemo.java` – Demonstrates the difference between `run()` and `start()`.

**Concepts Covered:**
- Thread creation and life cycle.
- Extending the Thread class.
- Implementing the Runnable interface.
- `start()`, `run()`, `sleep()`, `join()` and `isAlive()`.
- Thread priorities.
- Daemon threads.
- AtomicInteger for thread-safe counting.
- ExecutorService as an alternative approach.

**Test Results:**
- Thread implementation – Passed.
- Runnable implementation – Passed.
- `run()` versus `start()` – Passed.
- Ten execution runs – Passed; total of 45 scripts in each run.
- Thread priority experiment – Passed.
- Daemon thread experiment – Passed.

The ten execution runs produced the expected total of 45 scripts. The daemon experiment demonstrated that the JVM can exit while a daemon thread is still running.

**Evidence:**
- Thread and Runnable output screenshots.
- `run()` and `start()` comparison screenshot.
- Priority experiment screenshots and execution log.
- Daemon experiment screenshot and execution log.
- Ten-run screenshots and execution log.

---

## Task 05 – Race Conditions in a Cinema Seat-Booking System

**Status:** Completed

**Programs:**
- `SeatBooking.java` – Demonstrates unsynchronized booking and synchronized method/block approaches.
- `AlternativeBooking.java` – Demonstrates alternative thread-safe booking strategies.

**Concepts Covered:**
- Race conditions.
- Critical sections.
- Check-then-act problems.
- Unsynchronized shared data.
- Synchronized methods.
- Synchronized blocks.
- Intrinsic locks and object monitors.
- Static synchronization.
- AtomicBoolean.
- ReentrantLock.
- ConcurrentHashMap.
- Thread-safe counters.

**Test Results:**

**Unsynchronized Booking:**
- Executed 10 repeated runs.
- All 10 runs demonstrated extra bookings.
- Expected bookings: 10.
- Actual reported bookings ranged from 33 to 61.

**Synchronized Booking:**
- Synchronized method – 10 seats sold.
- Synchronized block – 10 seats sold.
- Both approaches prevented double-booking in the correctness test.

**Performance Experiment:**

| Run | Synchronized Method | Synchronized Block |
|---|---:|---:|
| 1 | 4 ms | 2 ms |
| 2 | 3 ms | 2 ms |
| 3 | 6 ms | 3 ms |
| 4 | 4 ms | 3 ms |
| 5 | 4 ms | 3 ms |
| **Average** | **4.2 ms** | **2.6 ms** |

**Alternative Approaches:**

| Approach | Average Execution Time |
|---|---:|
| AtomicBoolean array | 62.036 ms |
| ReentrantLock | 23.117 ms |
| ConcurrentHashMap | 35.878 ms |

All three alternative approaches successfully booked 1,000 unique seats in the five-run experiment.

**Evidence:**
- Unsynchronized booking experiment screenshots.
- Synchronized method and block screenshot.
- Performance experiment screenshots and log.
- Alternative approach screenshots and execution log.

---

## Task 06 – Deadlock and Producer-Consumer

**Status:** Completed

**Program:**
- `Task06Demo.java` – Demonstrates wallet deadlock, safe lock ordering, and a bounded producer-consumer token counter.

**Concepts Covered:**
- Deadlock and the four necessary conditions.
- Deadlock detection using ThreadMXBean.
- Lock ordering and deadlock prevention.
- Synchronized methods.
- `wait()` and `notifyAll()`.
- Producer-consumer coordination.
- Bounded circular buffer.
- `while` versus `if` condition experiment.
- `sleep()` versus `wait()`.
- ArrayBlockingQueue and tryLock alternatives.

**Test Results:**
- Unsafe wallet transfers – Two deadlocked threads detected.
- Safe wallet transfers – Completed without deadlock; total balance 2000.
- Producer-consumer – 20 tokens collected successfully.
- Buffer minimum size – 0.
- Buffer maximum size – 5.
- Total collected sum – 210.
- Incorrect `if` experiment – Negative buffer count and incorrect total demonstrated.

**Evidence:**
- Deadlock detection screenshot.
- Safe wallet transfer screenshot.
- Producer-consumer screenshots.
- `Q6_Correct_Output.txt`.
- `Q6_If_Experiment.txt`.
- Incorrect `if` experiment screenshots.

## Task 07 – Concurrent Fault-Tolerant Processor

**Status:** Pending

---

## Repository Structure

```text
Java_Assignment3/
│
├── Task01/
├── Task02/
├── Task03/
├── Task04/
├── Task05/
├── Task06/
├── Task07/
│
├── ASSIGNMENT – 3.docx
└── README.md
```

## General Notes

- Each task contains its Java source files and relevant test evidence.
- Screenshots and experimental output logs are included where applicable.
- Git commits are used to maintain progressive development history.
- This repository documents Student A's execution. Student B must also execute every assignment task independently in their own repository, as required by the faculty instructions.
