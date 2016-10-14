import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.tdb.TDBFactory;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.util.Properties;

import static org.apache.jena.enhanced.BuiltinPersonalities.model;

public class App {

  public static void main(String[] args) {
    try {
      Properties props = new Properties();
      props.load(new FileInputStream("log4j.properties"));
      PropertyConfigurator.configure(props);
    } catch (Exception e) {

    }

    // String directory = "Database/TestDB";
    String directory = "Database/DB";
    System.out.print("DB init");
    Dataset dataset = TDBFactory.createDataset(directory);
    System.out.print("DB created");
    Model model = dataset.getDefaultModel() ;
    System.out.print("DB loaded");

    StmtIterator iter = model.listStatements();

    // print out the predicate, subject and object of each statement
    while (iter.hasNext()) {
      Statement stmt      = iter.nextStatement();         // get next statement
      Resource  subject   = stmt.getSubject();   // get the subject
      Property  predicate = stmt.getPredicate(); // get the predicate
      RDFNode   object    = stmt.getObject();    // get the object

      System.out.print(subject.toString());
      System.out.print(" " + predicate.toString() + " ");
      if (object instanceof Resource) {
        System.out.print(object.toString());
      } else {
        // object is a literal
        System.out.print(" \"" + object.toString() + "\"");
      }
      System.out.println(" .");
    }
  }

}