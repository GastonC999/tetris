# AGENTS.md

Java SE Swing Tetris game, single package, no build tool (no Maven/Gradle).

## Commands

- Build: `javac -d out/production/TetrisPortfolio src/*.java`
- Run: `java -cp out/production/TetrisPortfolio Main`
- No tests, linter, or formatter configured. Verify by compiling and doing a short GUI launch.

## Architecture (read before editing)

- No packages, default package only. All classes in `src/`.
- `Board.java` is **View + Controller**: renders, runs the `javax.swing.Timer` game loop, and handles keyboard. It is NOT pure MVC — do not expect a clean separation.
- `GameLogic.java` is the model: board matrix (`Color[][]`), collisions, line clearing, score. UI-independent.
- `PieceFactory.createRandomPiece()` builds pieces. `Board` pre-reserves a `nextPiece` and shows it in `NextPiecePanel` (side preview panel).
- `Piece.drawAt(g, cell, ox, oy)` draws a shape at free coordinates (used by the preview); `draw()` uses board coords.

## Conventions / gotchas

- Codebase and README are in **Spanish** (identifiers like `getMatriz`, `securePart`, `limpiarLineas`; comments in Spanish). Match this in new code.
- GUI is the only way to exercise the game; `JOptionPane` shows the Game Over dialog and stops the timer.
- `out/` is gitignored (IntelliJ output). `.idea/` is committed.
- Remote: `git push` goes to `https://github.com/GastonC999/tetris.git` (repo moved; the old `tetris-` URL still works with a redirect warning).
