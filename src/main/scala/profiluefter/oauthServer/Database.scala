package profiluefter.oauthServer

import java.sql.DriverManager

import org.mindrot.jbcrypt.BCrypt

object Database {
  def login(username: String, password: String): Int = {
    val url = "jdbc:sqlite:db.sqlite"
    val connection = DriverManager.getConnection(url)
    val statement = connection.prepareStatement("SELECT id,passwordHash FROM users where username=?")
    statement.setString(1,username)
    statement.execute()
    val resultSet = statement.getResultSet
    val hash = resultSet.getString("passwordHash")
    if(BCrypt.checkpw(password,hash))
      resultSet.getInt("id")
    else
      -1
  }
}
