# PDF Deblanker

Screenshot
----------
![PDF Deblanker UI](https://github.com/xresch/PDF-Deblanker/blob/master/pdfdeblanker/docs/images/pdfdeblanker_ui.jpg)

Description
-----------
A JavaFx application that removes pages that are considered blank when they have a certain amount of white area.
This tool was originally created to shorten scanned documents, that had a lot of pages with few information which where actually not needed.

This tool will work with all PDFs, regardless if they contain text or contain images from scans.
It will check the color of each pixel, and if the pixels red, green and blue value are >= 245, it will consider the pixel as white.
If a page has a certain percentage(configured on the user interface) of white pixels, it will be considered as a blank page.

You can use values for the percentage like "99.1" or "99.723421" to fine tune you're results.

The results will be saved to the folder of the original PDF file in the following sub folders:
  - deblanked
  - removedPages

Quickstart
----------
1. Install Java 1.8 or higher on your machine
2. Download the jar-File from this repository under ./pdfdeblanker/dist
3. Double click the .jar-File to start the application.
  
