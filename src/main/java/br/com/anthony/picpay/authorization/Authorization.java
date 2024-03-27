package br.com.anthony.picpay.authorization;

public record Authorization(String message) {

    public boolean isAuthorized() {
        return message.equals("Authorized");
    }
}
