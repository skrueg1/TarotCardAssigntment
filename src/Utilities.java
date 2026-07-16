import java.util.ArrayList;
import java.util.List;

public class Utilities {

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

}
