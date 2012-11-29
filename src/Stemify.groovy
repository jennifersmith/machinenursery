import cc.mallet.pipe.Pipe
import cc.mallet.types.Instance
import org.apache.lucene.queryParser.QueryParser
import org.apache.lucene.util.Version
import org.apache.lucene.analysis.en.EnglishAnalyzer


public class Stemify extends Pipe implements Serializable {

    public Instance pipe (Instance carrier) {

        if (carrier.getData() instanceof String) {
            String data = (String) carrier.getData();


            carrier.setData(new QueryParser(Version.LUCENE_34, "field", new EnglishAnalyzer(Version.LUCENE_34)).parse(data.replace("!", "")).toString("field"));
        }
        else {
            throw new IllegalArgumentException("CharSequenceLowercase expects a String, found a " + carrier.getData().getClass());
        }

        return carrier;
    }

    // Serialization

    private static final long serialVersionUID = 1;
    private static final int CURRENT_SERIAL_VERSION = 0;

    private void writeObject (ObjectOutputStream out) throws IOException {
        out.writeInt (CURRENT_SERIAL_VERSION);
    }

    private void readObject (ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        int version = inputStream.readInt ();
    }

}