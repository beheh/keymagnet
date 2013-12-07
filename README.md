KeyMagnet
=========
A bot to grab posts from various source and extract specific strings, e.g. betakeys from text or images (using the Tesseract OCR engine). Requires json-simple, JNA, JAI IO, Tesseract (with eng language pack) and Tess4j. Built an tested on a Linux system.

If Tesseract should crash, try executing the application from a terminal with LC\_ALL=C and setting TESSDATA\_PREFIX to the parent directory of your tessdata folder.
