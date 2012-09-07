/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cygn.foobar2000;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 *
 * @author Thomas Friedel
 */

public class AccountApp {

    public static void main(String[] args) throws Exception {

        // this uses h2 by default but change to match your database
        String databaseUrl = "jdbc:h2:mem:account";
		databaseUrl = "jdbc:h2:tcp://localhost/~/test";
        // create a connection source to our database
		ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl,"sa","");
		//Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test","sa","");
        // instantiate the dao
        Dao<Account, String> accountDao =
            DaoManager.createDao(connectionSource, Account.class);

        // if you need to create the 'accounts' table make this call
        TableUtils.createTable(connectionSource, Account.class);

 	
        // create an instance of Account
        Account account = new Account();
        account.setName("Jim Coakley");

        // persist the account object to the database
        accountDao.create(account);

        // retrieve the account from the database by its id field (name)
        Account account2 = accountDao.queryForId("Jim Coakley");
        System.out.println("Account: " + account2.getName());

        // close the connection source
        connectionSource.close();
    }
}
