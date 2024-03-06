public enum Book {
    JHTP("Java how to Program", "2015");

    private final String title;
    private final String copyrightYear;
    Book(String title, String copyrightYear) {
        this.title = title;
        this.copyrightYear =copyrightYear;
    }

    // acessor para título de campo
    public String getTitle()
    {
        return title;
    }
    // acessor para o campo copyrightYear
    public String getCopyrightYear()
    {
        return copyrightYear;
    }
}
