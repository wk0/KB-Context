import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.util.Properties;

public class App {

  public static void main(String[] args) {
    try {
      Properties props = new Properties();
      props.load(new FileInputStream("log4j.properties"));
      PropertyConfigurator.configure(props);
    } catch (Exception e) {

    }
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