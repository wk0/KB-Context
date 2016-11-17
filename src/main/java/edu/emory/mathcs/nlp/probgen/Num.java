package edu.emory.mathcs.nlp.probgen;

import org.apache.jena.rdf.model.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Num {

    private Statement statement;
    private StmtIterator iter;
    private RDFNode object;
    private Resource resource;

    public Num () { }

    public Num (Statement statement) {
        this.statement = statement;
        this.object = this.statement.getObject();
    }

    public Num (StmtIterator iter) {
        this.iter = iter;
    }

    public Num (Resource resource, Property property) {
        this.resource = resource;
        this.resource.getProperty(property);
        this.object = this.resource;
    }

    public float getNum() {
        String line = object.toString();
        String pattern = "(\\d+.\\d+|\\d+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        float f;
        f = 0;
        try {
            m.find();
            f = Float.parseFloat(m.group(0));
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        return f;
    }

    public float getSumOfStatementList() {
        float sum = 0;
        while (this.iter.hasNext()) {
            Statement statement = this.iter.nextStatement();
            Num nm  = new Num(statement);
            sum = sum + nm.getNum();
        }
        return sum;
    }
}
