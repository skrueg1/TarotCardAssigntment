import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AssignPairings {
    List<ParticipantPair> pairings;
    List<Artist> artists;
    List<Writer> writers;

    public void run() {
        createPairings();
        savePairings();
    }

    public void createPairings(){

        parseResponses();

        List<Writer> lonelyWriters = new ArrayList<>();
        List<Artist> lonelyArtists = new ArrayList<>();

        lonelyWriters.addAll(writers);
        lonelyArtists.addAll(artists);

        /******* PAIR UP PARTICIPANTS WHO ALREADY HAVE PARTNERS ********/

        /*
            If a writer has no partners: continue (keep on lonelyWriters list)
            If a writer has one or both partner(s)...
                + add new ParticipantPair to pairings with their preferred CardClass for each partner
                + each artist partner is assigned this writer as their partner
                + each artist is removed from the lonelyArtist list
            If only 1 partner: keep writer on lonelyWriters list
            If both partners: remove writer from lonelyWriters list
        */
        for (Writer writer : lonelyWriters){
            if (writer.partner1 == null) continue;

            if (writer.preferredCardClass == writer.partner1.preferredCardClass){
                pairings.add(new ParticipantPair(writer, writer.partner1, writer.preferredCardClass));
            } else {
                pairings.add(new ParticipantPair(writer, writer.partner1));
            }
            writer.partner1.partner = writer;
            lonelyArtists.remove(writer.partner1);

            // partner2
            if (writer.partner2 == null) continue;

            if (writer.preferredCardClass == writer.partner2.preferredCardClass){
                pairings.add(new ParticipantPair(writer, writer.partner2, writer.preferredCardClass));
            } else {
                pairings.add(new ParticipantPair(writer, writer.partner2));
            }
            writer.partner2.partner = writer;
            lonelyArtists.remove(writer.partner2);

            lonelyWriters.remove(writer);
        }

        /******* PAIR UP EVERYONE ELSE ********/

        List<Writer> lonelyCovenWriters = new ArrayList<>();
        List<Artist> lonelyCovenArtists = new ArrayList<>();
        List<Writer> lonelyChivalryWriters = new ArrayList<>();
        List<Artist> lonelyChivalryArtists = new ArrayList<>();

        /*
            For all writers on the lonelyWriters list:
                If they prefer COVEN: add to lonelyCovenWriters list
                If they prefer CHIVALRY: add to lonelyChivalryWriters list
                If they have no preference: do nothing and continue (they stay on lonelyWriters list)
                Remove from lonelyWriters if they were added to COVEN or CHIVALRY preference lists
        */
        for (Writer writer : lonelyWriters){
            if (writer.preferredCardClass == CardClass.COVEN){
                lonelyCovenWriters.add(writer);
            }
            else if (writer.preferredCardClass == CardClass.CHIVALRY){
                lonelyChivalryWriters.add(writer);
            }
            else
                continue;

            lonelyWriters.remove(writer);
        }

        /*
            For all artists on the lonelyartists list:
                If they prefer COVEN: add to lonelyCovenartists list
                If they prefer CHIVALRY: add to lonelyChivalryartists list
                If they have no preference: do nothing and continue (they stay on lonelyartists list)
                Remove from lonelyartists if they were added to COVEN or CHIVALRY preference lists
        */
        for (Artist artist : lonelyArtists){
            if (artist.preferredCardClass == CardClass.COVEN){
                lonelyCovenArtists.add(artist);
            }
            else if (artist.preferredCardClass == CardClass.CHIVALRY){
                lonelyChivalryArtists.add(artist);
            }
            else
                continue;

            lonelyArtists.remove(artist);
        }

        // for every writer in lonelyCovenWriters...
            // if partner1 == null
                // if lonelyCovenArtists not empty -> random assign partner1 as artist from lonelyCovenArtists
                    // ParticipantPair(writer, artist, combined preference)
                    // add pair to Pairings
                    // remove artist from lonelyCovenArtists
                // Else, assign partner1 from lonelyArtists
                    // ParticipantPair(writer, artist, writer preference)
                    // add pair to Pairings
                    // remove artist from lonelyArtists
                // else, assign partner1 from lonelyChivalryArtists
                    // ParticipantPair(writer, artist)
                    // add pair to Pairings
                    // remove artist from lonelyChivalryArtists
            // if partner2 == null
                // same as above
            // remove writer from their list

        /*
            For every writer with a COVEN preference
                If they are missing a partner1 -> assign an artist
                If they are missing a partner2 -> assign an artist
            Remove from lonelyCovenWriters list
        */
        for (Writer writer : lonelyCovenWriters) {
            if (writer.partner1 == null) {
                writer.partner1 = assignArtist(writer, lonelyCovenArtists, lonelyArtists, lonelyChivalryArtists);
            }

            if (writer.partner2 == null) {
                writer.partner2 = assignArtist(writer, lonelyCovenArtists, lonelyArtists, lonelyChivalryArtists);
            }

            lonelyCovenWriters.remove(writer);
        }

        /*
            For every writer with a CHIVALRY preference
                If they are missing a partner1 -> assign an artist
                If they are missing a partner2 -> assign an artist
            Remove from lonelyChivalryWriters list
        */
        for (Writer writer : lonelyChivalryWriters){
            if (writer.partner1 == null) {
                writer.partner1 = assignArtist(writer, lonelyChivalryArtists, lonelyArtists, lonelyCovenArtists);
            }

            if (writer.partner2 == null) {
                writer.partner2 = assignArtist(writer, lonelyChivalryArtists, lonelyArtists, lonelyCovenArtists);
            }

            lonelyChivalryWriters.remove(writer);
        }


        /*
            Now, lonelyCovenWriters and lonelyChivalryWriters are empty
            lonelyWriters remains -> these writers have no CardClass preference

            For every writer in lonelyWriter...
                If partner1 is empty -> assign an artist with a pairing CardClass preference of the artist
                If partner2 is empty -> assign an artist with a pairing CardClass preference of the artist
            Remove writer from lonelyWriter list

            This ensures that the created ParticipantPairs have a preference of the artist, which is either
            COVEN, CHIVALRY, or NULL, as the writer does not have a preference.
        */
        for (Writer writer : lonelyWriters){

            if (writer.partner1 == null) {
                if (!lonelyCovenArtists.isEmpty()){
                    writer.partner1 = assignArtist(writer, lonelyCovenArtists, null, null);
                } else if (!lonelyChivalryArtists.isEmpty()){
                    writer.partner1 = assignArtist(writer, lonelyChivalryArtists, null, null);
                } else {
                    writer.partner1 = assignArtist(writer, lonelyArtists, null, null);
                }
            }

            if (writer.partner2 == null) {
                if (!lonelyCovenArtists.isEmpty()){
                    writer.partner2 = assignArtist(writer, lonelyCovenArtists, null, null);
                } else if (!lonelyChivalryArtists.isEmpty()){
                    writer.partner2 = assignArtist(writer, lonelyChivalryArtists, null, null);
                } else {
                    writer.partner2 = assignArtist(writer, lonelyArtists, null, null);
                }
            }

            lonelyWriters.remove(writer);
        }

        /*
            Check work:
                + Do all writers have exactly two non-null artist partners?
                + Do all artists have one non-null writer partner?
                + Do we have exactly 78 pairings?
                + Do all pairings have a writer and artist?
         */
        try {

            for (Writer writer : writers) {
                if (writer.partner1 == null) throw new Exception(writer + " is missing partner1");
                if (writer.partner2 == null) throw new Exception(writer + " is missing partner2");
            }

            for (Artist artist : artists) {
                if (artist.partner == null) throw new Exception(artist + " is missing partner");
            }

            if (pairings.size() != 78) throw new Exception("Number of pairings is incorrect");

            for (ParticipantPair pair : pairings) {
                if (pair.writer == null) throw new Exception("a pair is missing a writer");
                if (pair.artist == null) throw new Exception("a pair is missing an artist");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Add final touches to all pairs
        for (ParticipantPair pair : pairings) {
            pair.preferredClass = Utilities.determinePairPreference(pair.writer, pair.artist);
            pair.setAvoiders();
            pair.setPriority();
        }

    }

    public Artist assignArtist(Writer writer,
                               List<Artist> preferredList,
                               List<Artist> eitherList,
                               List<Artist> oppositeList ) {

        Random random = new Random();
        Artist artist = null;

        if (!preferredList.isEmpty()) {
            artist = preferredList.get(random.nextInt(preferredList.size()));
            pairings.add(new ParticipantPair(writer, artist, artist.preferredCardClass));
            preferredList.remove(artist);
        } else if (!eitherList.isEmpty()) {
            artist = eitherList.get(random.nextInt(eitherList.size()));
            pairings.add(new ParticipantPair(writer, artist, writer.preferredCardClass));
            eitherList.remove(artist);
        } else {
            artist = oppositeList.get(random.nextInt(oppositeList.size()));
            pairings.add(new ParticipantPair(writer, artist));
            oppositeList.remove(artist);
        }

        artist.partner = writer;
        return artist;
    }

    public void parseResponses() {
        String file = "data/responses.csv";
        String line;

        HashMap<String, Artist> artistList = new HashMap<>();
        HashMap<String, Writer> writerList = new HashMap<>();
        HashMap<String, TarotCard> deck = Utilities.getTarotDeck();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String partner1 = null;
            String partner2 = null;
            List<TarotCard> avoiders = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                String[] values = new String[8];

                /********* LINE PARSING *********/

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

                // store chosen avoid cards
                for (int i = 5; i < values.length; i++) {
                    if (values[i].isEmpty()) break;
                    avoiders.add(deck.get(values[i]));
                }


                /****** CREATE AND FILL APPROPRIATE PARTICIPANT OBJECT ******/

                if (values[1].equals("Artist")) {
                    Artist participant;

                    // assign name
                    if (artistList.containsKey(values[0])) participant = artistList.get(values[0]);
                    else participant = new Artist(values[0]);

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

                    // assign name
                    if (writerList.containsKey(values[0])) participant = writerList.get(values[0]);
                    else participant = new Writer(values[0]);

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
    }

    public void savePairings() {
        // Save them as CSV for later use
        try (PrintWriter printer = new PrintWriter("data/generatedPairs.csv")) {
            printer.println("artist,writer,PREFERENCE,\"avoiders\",assigned card,priority");
            for (ParticipantPair pair : pairings)
                printer.print(pair.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
