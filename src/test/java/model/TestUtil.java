/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author byivo
 */
public class TestUtil {

    public static String getStringBySize(int size) {
        StringBuilder stb = new StringBuilder();

        for (int i = 0; i < size; i++) {
            stb.append('a');
        }

        return stb.toString();
    }

    public static String getEmailBySize(int size) {
        StringBuilder stb = new StringBuilder();

        if (size > 6) {
            for (int i = 6; i < size; i++) {
                stb.append('a');
            }
        }

        stb.append("a@a.aa");

        return stb.toString();
    }
}
