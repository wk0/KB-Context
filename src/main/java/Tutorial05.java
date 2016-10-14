/**
 * Created by gary on 10/13/16.
 */
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

import java.io.*;

/** Tutorial 5 - read RDF XML from a file and write it to standard out
 */
public class Tutorial05 extends Object {

  /**
   NOTE that the file is loaded from the class-path and so requires that
   the data-directory, as well as the directory containing the compiled
   class, must be added to the class-path when running this and
   subsequent examples.
   */
  static final String inputFileName  = "Store/instance_types_en.nt";

  public static void main (String args[]) {
    // create an empty model
    Model model = ModelFactory.createDefaultModel();

    InputStream in = FileManager.get().open( inputFileName );
    if (in == null) {
      throw new IllegalArgumentException( "File: " + inputFileName + " not found");
    }

    // read the RDF/XML file
    model.read(in, null, "NT");

    // write it to standard out
    // model.write(System.out);
//    StmtIterator iter = model.listStatements();
//
//    // print out the predicate, subject and object of each statement
//    while (iter.hasNext()) {
//      Statement stmt      = iter.nextStatement();         // get next statement
//      Resource  subject   = stmt.getSubject();   // get the subject
//      Property  predicate = stmt.getPredicate(); // get the predicate
//      RDFNode   object    = stmt.getObject();    // get the object
//
//      System.out.print(subject.toString());
//      System.out.print(" " + predicate.toString() + " ");
//      if (object instanceof Resource) {
//        System.out.print(object.toString());
//      } else {
//        // object is a literal
//        System.out.print(" \"" + object.toString() + "\"");
//      }
//      System.out.println(" .");
//    }
  }
}