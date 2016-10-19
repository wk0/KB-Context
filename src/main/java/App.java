import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.*;
import org.apache.jena.tdb.TDBFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.vocabulary.RDF;
import java.io.FileInputStream;
import java.util.Properties;


public class App {

  public static void main(String[] args) {
    System.out.println("Starting!");

    try {
      Properties props = new Properties();
      props.load(new FileInputStream("log4j.properties"));
      PropertyConfigurator.configure(props);
    } catch (Exception e) {

    }

    String schemaDirectory = "Database/dbpedia_2014";
    Dataset schemaDataset = TDBFactory.createDataset(schemaDirectory);
    Model dbpediaSchema = schemaDataset.getDefaultModel() ;

    String typeDirectory = "Database/instance_types_en";
    Dataset typeDataset = TDBFactory.createDataset(typeDirectory);
    Model typeData = typeDataset.getDefaultModel();

    String factDirectory = "Database/mappingbased_properties_en";
    Dataset factDataset = TDBFactory.createDataset(factDirectory);
    Model factData = factDataset.getDefaultModel();

    String numDirectory = "Database/specific_mappingbased_properties_en";
    Dataset numDataset = TDBFactory.createDataset(numDirectory);
    Model numData = numDataset.getDefaultModel();


    //combine models
    Model combinedDataModel = ModelFactory.createUnion(factData, numData);
    Model dbpediaBaseModel = ModelFactory.createUnion(combinedDataModel, typeData);

    //Build combined inference model
    Reasoner dbpediaReasoner = ReasonerRegistry.getRDFSReasoner();
    dbpediaReasoner = dbpediaReasoner.bindSchema(dbpediaSchema);
    InfModel dbpediaInfModel = ModelFactory.createInfModel(dbpediaReasoner, dbpediaBaseModel);


	//Validate the model
    /*
    /System.out.println("dbpediaInfModel");
    validateModel(dbpediaInfModel);
    System.out.println();
    */



    //Two resource types needed. Keep note of resource-ontology diff
    //Property country = dbpediaInfModel.getProperty("http://dbpedia.org/ontology/country");
    //Property birthPlace = dbpediaInfModel.getProperty("http://dbpedia.org/ontology/birthPlace");

    /*
    Resource abe = dbpediaInfModel.getResource("http://dbpedia.org/resource/Abraham_Lincoln");
    Resource person = dbpediaInfModel.getResource("http://dbpedia.org/ontology/Person");
    Resource abe_home;


    // If Abraham Lincoln is type Person -> Is Abraham Lincoln a person?
    if (dbpediaInfModel.contains(abe, RDF.type, person)) {
      System.out.println("Abraham Lincoln is a person.");

      if (dbpediaInfModel.contains(abe, birthPlace, (RDFNode) null)){
        NodeIterator born = dbpediaInfModel.listObjectsOfProperty(abe, birthPlace);
        System.out.println("Abraham Lincoln has a birthPlace:");

        if(born.hasNext()){
          RDFNode abe_birthplace = born.next();
          abe_home = abe_birthplace.asResource();
          System.out.println(abe_home.getURI());
          NodeIterator countryIn = dbpediaInfModel.listObjectsOfProperty(abe_home, country);

          if(countryIn.hasNext()){
            RDFNode abeHomeCountry = countryIn.next();
            Resource abeCountry = abeHomeCountry.asResource();
            System.out.println("Abraham Lincoln home country:");
            System.out.println(abeCountry.getURI());
          }
        }
        else{
          System.out.println("no place.");
        }
      }
    }
    else {
      System.out.println("Abraham Lincoln was failed to be recognized as a person");
    }
	*/

    Property country = dbpediaInfModel.getProperty("http://dbpedia.org/ontology/country");
    Property birthPlace = dbpediaInfModel.getProperty("http://dbpedia.org/ontology/birthPlace");
    Property isPartOf = dbpediaInfModel.getProperty("http://dbpedia.org/ontology/isPartOf");
    Property state = dbpediaInfModel.getProperty("http://dbpedia.org/ontology/state");

   	Resource unitedStates = dbpediaInfModel.getResource("http://dbpedia.org/resource/United_States");

    ResIterator peopleWithBirthPlace = dbpediaInfModel.listResourcesWithProperty(birthPlace);

    Resource homeState;

    int count = 0;
    while(count < 100){
    		Resource currentPerson = peopleWithBirthPlace.nextResource();
    		homeState = null;
    		//Look through multiple birth place entries
    		NodeIterator birthPlaceList = dbpediaInfModel.listObjectsOfProperty(currentPerson, birthPlace);
    		while (birthPlaceList.hasNext()){
    			RDFNode currentBirthPlace = birthPlaceList.next();
    			Resource currentBirthPlaceResource = currentBirthPlace.asResource();

    			//Check if birthplace is within the USA
    			if(dbpediaInfModel.contains(currentBirthPlaceResource, country, unitedStates)){
    				System.out.println(currentPerson + " ");
    				System.out.println(currentBirthPlaceResource.getURI());

    				ResIterator stateBirthPlace = dbpediaInfModel.listResourcesWithProperty(state, currentBirthPlaceResource);
    				if (stateBirthPlace.hasNext()){
    					homeState = currentBirthPlaceResource;
    					System.out.println("STATE! (1)");
    				}
    				//else
    				NodeIterator birthPlacePartOfList = dbpediaInfModel.listObjectsOfProperty(currentBirthPlaceResource, isPartOf);
    				while(birthPlacePartOfList.hasNext()){
    					RDFNode currentBirthPlacePartOf = birthPlacePartOfList.next();
    					Resource currentBirthPlacePartOfResource = currentBirthPlacePartOf.asResource();

    					ResIterator statePartOfBirthPlace = dbpediaInfModel.listResourcesWithProperty(state, currentBirthPlacePartOfResource);
    					if(statePartOfBirthPlace.hasNext()){
    						System.out.println(currentBirthPlacePartOfResource.getURI());
    						homeState = currentBirthPlacePartOfResource;
    						System.out.println("STATE! (2)");
    					}
    				}
    				//}
    				if (homeState != null){
    					System.out.println(homeState.getURI());
    				}
					System.out.println();
				}
    		}
    	count++;
    }

    //goal: for type(Person) get birthplace,
    //      if birthplace country == United States
    //			then find birthplace $state
    //
    //		find United States area in sq miles
    //		find $state in sq miles
    //
    //		$Person was born in $birthplace.state
    //		What percent of the United States by area
    //		is $state if the United States is $US.areaSqMiles
    //		and $state is $state.areaSqMiles? Round to the
    //		nearest whole number percentage.
    //
    //		Ans = ( $state.areaSqMiles / %US.areaSqMiles ) * 100


    System.out.println("done");
  }


  /*
  public static void validateModel(InfModel m){
    ValidityReport validity = m.validate();
    if (validity.isValid()) {
        System.out.println("OK, valid.");
        return;
    } else {
        System.out.println("Conflicts");
        for (Iterator i = validity.getReports(); i.hasNext();){
            System.out.println(" - " + i.next());
        }
        return;
    }
  }

  public static void printStatements(Model m, Resource s, Property p, Resource o) {
    for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) {
        Statement stmt = i.nextStatement();
        System.out.println(" - " + PrintUtil.print(stmt));
    }
  }
  */
}
