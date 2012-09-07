/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cygn.foobar2000;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "accounts")
public class Account {
    
    @DatabaseField(id = true)
    private String name;
    @DatabaseField
    private String password;
    
    public Account() {
        // ORMLite needs a no-arg constructor 
    }
    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}