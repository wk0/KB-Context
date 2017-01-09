package edu.emory.mathcs.nlp.probgen;
import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.LinkedHashSet;
import java.util.Set;

import static edu.emory.mathcs.nlp.probgen.InferenceModel.dbpediaInfModel;

public class App {

    public static void main(String[] args) {
        System.out.println("Starting!");

        LoadLogger logger = new LoadLogger();
        logger.loadLogger();
        InferenceModel inf = new InferenceModel();
        inf.makeInferenceModel();


        Resource resource = dbpediaInfModel.getResource("http://dbpedia.org/resource/Barack_Obama");



        System.out.println(resource.toString() + " types:");
        List<Resource> resourceTypeList = getResourceTypeList(resource);
        for(Resource r : resourceTypeList){
        	System.out.println("	" + r);
        }

        System.out.println();

        System.out.println(resource.toString() + " properties:");
        List<Property> propertyList = getResourcePropertyList(resource);
        for (Property p: propertyList) {
            System.out.println("	" + p);
        }

    }

    static ArrayList getResourcePropertyList(Resource r) {
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

    static ArrayList getResourceTypeList(Resource r) {
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

    static ArrayList removeDuplicateResources(ArrayList<Resource> resourceList){
    	Set<Resource> hs = new LinkedHashSet<Resource>();
    	hs.addAll(resourceList);
    	resourceList.clear();
    	resourceList.addAll(hs);
    	return resourceList;
    }

    static ArrayList removeDuplicateProperties(ArrayList<Property> propertyList){
    	Set<Property> hs = new LinkedHashSet<Property>();
    	hs.addAll(propertyList);
    	propertyList.clear();
    	propertyList.addAll(hs);
    	return propertyList;
    }

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