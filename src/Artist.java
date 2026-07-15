import java.util.ArrayList;
import java.util.List;

public class Artist extends Participant {
    Writer partner;

    public Artist() {
        this.name = null;
        this.preferredCardClass = null;
        this.partner = null;
        this.avoiders = null;
        this.assignedCard = null;
    }
}
