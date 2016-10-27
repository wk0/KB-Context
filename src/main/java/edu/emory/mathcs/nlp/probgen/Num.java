import org.apache.jena.base.Sys;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.*;
import org.apache.jena.tdb.TDBFactory;
import org.apache.log4j.PropertyConfigurator;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Num {

  public static void main(String[] args) {
    try {
      Properties props = new Properties();
      props.load(new FileInputStream("log4j.properties"));
      PropertyConfigurator.configure(props);
    } catch (Exception e) {

    }

    String numDirectory = "Database/specific_mappingbased_properties_en";
    Dataset numDataset = TDBFactory.createDataset(numDirectory);
    Model numData = numDataset.getDefaultModel();

    StmtIterator iter = numData.listStatements();

    // print out the predicate, subject and object of each statement
    while (iter.hasNext()) {
      Statement stmt      = iter.nextStatement();  // get next statement
      Resource  subject   = stmt.getSubject();     // get the subject
      Property  predicate = stmt.getPredicate();   // get the predicate
      RDFNode object    = stmt.getObject();      // get the object

      System.out.print("Resources: ");
      System.out.print(subject.toString());
      System.out.print("\n");
      System.out.print("Property: ");
      System.out.print(predicate.toString());
      System.out.print("\n");

      if (object instanceof Resource) {

      } else {
        String line = object.toString();
        String pattern = "(\\d+.\\d+|\\d+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
          float f = Float.parseFloat(m.group(0));
          System.out.println("Found value: " + f );
          System.out.print("\n");
        }else {
          System.out.println("NO MATCH");
        }
      }

    }
}
}
