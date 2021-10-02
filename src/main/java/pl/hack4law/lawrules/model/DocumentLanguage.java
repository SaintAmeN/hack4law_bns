package pl.hack4law.lawrules.model;

public enum DocumentLanguage {
    ENGLISH("en"),
    POLISH("pl");
    private final String language;

    DocumentLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
