/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import java.util.Objects;

/**
 *
 * @author Igor Medeiros
 */
public class Variable {

    private String name;
    private String type;
    private int bloco;

    public Variable() {
    }

    public Variable(int type, String name, int bloco) {
        this.name = name;
        this.bloco = bloco;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBloco() {
        return bloco;
    }

    public void setBloco(int bloco) {
        this.bloco = bloco;
    }

    @Override
    public String toString() {
        return "Variable{" + "name=" + name + ", type=" + type + ", bloco=" + bloco + '}';
    }

    @Override
    public boolean equals(Object obj) {
        final Variable other = (Variable) obj;
        if (this.bloco != other.bloco) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }


}
