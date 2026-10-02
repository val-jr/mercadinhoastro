package com.dev.mercadinhoastro.web.exceptions;

public class ClienteNotFoundExecption  extends RuntimeException{
    public ClienteNotFoundExecption() {
        super("despesa nao encontrada");
    }
    public ClienteNotFoundExecption(String message) {
        super(message);
    }
}
