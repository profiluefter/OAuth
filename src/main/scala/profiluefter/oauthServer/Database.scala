package profiluefter.oauthServer

import java.sql.DriverManager

import org.mindrot.jbcrypt.BCrypt

object Database {
  def registerUser(username: String, email: String, password: String): Int = {
    val url = "jdbc:sqlite:db.sqlite"
    val connection = DriverManager.getConnection(url)

    val statement = connection.prepareStatement(
      "INSERT INTO users (username, passwordHash, email) VALUES (?,?,?)"
    )
    statement.setString(1, username)
    statement.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()))
    statement.setString(3, email)
    statement.executeUpdate()
  }

  def registerApp(website: String, description: String, redirectUrl: String): Int = {
    val url = "jdbc:sqlite:db.sqlite"
    val connection = DriverManager.getConnection(url)

    val statement = connection.prepareStatement(
      "INSERT INTO applications (website, description, redirect_url) VALUES (?,?,?)"
    )
    statement.setString(1, website)
    statement.setString(2, description)
    statement.setString(3, redirectUrl)
    statement.executeUpdate()
  }

  def login(username: String, password: String): Int = {
    val url = "jdbc:sqlite:db.sqlite"
    val connection = DriverManager.getConnection(url)

    val statement = connection.prepareStatement("SELECT rowid,passwordHash FROM users where username=?")
    statement.setString(1, username)
    statement.execute()

    val resultSet = statement.getResultSet
    if (resultSet.next()) {
      val hash = resultSet.getString("passwordHash")
      if (BCrypt.checkpw(password, hash)) {
        val id = resultSet.getInt("rowid")
        connection.close()
        id
      } else {
        connection.close()
        -1
      }
    } else {
      connection.close()
      -2
    }
  }
}
