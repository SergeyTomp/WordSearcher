public enum Mime {

    TEXT_PLAIN("text/plain"),
    TEXT_CSV("text/csv"),
    TEXT_CSS("text/css"),
    TEXT_HTML("text/html"),
    TEXT_XML("text/xml"),
    TEXT_CMD("text/cmd"),
    TEXT_PHP("text/php"),
    APPLICATION_XML("application/xml");

    private String fileType;

    Mime(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }
}
