---
name: run-tests
description: Use when the user asks to run tests, run the test suite, verify the build, or check that the Tetris game compiles and launches. Covers compiling with javac and launching the GUI briefly to smoke-test.
---

# Run Tests (TetrisPortfolio)

This project has **no automated test framework** (no JUnit, Maven or Gradle).
"Running the tests" means verifying the game compiles and launches correctly.

## Steps

1. **Compile**
   ```bash
   javac -d out/production/TetrisPortfolio src/*.java
   ```
   Fail fast if javac reports errors — do not proceed to the GUI launch.

2. **Smoke-launch the GUI** (short run to confirm it starts without exceptions)
   ```bash
   java -cp out/production/TetrisPortfolio Main
   ```
   - Let it run ~2 seconds, then terminate it.
   - Any exception or stack trace printed to stdout/stderr means the smoke test failed.

3. **Report**
   - On success, report the result concisely (compilation clean, GUI launched without errors).
   - On failure, quote the relevant compiler/runtime error and the file/line that caused it.

## Notes

- GUI is the only way to exercise the game; `JOptionPane` shows the Game Over dialog and stops the timer.
- The game loop is driven by `javax.swing.Timer` in `Board.java`; `GameLogic.java` is the UI-independent model.
- Do not invent test commands that do not exist (e.g. `mvn test`, `gradle test`). Use the javac + GUI launch flow above.
