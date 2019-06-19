package com.peng.internal.pdfdeblanker;

import org.apache.pdfbox.pdmodel.PDDocument;

public class DeblankResults {

	public PDDocument originalPDF;
	public PDDocument deblankedPDF = new PDDocument();
	public PDDocument removedPagesPDF = new PDDocument();
	public int totalPageCount = 0;
	public int resultingPageCount = 0;
	public int removedPageCount = 0;
	
}
