/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.model;

/**
 *
 * @author byivo
 */
public class ModelValidation {
    
    public static class User {
        public static final String EMPTY_NAME = "Você deve informar seu nome!";
        public static final String LONGEST_NAME = "Seu nome deve conter no máximo 80 caracteres!";
        
        public static final String EMPTY_EMAIL = "Você deve informar seu email!";
        public static final String LONGEST_EMAIL = "Seu e-mail deve contern o máximo 100 caracteres!";
        public static final String NOT_AN_EMAIL = "Você deve informar um e-mail válido!";
        public static final String DUPLICATED_EMAIL = "O e-mail escolhido já está registrado!";
        
        public static final String EMPTY_PASSWORD = "Informe uma senha";
        public static final String UNMATCH_PASSWORD = "As senhas não conferem";
    }
    
    public static class Item {
        public static final String EMPTY_NAME = "Você deve informar o nome do produto!";
        public static final String LONGEST_NAME = "O nome do produto deve conter no máximo 80 caracteres!";
        
        public static final String LONGEST_IMAGE = "O link informado é muito grande!";
        
        public static final String WRONG_PRICE = "O preço não pode ser R$ 0,00!";
    }
    
    public static class Sell {
        public static final String EMPTY_PAYMENT = "Informe um meio de pagamento!";
        
        public static final String EMPTY_CART = "Você deve comprar ao menos um item!";
        
        public static final String NULL_USER = "Você deve indentificar-se para realizar a compra!";
    }
}
