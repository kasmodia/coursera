class TestFailedException extends RuntimeException {
    private static final long serialVersionUID = 3346160914012192379L;

    public TestFailedException(final String msg) {
       super(msg);
    }
}