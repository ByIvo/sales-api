/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author byivo
 */
public class MySQLErrorCode {
    public static final int ER_DUP_ENTRY = 1062;
    public static final int CONSTRAINT_VIOLATED = 1452;
    
    public static final Map<Integer, String> errorMessages;
    
    static {
        errorMessages = new HashMap<>();
        
        errorMessages.put(ER_DUP_ENTRY, "Resultado duplicado!");
        errorMessages.put(CONSTRAINT_VIOLATED, "%1$s não encontrado!");
    }
}
