import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utilities {

    public static void main(String[] args){

    }

    public static CardClass determinePairPreference(Writer writer, Artist artist){
        CardClass writerPref = writer.preferredCardClass;
        CardClass artistPref = artist.preferredCardClass;

        // participants agree on class preference -> return it
        if (writerPref == artistPref) return writerPref;

        // writer has no preference, artist has preference -> return artist's preference
        if ((writerPref == null) && (artistPref != null)) return artistPref;

        // writer has preference, artist does not -> return writer's preference
        if ((writerPref != null) && (artistPref == null)) return writerPref;

        // neither participants have preference -> return no preference (null)
        return null;
    }

    public static List<ParticipantPair> sortPairsByPriority(List<ParticipantPair> pairs){
        List<ParticipantPair> first = new ArrayList<>();
        List<ParticipantPair> second = new ArrayList<>();
        List<ParticipantPair> third = new ArrayList<>();

        for (ParticipantPair pair : pairs){
            if (pair.priority == 1) first.add(pair);
            else if (pair.priority == 2) second.add(pair);
            else if (pair.priority == 3) third.add(pair);
        }

        first.addAll(second);
        first.addAll(third);

        return first;
    }

    public static List<ParticipantPair> getPairsByPriority(List<ParticipantPair> pairs, int priorityLevel){
        List<ParticipantPair> newlist = new ArrayList<>();
        for (ParticipantPair pair : pairs){
            if (pair.priority == priorityLevel) newlist.add(pair);
        }
        return newlist;
    }

    public static TarotCard cardSearch(List<TarotCard> deck, String name){
        for (TarotCard card : deck){
            if (card.cardName.equals(name)) return card;
        }
        return null;
    }

    /************** PARSING FUNCTIONS **************/

    public List<Participant> parseParticipants() {
        String file = "data/responses.csv";
        String line;

        HashMap<String, Artist> artistList = new HashMap<>();
        HashMap<String, Writer> writerList = new HashMap<>();
        List<TarotCard> deck = getTarotDeckList();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String partner1 = null;
            String partner2 = null;
            List<TarotCard> avoiders = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                String[] values = parseLine(line);

                // value[0] : name
                // value[1] : role (Artist or Writer)
                // value[2] : class preference (Coven, Chivalry, or Either)
                // value[3] : name of partner1
                // value[4] : name of partner2
                // value[5] : avoid card #1
                // value[6] : avoid card #2
                // value[7] : avoid card #3

                // save partner names
                partner1 = values[3];
                partner2 = values[4];

                // save avoid cards
                for (int i = 5; i < values.length; i++) {
                    if (values[i].isEmpty()) break;
                    avoiders.add(Utilities.cardSearch(deck, values[i]));
                }

                if (values[1].equals("Artist")) {
                    Artist participant;
                    if (artistList.containsKey(values[0])) participant = artistList.get(values[0]);
                    else participant = new Artist(values[0]);

                    // name assigned above

                    // assign card preference
                    if (values[2].equals("Coven")) participant.preferredCardClass = CardClass.COVEN;
                    else if (values[2].equals("Chivalry")) participant.preferredCardClass = CardClass.CHIVALRY;
                    else participant.preferredCardClass = null;

                    // assign partner
                    if (partner1 != null) {
                        Writer temp;
                        if (writerList.containsKey(partner1)) temp = writerList.get(partner1);
                        else temp = new Writer(partner1);
                        participant.partner = writerList.get(partner1);
                    }

                    // add avoid cards
                    participant.avoiders.addAll(avoiders);

                } else {
                    Writer participant;
                    if (writerList.containsKey(values[0])) participant = writerList.get(values[0]);
                    else participant = new Writer(values[0]);

                    // name assigned above

                    // assign card preference
                    if (values[2].equals("Coven")) participant.preferredCardClass = CardClass.COVEN;
                    else if (values[2].equals("Chivalry")) participant.preferredCardClass = CardClass.CHIVALRY;
                    else participant.preferredCardClass = null;

                    // assign partner(s)
                    if (partner1 != null) {
                        Artist temp;
                        if (artistList.containsKey(partner1)) temp = artistList.get(partner1);
                        else temp = new Artist(partner1);
                        participant.partner1 = artistList.get(partner1);
                    }
                    if (partner2 != null) {
                        Artist temp;
                        if (artistList.containsKey(partner1)) temp = artistList.get(partner2);
                        else temp = new Artist(partner2);
                        participant.partner2 = artistList.get(partner2);
                    }

                    // add avoid cards
                    participant.avoiders.addAll(avoiders);
                }
                avoiders.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Participant> participants = new ArrayList<>();
        participants.addAll(artistList.values());
        participants.addAll(writerList.values());
        return participants;
    }

    public String[] parseLine(String line){
        String[] values = new String[8];

        // Split the line string into 6 parts, the last section including a line with quotes
        String[] firstPart = line.split(",", 6);

        // Parse first part
        for (int i = 0; i < 5; i++) {
            if (!firstPart[i].isEmpty()) values[i] = firstPart[i];
        }

        // Parse final column
        if (!firstPart[5].isEmpty()){
            // remove quotes if they exist
            if (firstPart[5].startsWith("\"") &&  firstPart[5].endsWith("\"")) {
                firstPart[5] = firstPart[5].substring(1, firstPart[5].length() - 1);
            }
            String[] cards = firstPart[5].split(",");
            // add entries to values
            for (int i = 0; i < 3; i++)
                if (!cards[i].isEmpty()) values[5 + i] = cards[i];
        }

        return values;
    }

    public static List<TarotCard> getTarotDeckList() {
        String file = "data/deck.csv";
        String line;

        List<TarotCard> deck = new ArrayList<>();
        CardClass cardClass = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (values[1].equals("Coven")) cardClass = CardClass.COVEN;
                else cardClass = CardClass.CHIVALRY;

                deck.add(new TarotCard(values[0], cardClass));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deck;
    }

}
