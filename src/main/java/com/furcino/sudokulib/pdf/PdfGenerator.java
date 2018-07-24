/*
 * Copyright 2018 Martin Furek
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.furcino.sudokulib.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.furcino.sudokulib.models.Sudoku;

/**
 * The Class PdfGenerator.
 *
 * @author Martin Furek
 */
public class PdfGenerator {

	/** The font. */
	private static PDFont font = PDType1Font.HELVETICA_BOLD;

	/** The solution size. */
	private static float SOLUTION_SIZE = 150;

	/**
	 * Generate sudoku pdf.
	 *
	 * @param title the title
	 * @param file the file
	 * @param sudokus the sudokus
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateSudokuPdf(String title, String file, List<Sudoku> sudokus) throws IOException {
	    generateSudokuPdf(title, file, 200, 50, 50, sudokus);
    }

    /**
     * Generate sudoku pdf.
     *
     * @param title the title
     * @param file the file
     * @param boxWidth the box width
     * @param sudokus the sudokus
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void generateSudokuPdf(String title, String file, float boxWidth, List<Sudoku> sudokus) throws IOException {
        generateSudokuPdf(title, file, boxWidth, 50, 50, sudokus);
    }

	/**
	 * Generate sudoku pdf.
	 *
	 * @param title the title
	 * @param file the file
	 * @param boxWidth the box width
	 * @param margin the margin
	 * @param offset the offset
	 * @param sudokus the sudokus
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateSudokuPdf(String title, String file, float boxWidth, float margin, float offset, List<Sudoku> sudokus) throws IOException {
		if (sudokus == null || sudokus.size() <= 0) {
		    throw new IOException("Can not write file without input");
        }

        // create document
	    PDDocument doc = new PDDocument();

		// print sudokus
        printSudokus(doc, title, boxWidth, offset, margin, sudokus);

        // get solutions
        List<Sudoku> solutions = new ArrayList<>();
        for (Sudoku sudoku : sudokus) {
            solutions.add(sudoku.getSolution());
        }

        // print solutions
        printSudokus(doc, title + " (Solutions)", SOLUTION_SIZE, offset, margin, solutions);

		doc.save(file);
	}

	/**
	 * Prints the sudokus.
	 *
	 * @param doc the doc
	 * @param title the title
	 * @param boxWidth the box width
	 * @param offset the offset
	 * @param margin the margin
	 * @param sudokus the sudokus
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void printSudokus(PDDocument doc, String title, float boxWidth, float offset, float margin, List<Sudoku> sudokus) throws IOException {
        // create first page
        PDPage page = new PDPage();
        doc.addPage(page);
        page.setResources(new PDResources());
        PDPageContentStream pcs = new PDPageContentStream(doc, page);

        int index = 1;
        int row = 1;
        int col = 1;
        float pageHeight = page.getMediaBox().getHeight();
        float pageWidth = page.getMediaBox().getWidth();

        int numberOfRows = calculateNumberOfRows(boxWidth, pageHeight, offset, margin);
        int numberOfCols = calculateNumberOfCols(boxWidth, pageWidth, margin);

        if (numberOfRows == 0 || numberOfCols == 0) {
        	pcs.close();
            throw new IOException("Wrong parameters");
        }

        printTitle(title, margin, offset, pageWidth, pageHeight, pcs);

        for (Sudoku sudoku : sudokus) {
            // if overflow, create next page
            if (row > numberOfRows) {
                row = 1;
                pcs.close();
                page = new PDPage();
                doc.addPage(page);
                page.setResources(new PDResources());
                pcs = new PDPageContentStream(doc, page);
                printTitle(title, margin, offset, pageWidth, pageHeight, pcs);
            }

            // draw sudoku
            drawSudoku(index, calculateColX(col, numberOfCols, boxWidth, pageWidth, margin), calculateRowY(row, numberOfRows, boxWidth,pageHeight,offset,margin), boxWidth, sudoku, pcs);

            // next col
            index++;
            col++;
            if (col > numberOfCols) {
                col = 1;
                row++;
            }
        }
        pcs.close();
    }

	/**
	 * Prints the title.
	 *
	 * @param title the title
	 * @param margin the margin
	 * @param offset the offset
	 * @param pageWidth the page width
	 * @param pageHeight the page height
	 * @param stream the stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void printTitle(String title, float margin, float offset, float pageWidth, float pageHeight, PDPageContentStream stream) throws IOException {
        float fontSize = offset/2;
        float fontWidth = font.getStringWidth(title) / 1000 * fontSize;
        float fontHeight = font.getFontDescriptor().getCapHeight() / 1000 * fontSize;
        float x = (pageWidth/2) - (fontWidth/2);
        float y = pageHeight - margin - fontHeight/2;

        stream.beginText();
        stream.setFont(font, fontSize);
        stream.newLineAtOffset(x,  y);
        stream.showText(title);
        stream.endText();
    }

	/**
	 * Draw sudoku.
	 *
	 * @param index the index
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param sudoku the sudoku
	 * @param stream the stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("deprecation")
	private static void drawSudoku(int index, float x, float y, float width, Sudoku sudoku, PDPageContentStream stream) throws IOException {
		float cellWidth = width/Sudoku.N;
		float fontSize = cellWidth * 2/3;
        float normalLineWidth = fontSize / 20;
        float boldLineWidth = normalLineWidth * 2;

        // print index
        String indexString = ((Integer) index).toString() + ".";
        float fontWidth = font.getStringWidth(indexString) / 1000 * fontSize;
        float fontHeight = font.getFontDescriptor().getCapHeight() / 1000 * fontSize;
        stream.beginText();
        stream.setFont(font, fontSize);
        stream.newLineAtOffset(x + (width/2 - fontWidth/2), y + fontHeight/4);
        stream.showText(indexString);
        stream.endText();

        // print sudoku
        for (int i = 0; i < Sudoku.N; i++) {
			// draw vertical
            if (i % 3 == 0){
                stream.setLineWidth(boldLineWidth);
            } else {
                stream.setLineWidth(normalLineWidth);
            }
			stream.drawLine(x + i * cellWidth, y, x + i * cellWidth, y - width);

			for (int j = 0; j < Sudoku.N; j++) {
				// draw horizontal
				if (i == 0) {
                    if (j % 3 == 0){
                        stream.setLineWidth(boldLineWidth);
                    } else {
                        stream.setLineWidth(normalLineWidth);
                    }
					stream.drawLine(x, y - j * cellWidth, x + width, y - j * cellWidth);
				}
				// print number
				int value = sudoku.getCellValue(i, j);
                fontWidth = font.getStringWidth(((Integer) value).toString()) / 1000 * fontSize;
                fontHeight = font.getFontDescriptor().getCapHeight() / 1000 * fontSize;
                float fontPaddingHeight = (cellWidth - fontHeight)/2;
                float fontPaddingWidth = (cellWidth - fontWidth)/2;
				if (value != 0) {
					stream.beginText();
					stream.setFont(font, fontSize);
					stream.newLineAtOffset(x + j * cellWidth + fontPaddingWidth, y - i * cellWidth - fontPaddingHeight - fontHeight);
					stream.showText(((Integer) value).toString());
					stream.endText();
				}
			}
		}

        stream.setLineWidth(boldLineWidth);
		// draw last vertical
		stream.drawLine(x + width, y, x + width, y - width);
		// draw last horizontal
		stream.drawLine(x , y - width, x + width, y - width);
	}

	/**
	 * Calculate number of rows.
	 *
	 * @param boxWidth the box width
	 * @param pageHeight the page height
	 * @param offset the offset
	 * @param margin the margin
	 * @return the int
	 */
	private static int calculateNumberOfRows(float boxWidth, float pageHeight, float offset, float margin) {
	    int numberOfRows = 0;
	    numberOfRows = (int) Math.floor((pageHeight - 2 * margin - offset) / boxWidth);
	    return numberOfRows;
    }

