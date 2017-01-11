package edu.emory.mathcs.nlp.probgen;
import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Random;

import static edu.emory.mathcs.nlp.probgen.InferenceModel.dbpediaInfModel;

public class App {

    public static void main(String[] args) {
        System.out.println("Starting!");

        LoadLogger logger = new LoadLogger();
        logger.loadLogger();
        InferenceModel inf = new InferenceModel();
        inf.makeInferenceModel();


        Resource obamaResource = dbpediaInfModel.getResource("http://dbpedia.org/resource/Barack_Obama");
        Resource abeResource = dbpediaInfModel.getResource("http://dbpedia.org/resource/Abraham_Lincoln");

        //ArrayList<SubjectPredicatePair> inc = getIncoming(obamaResource);
        //ArrayList<PredicateObjectPair>  out = getOutgoing(obamaResource);


        System.out.println();

        System.out.println(obamaResource);
        InOut obama = new InOut(obamaResource);
	    System.out.println("	incoming: " + obama.getSizeIncoming());
        System.out.println("	outgoing: " + obama.getSizeOutgoing());


        System.out.println();

        System.out.println(abeResource);
        InOut abe = new InOut(abeResource);
        System.out.println("	incoming: " + abe.getSizeIncoming());
        System.out.println("	outgoing: " + abe.getSizeOutgoing());

    }
}

class InOut{
	Resource core;
	ArrayList<SubjectPredicatePair> incoming;
	ArrayList<PredicateObjectPair> outgoing;

	int incomingLength;
	int outgoingLength;

	public InOut(Resource r){
		this.core = r;

		ArrayList<SubjectPredicatePair> inc = new ArrayList<SubjectPredicatePair>();
		ArrayList<PredicateObjectPair> out = new ArrayList<PredicateObjectPair>();

		inc = getIncoming(core);
		out = getOutgoing(core);

		this.incoming = inc;
		this.outgoing = out;

		this.incomingLength = incoming.size();
		this.outgoingLength = outgoing.size();
	}

	public int getSizeIncoming(){
		return incomingLength;
	}
	public int getSizeOutgoing(){
		return outgoingLength;
	}

	public ArrayList<SubjectPredicatePair> getIncoming(Resource resourceIn){
    	// incoming
        Resource s = null;
        Property p = null;
        StmtIterator oi = dbpediaInfModel.listStatements(s, p, resourceIn);

        ArrayList<SubjectPredicatePair> result = new ArrayList<SubjectPredicatePair>();

        while(oi.hasNext()){
        	Statement statement = oi.nextStatement();
        	Resource  subject   = statement.getSubject();
            Property  predicate = statement.getPredicate();


            if(checkProperty(predicate.toString()) && checkResource(subject.toString())) {
            	SubjectPredicatePair pair = new SubjectPredicatePair(subject, predicate);
            	result.add(pair);
            }
        }
    	return result;
    }


	public ArrayList<PredicateObjectPair> getOutgoing(Resource resourceIn){
    	// outgoing
        Property p = null;
        RDFNode r = null;
        StmtIterator oo = dbpediaInfModel.listStatements(resourceIn, p, r);

        ArrayList<PredicateObjectPair> result = new ArrayList<PredicateObjectPair>();

        while (oo.hasNext()){
            Statement statement = oo.nextStatement();
            Property  predicate = statement.getPredicate();
            RDFNode   object    = statement.getObject();

            if(checkProperty(predicate.toString()) && checkResource(object.toString())) {
            	PredicateObjectPair pair = new PredicateObjectPair(predicate, object.asResource());
            	result.add(pair);
            }
        }
        return result;
    }

    public boolean checkResource(String object) {
        String dbo = "dbpedia.org/resource";
        Pattern dboPattern = Pattern.compile(dbo);
        Matcher dboMatcher = dboPattern.matcher(object);
        return dboMatcher.find();
    }

    public boolean checkProperty(String object) {
        String dbo = "dbpedia.org/ontology";
        Pattern dboPattern = Pattern.compile(dbo);
        Matcher dboMatcher = dboPattern.matcher(object);
        return dboMatcher.find();
    }
}




class PredicateObjectPair{
	Property predicate;
	Resource object;
	public PredicateObjectPair(Property p, Resource r){
		this.predicate = p;
		this.object = r;
	}

	public Property getPredicate(){
		return predicate;
	}
	public Resource getObject(){
		return object;
	}
}

class SubjectPredicatePair{
	Resource subject;
	Property predicate;

	public SubjectPredicatePair(Resource r, Property p){
		this.subject = r;
		this.predicate = p;
	}

	public Resource getSubject(){
		return subject;
	}
	public Property getPredicate(){
		return predicate;
	}
}

class RandomWalk{
	ResourceObject seed;
	ArrayList<ResourceObject> chain;

	public RandomWalk(ResourceObject ro){
		ArrayList<ResourceObject> chain = new ArrayList<ResourceObject>();
		this.seed = ro;
		chain.add(ro);
		this.chain = chain;
	}

