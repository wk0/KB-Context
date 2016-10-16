import org.apache.jena.base.Sys;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.*;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.TDBLoader;
import org.apache.log4j.PropertyConfigurator;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.vocabulary.RDF;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;


public class App {

  public static void main(String[] args) {
    try {
      Properties props = new Properties();
      props.load(new FileInputStream("log4j.properties"));
      PropertyConfigurator.configure(props);
    } catch (Exception e) {

    }

    String schema_directory = "Database/dbpedia_2014";
    Dataset schema_dataset = TDBFactory.createDataset(schema_directory);
    Model schema = schema_dataset.getDefaultModel() ;
    Resource schemaResource = schema.getResource("http://dbpedia.org/ontology/Person");
    StmtIterator schemaIter = schemaResource.listProperties();

    while (schemaIter.hasNext()) {
      Statement stmt      = schemaIter.nextStatement();  // get next statement
      System.out.println(stmt);
    }

    String type_directory = "Database/instance_types_en";
    Dataset type_dataset = TDBFactory.createDataset(type_directory);
    Model typeData = type_dataset.getDefaultModel();




    String fact_directory = "Database/mappingbased_properties_en";
    Dataset fact_dataset = TDBFactory.createDataset(fact_directory);
    Model factData = fact_dataset.getDefaultModel();
    Resource factResources = factData.getResource("http://dbpedia.org/ontology/Person");

//    System.out.print(factData);
    //Builds type based model
//    Reasoner typeReasoner = ReasonerRegistry.getRDFSReasoner();
//    typeReasoner = typeReasoner.bindSchema(schema);
//    InfModel typeModel = ModelFactory.createInfModel(typeReasoner, typeData);

//    Reasoner factReasoner = ReasonerRegistry.getRDFSReasoner();
//    factReasoner = factReasoner.bindSchema(schema);
//    InfModel factModel = ModelFactory.createInfModel(factReasoner, factData);
//
//    Resource abe = typeModel.getResource("http://dbpedia.org/resource/Abraham_Lincoln");
//    Resource person = typeModel.getResource("http://dbpedia.org/ontology/Person");
//    Property birthPlace = factModel.getProperty("http://dbpedia.org/ontology/birthPlace");

//    Object bool = null;
//    Selector personSelector = new SimpleSelector(person, birthPlace, bool);
//    Model persons = factModel.query(personSelector);

  }

}
