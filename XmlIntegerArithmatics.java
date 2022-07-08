import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.junit.Test;


public class XmlIntegerArithmatics {

    public static void main(String[] args) {

        XmlIntegerArithmatics XAI = new XmlIntegerArithmatics();

        VarList list = XAI.new VarList();

        try {

            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream("input.xml"));

            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case "var":

                            Attribute varName = startElement.getAttributeByName(new QName("name"));
                            Var newVar;
                            if (varName != null) {

                                Attribute varValue = startElement.getAttributeByName(new QName("value"));
                                if (varValue != null)
                                    newVar = XAI.new Var(varName.getValue(), Integer.parseInt(varValue.getValue()));
                                else
                                    newVar = XAI.new Var((varName.getValue()));

                                if (list.findVar(newVar.name) == -1)

                                    list.vars.add(newVar);
                                else
                                    throw new Exception("Error:The variable " + varName.getValue() + " exists!:((");
                            }

                            break;
                        case "print":

                            Attribute VarPrint = startElement.getAttributeByName(new QName("n"));

                            int VarPrintIndex = list.findVar(VarPrint.getValue());
                            if (VarPrintIndex < 0)
                                throw new Exception(
                                        "Error:The variable " + VarPrint.getValue() + " is not defined! :((");

                            System.out.println(list.vars.get(VarPrintIndex).value);

                            break;
                        case "add":

                            Attribute n1 = startElement.getAttributeByName(new QName("n1"));
                            Attribute n2 = startElement.getAttributeByName(new QName("n2"));
                            Attribute to = startElement.getAttributeByName(new QName("to"));

                            list.AddOperation(n1.getValue(), n2.getValue(), to.getValue());

                            break;

                    }
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public class Var {
        public String name;
        public int value;

        public Var(String n, int v) {
            this.name = n;
            this.value = v;
        }

        public Var(String n) {
            this.name = n;
        }

    }

    public class VarList {
        public List<Var> vars = new ArrayList<Var>();

        public void AddOperation(String n1, String n2, String to) {

            int a, b, c, n1Index, n2Index, toIndex;
            try {
                // validates n1
                if (!isInteger(n1)) {
                    n1Index = this.findVar(n1);
                    if (n1Index < 0)

                        throw new Exception("Error:The variable " + n1 + " is not defined! :((");
                    a = this.vars.get(n1Index).value;
                } else
                    a = Integer.parseInt(n1);

                // validates n2
                if (!isInteger(n2)) {
                    n2Index = this.findVar(n2);
                    if (n2Index < 0)

                        throw new Exception("Error:The variable " + n2 + " is not defined! :((");
                    b = this.vars.get(n2Index).value;
                } else
                    b = Integer.parseInt(n2);
                // validates c
                toIndex = this.findVar(to);
                if (toIndex < 0)

                    throw new Exception("Error:The variable " + to + " is not defined! :((");

                c = a + b;
                this.vars.get(toIndex).value = c;
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());

            }

        }

        public int findVar(String name) {
            for (int i = 0; i < vars.size(); i++)

                if (vars.get(i).name.equals(name))
                    return i;

            return -1;
        }

        public static boolean isInteger(String strNum) {
            if (strNum == null) {
                return false;
            }
            try {
                int Num = Integer.parseInt(strNum);
            } catch (NumberFormatException nfe) {
                return false;
            }
            return true;
        }

    }
    @Test
    public void testXmlIntegerArithmaticsMain() throws IOException {
        
        String[] args = null;
        System.setOut(new PrintStream(new FileOutputStream("output")));
        System.setErr(new PrintStream(new FileOutputStream("error")));
        System.setIn(new FileInputStream("input.xml"));
        XmlIntegerArithmatics.main(args);
        
    }
}
