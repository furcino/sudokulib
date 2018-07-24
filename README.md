# SudokuLib

This library contains classes to solve and generate Sudokus. 

# Usage
## Sudoku

Sudokus can be loaded from a string, array or copied from another sudoku object by passing the argument to the constructor.

## SudokuGenerator

SudokuGenerator generates different difficulty levels of puzzles: 

```
int numberOfPuzzles = 10;
SudokuGenerator generator = new SudokuGenerator();
generator.generate(numberOfPuzzles, SudokuDifficulty.HARD);
List<Sudoku> results = generator.getSudokus();
```

## SudokuSolvers

SudokuSolvers solve puzzles and create solutions:

```
SudokuPossibilityBasedSolver solver = new SudokuPossibilityBasedSolver(sudoku);
int numberOfSolutions = solver.solve();
Sudoku solution = solver.getFirstSolution();
```

## PdfGenerator

PdfGenerator generates a PDF file with sudokus and their solutions.

```
SudokuFactory generator = new SudokuFactory();
List<Sudoku> sudokus = generator.generate(36, SudokuDifficulty.EASY);
		
try {
PdfGenerator.generateSudokuPdf("Furcino.com Sudokus","easySudokus.pdf", sudokus);
} catch (IOException e) {
	fail("Could not generate PDF");
}
```