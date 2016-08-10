/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.exceptions;

import javax.management.RuntimeErrorException;

/**
 *
 * @author byivo
 */
public class SalesException extends RuntimeErrorException {

    private String errorMessage;
    
    public SalesException(String message) {
        super(new Error(message));
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    

}
