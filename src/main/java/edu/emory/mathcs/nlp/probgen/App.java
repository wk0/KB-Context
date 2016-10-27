package edu.emory.mathcs.nlp.probgen;

import org.apache.jena.rdf.model.*;
import org.apache.jena.datatypes.RDFDatatype;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static edu.emory.mathcs.nlp.probgen.InferenceModel.dbpediaInfModel;

public class App {

    public static void main(String[] args) {
        System.out.println("Starting!");

        LoadLogger logger = new LoadLogger();
        logger.loadLogger();

        Num num = new Num();

        InferenceModel inf = new InferenceModel();
        inf.makeInferenceModel();

        //Validate the model -- not needed right now
        /*
        /System.out.println("dbpediaInfModel");
        validateModel(dbpediaInfModel);
        System.out.println();
        */

        //goal: for type(Person) get birthplace,
        //      if birthplace country == United States
        //      then find birthplace $state
        //
        //      find United States area in sq miles
        //      find $state in sq miles
        //
        //      $Person was born in $birthplace.state
        //      What percent of the United States by area
        //      is $state if the United States is $US.areaSqMiles
        //      and $state is $state.areaSqMiles? Round to the
        //      nearest whole number percentage.
        //
        //      Ans = ( $state.areaSqMiles / %US.areaSqMiles ) * 100


        // 0. Person
        // 1. State
        // 2. Granular Location (may be null)
        ArrayList<Resource[]> personStateOriginal = getPersonAndHomeStateUSA(100);

        //printPSO(personStateOriginal);

        //note: takes either "round" or "precise"
        //      then arg2 is "sqKm"  or "sqMi"
        double usaArea = getUsaArea("round", "sqMi");

        String precision = "round";
        String units = "sqMi";
        for (Resource[] r : personStateOriginal) {
            double stateArea = getStateArea(precision, units, r[1]);
            printPSONoLoop(r);
            System.out.println("State Area:    " + stateArea + " " + units + " (" + precision + ")");
            System.out.println();
        }
    }

    public static double getStateArea(String code, String sqWhat, Resource state) {
        //STILL UNDER CONSTRUCTION
        Property areaTotalProp = dbpediaInfModel.getProperty("http://dbpedia.org/ontology/PopulatedPlace/areaTotal");
        NodeIterator stateProps = dbpediaInfModel.listObjectsOfProperty(state, areaTotalProp);

        double preciseArea = 0.0;
        double roundedArea = 0.0;

        while (stateProps.hasNext()) {
            RDFNode stateProperty = stateProps.next();
            Literal stateLiteral = stateProperty.asLiteral();

            RDFDatatype statePropertyDataType = stateLiteral.asLiteral().getDatatype();

            String numAsString = stateLiteral.getString();

            int intPlaces = numAsString.indexOf('.');
            int decPlaces = numAsString.length() - intPlaces - 1;

            if (decPlaces > 3) {
                preciseArea = Double.parseDouble(numAsString);
            } else {
                roundedArea = Double.parseDouble(numAsString);
            }
        }

        if (preciseArea == 0.0) {
            preciseArea = roundedArea;
        }

        if (sqWhat.equals("sqKm")) {
            if (code.equals("round")) {
                return roundedArea;
            }
            if (code.equals("precise")) {
                return preciseArea;
            }
        }

        if (sqWhat.equals("sqMi")) {
            //double milesInKm = 0.62137119;
            double usaSurveySqKmToSqMiles = 0.386100614;

            double preciseAreaInMiles = preciseArea * usaSurveySqKmToSqMiles;

            if (code.equals("round")) {
                DecimalFormat df = new DecimalFormat("#.");
                return Double.parseDouble(df.format(preciseAreaInMiles));
            }
            if (code.equals("precise")) {
                return preciseAreaInMiles;
            }
        }
        return 0.0;
    }

    public static double getUsaArea(String code, String sqWhat) {
        Resource usaResource = dbpediaInfModel.getResource("http://dbpedia.org/resource/United_States");
        Property areaTotalProp = dbpediaInfModel.getProperty("http://dbpedia.org/ontology/PopulatedPlace/areaTotal");
        NodeIterator usaProps = dbpediaInfModel.listObjectsOfProperty(usaResource, areaTotalProp);

        double preciseArea = 0.0;
        double roundedArea = 0.0;

        while (usaProps.hasNext()) {
            RDFNode usaProperty = usaProps.next();
            Literal usaLiteral = usaProperty.asLiteral();
            RDFDatatype usaPropertyDataType = usaLiteral.asLiteral().getDatatype();

            String numAsString = usaLiteral.getString();

            int intPlaces = numAsString.indexOf('.');
            int decPlaces = numAsString.length() - intPlaces - 1;

            if (decPlaces > 3) {
                preciseArea = Double.parseDouble(numAsString);
            } else {
                roundedArea = Double.parseDouble(numAsString);
            }
        }


        if (sqWhat.equals("sqKm")) {
            if (code.equals("round")) {
                return roundedArea;
            }
            if (code.equals("precise")) {
                return preciseArea;
            }
        }
        if (sqWhat.equals("sqMi")) {
            //double milesInKm = 0.62137119;
            double usaSurveySqKmToSqMiles = 0.386100614;

            double preciseAreaInMiles = preciseArea * usaSurveySqKmToSqMiles;

            if (code.equals("round")) {
                DecimalFormat df = new DecimalFormat("#.");
                return Double.parseDouble(df.format(preciseAreaInMiles));
            }
            if (code.equals("precise")) {
                return preciseAreaInMiles;
            }
        }
        return 0.0;
    }


