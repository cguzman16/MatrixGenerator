/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrixgenerator;

import javax.swing.JOptionPane;

/**
 *
 * @author carolynguzman
 */
public class mtxGenException extends Exception {

    private String exceptionType, message;
    public mtxGenException(String exceptionType) {
        this.exceptionType = exceptionType;
        setError();
    }

    private void setError() {
        switch(exceptionType) {
            case "exists":
               message = "Path Already Exists";
                break;
                case "format":
               message = "Invalid Format";
                break;
            default:
               message = "Unknown Error";
        }
    }
    
    public String getError() {
        return message;
    }
}
