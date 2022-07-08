import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.junit.Test;

public class XmlDocumentInversion {

    public static void main(String[] args) {

        ArrayList<ArrayList<String>> tags = new ArrayList<ArrayList<String>>();
        try {

            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream("input.xml"));

            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    tags.add(new ArrayList<>());
                    tags.get(tags.size() - 1).add(startElement.getName().toString());

                    Iterator<Attribute> iterator = startElement.getAttributes();
                    while (iterator.hasNext()) {
                        Attribute attribute = iterator.next();
                        tags.get(tags.size() - 1).add(attribute.toString());
                    }

                }
            }
            String printLine = "", margin = "";
            for (int i = tags.size() - 1; i >= 0; i--) {
                margin = margin + "    ";
                printLine = margin + "<";
                for (int j = 0; j < tags.get(i).size(); j++)
                    if (j != 0)
                        printLine = printLine + "  " + tags.get(i).get(j).toString();
                    else
                        printLine = printLine + tags.get(i).get(j).toString();

                if (i != 0)
                    printLine = printLine + ">";
                else
                    printLine = printLine + "/>";

                System.out.println(printLine);
            }
            for (int i = 1; i < tags.size(); i++) {
                margin = margin.substring(0, margin.length() - 4);
                printLine = margin + "  </" + tags.get(i).get(0).toString() + ">";
                System.out.println(printLine);
            }

            return;
        } catch (Exception e) {
            System.out.println("Error in parsing the XML file :(");
        }

    }
    @Test
    public void testXmlDocumentInversionMain() throws IOException {
        
        String[] args = null;
        System.setOut(new PrintStream(new FileOutputStream("output")));
        System.setErr(new PrintStream(new FileOutputStream("error")));
        System.setIn(new FileInputStream("input.xml"));
        XmlDocumentInversion.main(args);
        
    }

}
