import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVparser {
    public void parse() {
        String file = "";
        String line;


        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                Participant participant;
                if (values[0].equals("Artist")) {
                    if (artistList.contains(values[1])) {}
                    participant = new Artist();
                } else {
                    // check if writer already on writerList
                    participant =  new Writer();
                }




                List<TarotCard> cardsToAvoid = new ArrayList<>();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
