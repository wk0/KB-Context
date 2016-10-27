package edu.emory.mathcs.nlp.probgen;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.*;
import org.apache.jena.tdb.TDBFactory;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by gary on 10/26/16.
 */
public class TNum {
    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("edu.emory.mathcs.nlp.probgen/log4j.properties"));
            PropertyConfigurator.configure(props);
        } catch (Exception e) {

        }

        String numDirectory = "Database/specific_mappingbased_properties_en";
        Dataset numDataset = TDBFactory.createDataset(numDirectory);
        Model numData = numDataset.getDefaultModel();

        Resource iowa = numData.getResource("http://dbpedia.org/page/Britt,_Iowa");
        StmtIterator iter = numData.listStatements();

        Num nms = new Num(iter);
        System.out.print(nms.getSumOfStatementList());
    }
}
