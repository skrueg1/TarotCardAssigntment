import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class createPairings {
    public static void main(String[] args){

        List<Writer> writers = new ArrayList<>();
        List<Artist> artists = new ArrayList<>();
        List<ParticipantPair> pairings = new ArrayList<>();

        // parse csv --> populate writers and artists with ALL writers and artists

        List<Writer> lonelyWriters = new ArrayList<>();
        List<Artist> lonelyArtists = new ArrayList<>();
        lonelyWriters.addAll(writers);
        lonelyArtists.addAll(artists);

        /*
            If a writer has no partners: continue (keep on lonelyWriters list)
            If a writer has one or both partner(s)...
                + add new ParticipantPair to pairings with their preferred CardClass for each partner
                + each artist partner is assigned this writer as their partner
                + each artist is removed from the lonelyArtist list
            If only 1 partner: keep writer on lonelyWriters list
            If both partners: remove writer from lonelyWriters list
        */
        for(Writer writer : lonelyWriters){
            // partner1
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
        for (Writer writer : lonelyCovenWriters){
            if (writer.partner1 == null) {
                writer.partner1 = assignArtist(writer, lonelyCovenArtists, lonelyArtists, lonelyChivalryArtists, pairings);
            }

            if (writer.partner2 == null) {
                writer.partner2 = assignArtist(writer, lonelyCovenArtists, lonelyArtists, lonelyChivalryArtists, pairings);
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
                writer.partner1 = assignArtist(writer, lonelyChivalryArtists, lonelyArtists, lonelyCovenArtists, pairings);
            }

            if (writer.partner2 == null) {
                writer.partner2 = assignArtist(writer, lonelyChivalryArtists, lonelyArtists, lonelyCovenArtists, pairings);
            }

            lonelyChivalryWriters.remove(writer);
        }


        /*
            lonelyCovenWriters and lonelyChivalryWriters are empty
            lonelyWriters remain -> these writers have no CardClass preference

            For every writer in lonelyWriter...
                If partner1 is empty -> assign an artist with a pairin CardClass preference of the artist
                If partner2 is empty -> assign an artist with a pairin CardClass preference of the artist
            Remove writer from lonelyWriter list

            This ensures that the created ParticipantPairs have a preference of the artist, which is either
            COVEN, CHIVALRY, or NULL, as the writer does not have a preference.
        */
        for (Writer writer : lonelyWriters){

            if (writer.partner1 == null) {
                if (!lonelyCovenArtists.isEmpty()){
                    writer.partner1 = assignArtist(writer, lonelyCovenArtists, null, null, pairings);
                } else if (!lonelyChivalryArtists.isEmpty()){
                    writer.partner1 = assignArtist(writer, lonelyChivalryArtists, null, null, pairings);
                } else {
                    writer.partner1 = assignArtist(writer, lonelyArtists, null, null, pairings);
                }
            }

            if (writer.partner2 == null) {
                if (!lonelyCovenArtists.isEmpty()){
                    writer.partner2 = assignArtist(writer, lonelyCovenArtists, null, null, pairings);
                } else if (!lonelyChivalryArtists.isEmpty()){
                    writer.partner2 = assignArtist(writer, lonelyChivalryArtists, null, null, pairings);
                } else {
                    writer.partner2 = assignArtist(writer, lonelyArtists, null, null, pairings);
                }
            }

            lonelyWriters.remove(writer);
        }



        // ensure Pairings has 78 objects
        // ensure every writer in writers has partner1 & partner2 not null
        // ensure every artist in artists has partner not null
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


        for (ParticipantPair pair : pairings) {
            pair.setAvoiders();
            pair.setPriority();
            pair.preferredClass = Utilities.determinePairPreference(pair.writer, pair.artist);
        }


    }

    public static Artist assignArtist(Writer writer,
                                      List<Artist> preferredList,
                                      List<Artist> eitherList,
                                      List<Artist> oppositeList,
                                      List<ParticipantPair> pairings) {

        Random random = new Random(333);
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

}
