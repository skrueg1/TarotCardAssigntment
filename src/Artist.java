public class Artist extends Participant {
    Writer partner;

    public Artist() {
        this.name = null;
        this.preferredCardClass = null;
        this.partner = null;
        this.avoiders = null;
        this.assignedCard = null;
    }

    public Artist (String name) {
        super();
        this.name = name;
    }
}
