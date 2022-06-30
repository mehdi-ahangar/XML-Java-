import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XmlDocumentInversion {

    public static void main(String[] args) {

        List<String> Tags = new ArrayList<>();
        try {

            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream("program.xml"));

            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    Tags.add(startElement.getName().toString());

                }
                if (nextEvent.isEndElement()) {

                    String PrintLine = "", Margin = "";
                    for (int i = Tags.size() - 1; i >= 0; i--) {
                        Margin = Margin + "   ";
                        if (i != 0)
                            PrintLine = Margin + "  <" + Tags.get(i).toString() + ">";
                        else
                            PrintLine = Margin + "  <" + Tags.get(i).toString() + "/>";

                        System.out.println(PrintLine);
                    }
                    for (int i = 1; i < Tags.size(); i++) {
                        Margin = Margin.substring(0, Margin.length() - 4);
                        PrintLine = Margin + "  </" + Tags.get(i).toString() + ">";
                        System.out.println(PrintLine);
                    }

                    return;

                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
