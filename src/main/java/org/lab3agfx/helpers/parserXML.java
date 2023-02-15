package org.lab3agfx.helpers;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


import java.io.IOException;



// source: https://howtodoinjava.com/java/xml/jdom2-read-parse-xml-examples/#project-folders
// Reason for using SAX instead of DOM parser: https://stackoverflow.com/a/19154095
// Since it is overall faster, I should try to use the SAX Parser almost every time


public class parserXML {

    public static Document parseXML(String filepath) {
       SAXBuilder builder = new SAXBuilder();
       Document document = null;

        try {
            document = (Document) builder.build(filepath);
        } catch (JDOMException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return document;

    }



}