	public void takeStep(){
		ResourceObject previous = chain.get(chain.size() - 1);

		Resource previousResource = previous.getOriginalResource();
		Property previousRandomProperty = previous.getRandomProperty();

		int rerollCount = 0;
		while(previousRandomProperty == null){
			if(rerollCount > 3){
				return;
			}
			//System.out.println("Reroll previousRandomProperty");
			previousRandomProperty = previous.getRandomProperty();
			rerollCount++;
		}



		NodeIterator candidates = dbpediaInfModel.listObjectsOfProperty(previousResource, previousRandomProperty);

		ArrayList<Literal> nextLiteral = new ArrayList<Literal>();
		ArrayList<Resource> nextResource = new ArrayList<Resource>();

		while(candidates.hasNext()){
			RDFNode node = candidates.next();

			if(node.isLiteral()){
				nextLiteral.add(node.asLiteral());
			}
			if(node.isResource()){
				nextResource.add(node.asResource());
			}
		}

		while(nextResource.isEmpty()){
			//Reroll
			nextLiteral.clear();
			nextResource.clear();

			previousRandomProperty = previous.getRandomProperty();

			int rerollCount2 = 0;
			while(previousRandomProperty == null){
				if(rerollCount2 > 3){
					return;
				}
				//System.out.println("Reroll previousRandomProperty");
				previousRandomProperty = previous.getRandomProperty();
				rerollCount2++;
			}

			NodeIterator newCandidates = dbpediaInfModel.listObjectsOfProperty(previousResource, previousRandomProperty);

			while(newCandidates.hasNext()){
				RDFNode newNode = newCandidates.next();

				if(newNode.isLiteral()){
					nextLiteral.add(newNode.asLiteral());
				}
				if(newNode.isResource()){
					nextResource.add(newNode.asResource());
				}
			}

		}



		Resource resultResource = nextResource.get(new Random().nextInt(nextResource.size()));

		System.out.println(previousResource + " " + previousRandomProperty + " " + resultResource);
		System.out.println();


		ResourceObject next = new ResourceObject(resultResource);
		chain.add(next);

		// Currently going to only use Resources, soon will use literals
		return;
	}
}


class ResourceObject{
	Resource resource;
	ArrayList<Resource> typeList;
	ArrayList<Property> propertyList;

	public ResourceObject(Resource r){
		this.resource = r;
		this.typeList = getResourceTypeList(r);
		this.propertyList = getResourcePropertyList(r);
	}

	public Resource getOriginalResource(){
		return resource;
	}
	public ArrayList<Resource> getResourceTypeList(){
		return typeList;
	}
	public ArrayList<Property> getPropertyList(){
		return propertyList;
	}

	// Evenly distributed??? With removed duplicated may
	// bias toward those without since duplicates were pruned
	public Resource getRandomTypeResource(){
		if(typeList.isEmpty()){
			return null;
		}

		Resource randomTypeResource = typeList.get(new Random().nextInt(typeList.size()));
		return randomTypeResource;
	}
	public Property getRandomProperty(){
		if(propertyList.isEmpty()){
			return null;
		}

		Property randomProperty = propertyList.get(new Random().nextInt(propertyList.size()));
		return randomProperty;
	}


	public void printResourceTypeList(){
		System.out.println(resource.toString() + " types:");
		for(Resource r : typeList){
        	System.out.println("	" + r);
        }
	}
	public void printPropertyList(){
		System.out.println(resource.toString() + " properties:");
        for (Property p: propertyList) {
            System.out.println("	" + p);
        }
	}
	public void printBoth(){
		printResourceTypeList();
		System.out.println();
		printPropertyList();
		System.out.println();
		System.out.println();
	}


	// Resources
	private ArrayList getResourceTypeList(Resource r) {
        ArrayList<Resource> resourceArray = new ArrayList<Resource>();
        StmtIterator rIter = r.listProperties();
        while (rIter.hasNext()){
            Statement statement = rIter.nextStatement();
            //Resource  subject   = statement.getSubject();       // get the subject
            Property  predicate = statement.getPredicate();     // get the predicate
            RDFNode   object    = statement.getObject();        // get the object

            if (checkType(predicate.toString()) && checkDBO(object.toString()) && object instanceof Resource) {
                if(object.isResource()){
                	resourceArray.add(object.asResource());
                }
            }
        }
        ArrayList<Resource> resourceArrayND = removeDuplicateResources(resourceArray);

        return resourceArrayND;
    }

    private ArrayList removeDuplicateResources(ArrayList<Resource> resourceList){
    	Set<Resource> hs = new LinkedHashSet<Resource>();
    	hs.addAll(resourceList);
    	resourceList.clear();
    	resourceList.addAll(hs);
    	return resourceList;
    }

    // Properties
    private ArrayList getResourcePropertyList(Resource r) {
        ArrayList<Property> propertyArray = new ArrayList<Property>();
        StmtIterator oIter = r.listProperties();
        while (oIter.hasNext()){
            Statement statement = oIter.nextStatement();
            Property  predicate = statement.getPredicate();     // get the predicate

            if (checkDBO(predicate.toString())) {
                propertyArray.add(predicate);
            }
        }
        ArrayList<Property> propertyArrayND = removeDuplicateProperties(propertyArray);

        return propertyArrayND;
    }

    private ArrayList removeDuplicateProperties(ArrayList<Property> propertyList){
    	Set<Property> hs = new LinkedHashSet<Property>();
    	hs.addAll(propertyList);
    	propertyList.clear();
    	propertyList.addAll(hs);
    	return propertyList;
    }

    // Checkers
    public static boolean checkType(String predicate) {
        String type = "#type";
        Pattern typePattern = Pattern.compile(type);
        Matcher typeMatcher = typePattern.matcher(predicate);
        return typeMatcher.find();
    }

    public static boolean checkDBO(String object) {
        String dbo = "dbpedia.org/ontology";
        Pattern dboPattern = Pattern.compile(dbo);
        Matcher dboMatcher = dboPattern.matcher(object);
        return dboMatcher.find();
    }
}