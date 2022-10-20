package com.wizeline.utils.Exceptions;

public class ExceptionGenerica {


    public static class ExcepcionGenerica extends RuntimeException {
        public ExcepcionGenerica(String mensajeError) {

            super(mensajeError);
        }
    }
}
