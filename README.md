# PDF-Deblanker
A JavaFx application that removes pages that are considered blank when they have a certain amount of white area.
This tool was originally created to shorten scanned documents, that had a lot of pages with few information which where actually not needed.

This tool will work with all PDFs, regardless if they contain text or contain images.
It will check the color of each pixel, and if the pixels red, green and blue value are >= 245, it will consider the pixel as white.
If a page has a certain percentage(configured on the user interface) of white pixels, it will be considered as a blank page.

You can use values like "99.1" or "99.723421" to fine tune you're results.

The results will be saved to the folder of the original PDF file in the following sub folders:
  - deblanked
  - removedPages
