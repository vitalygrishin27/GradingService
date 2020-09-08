package app.entity;

public enum Role {
    ADMINISTRATOR ("ADMINISTRATOR"),
    JURY ("JURY"),
    MANAGER("MANAGER"),
    PARTICIPANT("PARTICIPANT"),
    UNDEFINED("UNDEFINED");

    private String title;

    Role(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
