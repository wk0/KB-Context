import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.PropertyConfigurator;

public class App {

  public static void main(String[] args) {
    //Gets an error with log4j, so need to manually point
    // to the jena log4j property path
    String log4jConfPath = "/home/wkelly3/Jena/apache-jena-3.1.0/jena-log4j.properties";
    PropertyConfigurator.configure(log4jConfPath);

    // Code from:
    // http://www.iandickinson.me.uk/articles/jena-eclipse-helloworld/
    Model m = ModelFactory.createDefaultModel();
    String NS = "http://example.com/test";

    Resource r = m.createResource(NS + "r");
    Property p = m.createProperty(NS + "p");

    r.addProperty(p, "hello world", XSDDatatype.XSDstring);
    m.write(System.out, "Turtle");

  }
}