    /**
     * Calculate number of cols.
     *
     * @param boxWidth the box width
     * @param pageWidth the page width
     * @param margin the margin
     * @return the int
     */
    private static int calculateNumberOfCols(float boxWidth, float pageWidth, float margin) {
        int numberOfRows = 0;
        numberOfRows = (int) Math.floor((pageWidth - 2 * margin) / boxWidth);
        return numberOfRows;
    }

    /**
     * Calculate row Y.
     *
     * @param row the row
     * @param numOfRows the num of rows
     * @param boxWidth the box width
     * @param pageHeight the page height
     * @param offset the offset
     * @param margin the margin
     * @return the float
     */
    private static float calculateRowY(int row, int numOfRows, float boxWidth, float pageHeight, float offset, float margin) {
        float rowY = 0f;

        if (numOfRows < row){
            return rowY;
        }

        if (numOfRows == 1) {
            rowY = pageHeight - ((pageHeight - boxWidth) / 2);
        }

        if (numOfRows > 1) {
            float boxPadding = (pageHeight - 2 * margin - offset - numOfRows * boxWidth) / (numOfRows - 1);
            rowY = pageHeight - margin - offset - ((row - 1) * (boxWidth + boxPadding));
        }

        return rowY;
    }

    /**
     * Calculate col X.
     *
     * @param col the col
     * @param numOfCols the num of cols
     * @param boxWidth the box width
     * @param pageWidth the page width
     * @param margin the margin
     * @return the float
     */
    private static float calculateColX(int col, int numOfCols, float boxWidth, float pageWidth, float margin) {
        float colX = 0f;

        if (numOfCols < col){
            return colX;
        }

        if (numOfCols == 1) {
            colX = (pageWidth - boxWidth) / 2;
        }

        if (numOfCols > 1) {
            float boxPadding = (pageWidth - 2 * margin - numOfCols * boxWidth) / (numOfCols - 1);
            colX = margin + ((col - 1) * (boxWidth + boxPadding));
        }

        return colX;
    }
}
