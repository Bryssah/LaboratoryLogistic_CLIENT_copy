/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazydevelopers.models;

/**
 * Enumeration with the levels of danger which a product can have
 * @author Bryssa
 */
public enum Danger {
    /**
     * Low level of danger - You might drink/eat this and probably nothing
     * happens.
     */
    LOW,
    /**
     * Medium level of danger - Maybe not safe at all to drink/eat.
     */
    MEDIUM,
    /**
     * High level of danger - OK, this is serious, manipulate with caution.
     */
    HIGH,
    /**
     * Very high level of danger - You need a special P.P.E. to manipulate
     * this and be sure to use only in a controlled environment.
     */
    VERY_HIGH;
}
