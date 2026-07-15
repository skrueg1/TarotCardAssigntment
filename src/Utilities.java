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
}
