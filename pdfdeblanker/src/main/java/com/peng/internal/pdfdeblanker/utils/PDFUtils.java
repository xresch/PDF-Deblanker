package com.peng.internal.pdfdeblanker.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.peng.internal.pdfdeblanker.DeblankResults;
import com.peng.internal.pdfdeblanker.Main;

public class PDFUtils {
	
	
	public static DeblankResults deblankPDF(File pdfFile, double percentage) throws InvalidPasswordException, IOException {
		
		DeblankResults result = new DeblankResults();
		
		Main.progressModal.setMessage("Load PDF file ...");
		
	    PDDocument originalDoc = PDDocument.load(pdfFile);
	    System.out.println("Title: "+pdfFile.getName());
	    
	    result.originalPDF = originalDoc;

	    PDFRenderer renderedDoc = new PDFRenderer(originalDoc);
	    
	    result.totalPageCount = originalDoc.getNumberOfPages();
	    
	    double initialTotalProgress = Main.progressModal.getTotalProgress();
	    for (int pageIndex = 0; pageIndex <  result.totalPageCount; pageIndex++) {
	    	BufferedImage rendered = renderedDoc.renderImage(pageIndex);
	        
	    	if(!PDFUtils.isPageBlank(rendered, percentage) ) {
	        	result.deblankedPDF.addPage(originalDoc.getPage(pageIndex));
	        	result.resultingPageCount++;
	        }else {
	        	result.removedPagesPDF.addPage(originalDoc.getPage(pageIndex));
	        	Main.progressModal.setMessage("Removed Page Number " + (pageIndex + 1));
	            result.removedPageCount++;
	        }
	    	
	    	float progress = ((float)pageIndex+1) /result.totalPageCount;
	    	Main.progressModal.setCurrentFileProgress( progress );
	    	Main.progressModal.setTotalProgress(initialTotalProgress + (progress / Main.progressModal.getTotalFiles()));
	    }
	    
		return result;
	}
	
	public static Boolean isPageBlank(BufferedImage pageImage, double whitePercentage) throws IOException {
	    BufferedImage image = pageImage;
	    long count = 0;
	    int height = image.getHeight();
	    int width = image.getWidth();
	    Double areaFactor = (width * height) * whitePercentage / 100;

	    for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	            Color c = new Color(image.getRGB(x, y));
	            if (c.getRed() >= 245 && c.getGreen() >= 245 && c.getBlue() >= 245) {
	                count++;
	            }
	        }
	    }
	    if (count >= areaFactor) {
	        return true;
	    }
	    return false;
	}

}
