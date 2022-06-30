import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XmlArithInter {

    public static void main(String[] args) {

        XmlArithInter XAI = new XmlArithInter();

        VarList list = XAI.new VarList();

        try {

            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream("program.xml"));

            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case "var":

                            Attribute VarName = startElement.getAttributeByName(new QName("name"));
                            Var NewVar;
                            if (VarName != null) {

                                Attribute VarValue = startElement.getAttributeByName(new QName("value"));
                                if (VarValue != null)
                                    NewVar = XAI.new Var(VarName.getValue(), Integer.parseInt(VarValue.getValue()));
                                else
                                    NewVar = XAI.new Var((VarName.getValue()));

                                if (list.FindVar(NewVar.name) == -1)
                                    list.Vars.add(NewVar);
                                else
                                    throw new Exception("Error:The variable " + VarName.getValue() + " exists!:((");
                            }

                            break;
                        case "print":

                            Attribute VarPrint = startElement.getAttributeByName(new QName("n"));

                            int VarPrintIndex = list.FindVar(VarPrint.getValue());
                            if (VarPrintIndex < 0)
                                throw new Exception(
                                        "Error:The variable " + VarPrint.getValue() + " is not defined! :((");

                            System.out.println(list.Vars.get(VarPrintIndex).value);

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
        public List<Var> Vars = new ArrayList<Var>();

        public void AddOperation(String n1, String n2, String to) {

            int a, b, c, n1Index, n2Index, toIndex;
            try {
                // validates n1
                if (!isInteger(n1)) {
                    n1Index = this.FindVar(n1);
                    if (n1Index < 0)

                        throw new Exception("Error:The variable " + n1 + " is not defined! :((");
                    a = this.Vars.get(n1Index).value;
                } else
                    a = Integer.parseInt(n1);

                // validates n2
                if (!isInteger(n2)) {
                    n2Index = this.FindVar(n2);
                    if (n2Index < 0)

                        throw new Exception("Error:The variable " + n2 + " is not defined! :((");
                    b = this.Vars.get(n2Index).value;
                } else
                    b = Integer.parseInt(n2);
                // validates c
                toIndex = this.FindVar(to);
                if (toIndex < 0)

                    throw new Exception("Error:The variable " + to + " is not defined! :((");

                c = a + b;
                this.Vars.get(toIndex).value = c;
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());

            }

        }

        public int FindVar(String name) {
            for (int i = 0; i < Vars.size(); i++)

                if (Vars.get(i).name.equals(name))
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

}
