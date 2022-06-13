package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses;

public class Response {
    private final String errorMessage;
    private final boolean error;

    public String getErrorMessage() {
        return errorMessage;
    }

    public Response(String errorMessage, boolean error) {
        this.errorMessage = errorMessage;
        this.error = error;
    }

    public boolean isError() {
        return error;
    }
}