    public static void printPSONoLoop(Resource[] indiv) {
        if (indiv[2] == null) {
            System.out.println("Person:  " + indiv[0].getURI());
            System.out.println("Location:" + " null");
            System.out.println("State:   " + indiv[1].getURI());
        } else if (indiv[2] != null) {
            System.out.println("Person:  " + indiv[0].getURI());
            System.out.println("Location:" + indiv[2].getURI());
            System.out.println("State :  " + indiv[1].getURI());
        }
    }


    public static void printPSO(ArrayList<Resource[]> personStateOriginal) {
        for (Resource[] indiv : personStateOriginal) {
            if (indiv[2] == null) {
                System.out.println("Person: " + indiv[0].getURI());
                System.out.println("Location:" + " null");
                System.out.println("State: " + indiv[1].getURI());
            } else if (indiv[2] != null) {
                System.out.println("Person:  " + indiv[0].getURI());
                System.out.println("Location:" + indiv[2].getURI());
                System.out.println("State :  " + indiv[1].getURI());
            }
            System.out.println();
        }
    }

    public static ArrayList<Resource[]> getPersonAndHomeStateUSA(int iterations) {
        ArrayList<Resource[]> result = new ArrayList<Resource[]>();

        Property birthPlace = dbpediaInfModel.getProperty("http://dbpedia.org/ontology/birthPlace");

        int count = 0;
        int missed = 0;
        ResIterator peopleWithBirthPlace = dbpediaInfModel.listResourcesWithProperty(birthPlace);

        while (count < iterations) {
            Resource currentPerson = peopleWithBirthPlace.nextResource();
            Resource[] phs = personHomeStateUSA(currentPerson);

            if (phs != null) {
                result.add(phs);
            } else {
                missed++;
            }

            count++;
        }
        return result;

    }


    public static Resource[] personHomeStateUSA(Resource currentPerson) {
        ArrayList<Resource[]> options = new ArrayList<Resource[]>();

        Property country = dbpediaInfModel.getProperty("http://dbpedia.org/ontology/country");
        Property birthPlace = dbpediaInfModel.getProperty("http://dbpedia.org/ontology/birthPlace");
        Resource unitedStates = dbpediaInfModel.getResource("http://dbpedia.org/resource/United_States");

        NodeIterator birthPlaceList = dbpediaInfModel.listObjectsOfProperty(currentPerson, birthPlace);
        while (birthPlaceList.hasNext()) {
            Resource currentBirthPlaceResource = birthPlaceList.next().asResource();

            //Check if birthplace is within the USA
            if (dbpediaInfModel.contains(currentBirthPlaceResource, country, unitedStates)) {
                Resource[] individual = new Resource[3];
                Resource homeState = null;
                Resource homeStateFromIntermediate = null;

                if (isStateInUSA(currentBirthPlaceResource)) {
                    homeState = currentBirthPlaceResource;
                }

                homeStateFromIntermediate = isPartOfStateInUSA(currentBirthPlaceResource);

                if (homeStateFromIntermediate != null) {
                    individual[0] = currentPerson;
                    individual[1] = homeStateFromIntermediate;
                    individual[2] = currentBirthPlaceResource;
                    options.add(individual);
                } else if (homeStateFromIntermediate == null && homeState != null) {
                    individual[0] = currentPerson;
                    individual[1] = homeState;
                    individual[2] = null;
                    options.add(individual);
                }
            }
        }

        Resource[] option = findBest(options);
        return option;
    }

    public static Resource[] findBest(ArrayList<Resource[]> options) {
        //pretty terrible way to do this im sure but ehhh
        for (Resource[] inst : options) {
            //has more granular birthplace
            if (inst[2] != null) {
                return inst;
            }
        }

        for (Resource[] inst : options) {
            //only has state
            if (inst[2] == null) {
                return inst;
            }
        }
        return null;
    }

    public static Resource isPartOfStateInUSA(Resource location) {
        Resource state;
        Property isPartOf = dbpediaInfModel.getProperty("http://dbpedia.org/ontology/isPartOf");
        NodeIterator locationsPartOfState = dbpediaInfModel.listObjectsOfProperty(location, isPartOf);

        while (locationsPartOfState.hasNext()) {
            Resource locationPartOfStateResource = locationsPartOfState.next().asResource();

            if (isStateInUSA(locationPartOfStateResource)) {
                state = locationPartOfStateResource;
                return state;
            }
        }
        return null;
    }


    public static boolean isStateInUSA(Resource location) {
        Property state = dbpediaInfModel.getProperty("http://dbpedia.org/ontology/state");

        ResIterator findState = dbpediaInfModel.listResourcesWithProperty(state, location);

        if (findState.hasNext()) {
            return true;
        } else {
            return false;
        }
    }

}