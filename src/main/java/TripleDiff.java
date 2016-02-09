
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
* Created by fo, modified by pvb
*/
public class TripleDiff{

  private List<Line> lines = new ArrayList<>();

  public class Line {

    public final boolean add;
    public final Statement stmt;

    public Line(Statement stmt, boolean add) {
      this.add = add;
      this.remove = !add;
      this.stmt = stmt;
    }

  }

  public List<Line> getLines() {
    return this.lines;
  }

  public void addStatement(Statement stmt) {
    this.lines.add(new Line(stmt, true));
  }

  public void removeStatement(Statement stmt) {
    this.lines.add(new Line(stmt, false));
  }

  public void apply(Model model) {
    for (Line line : this.lines) {
      if (line.add) {
        model.add(line.stmt);
      } else {
        model.remove(line.stmt);
      }
    }
  }

  public void unapply(Model model) {
    for (Line line : this.lines) {
      if (line.add) {
        model.remove(line.stmt);
      } else {
        model.add(line.stmt);
      }
    }
  }

  public String toString() {
    String diffString = "";
    for (Line line : this.lines) {
      StringWriter triple = new StringWriter();
      RDFDataMgr.write(triple, ModelFactory.createDefaultModel().add(line.stmt), Lang.NTRIPLES);
      if (line.add) {
        diffString += "+ ".concat(triple.toString());
      } else {
        diffString += "- ".concat(triple.toString());
      }
    }
    return diffString;
  }
  
}
