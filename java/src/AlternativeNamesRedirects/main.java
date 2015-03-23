package xonder_VINF;

import javax.swing.JFrame;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class main {

	public static void main(String[] args) {
		MainGui GUI = new MainGui();
        GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GUI.setVisible(true);
		
		
	}

}
