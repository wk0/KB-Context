package edu.emory.mathcs.nlp.probgen;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.tdb.TDBFactory;

/**
 * Created by gary on 10/27/16.
 */
public class InferenceModel {

    static InfModel dbpediaInfModel;
    static Model typeData;

    public static void makeInferenceModel() {
        String schemaDirectory = "../Database/dbpedia_2014";
        Dataset schemaDataset = TDBFactory.createDataset(schemaDirectory);
        Model dbpediaSchema = schemaDataset.getDefaultModel();

        String typeDirectory = "../Database/instance_types_en";
        Dataset typeDataset = TDBFactory.createDataset(typeDirectory);
        typeData = typeDataset.getDefaultModel();

        String factDirectory = "../Database/mappingbased_properties_en";
        Dataset factDataset = TDBFactory.createDataset(factDirectory);
        Model factData = factDataset.getDefaultModel();

        String numDirectory = "../Database/specific_mappingbased_properties_en";
        Dataset numDataset = TDBFactory.createDataset(numDirectory);
        Model numData = numDataset.getDefaultModel();

        //combine models
        Model combinedDataModel = ModelFactory.createUnion(factData, numData);
        Model dbpediaBaseModel = ModelFactory.createUnion(combinedDataModel, typeData);

        //Build combined inference model
        Reasoner dbpediaReasoner = ReasonerRegistry.getRDFSReasoner();
        dbpediaReasoner = dbpediaReasoner.bindSchema(dbpediaSchema);
        dbpediaInfModel = ModelFactory.createInfModel(dbpediaReasoner, dbpediaBaseModel);
    }
}
