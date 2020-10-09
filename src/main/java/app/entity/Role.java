package app.entity;

public enum Role {
    ADMINISTRATOR ("ADMINISTRATOR"),
    JURY ("JURY"),
    MANAGER("MANAGER"),
    PARTICIPANT("PARTICIPANT");
    private String title;

    Role(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
