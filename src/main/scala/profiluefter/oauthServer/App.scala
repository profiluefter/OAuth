package profiluefter.oauthServer

import java.io._
import java.net.ServerSocket

import scala.io.BufferedSource

object App {

  def main(args: Array[String]): Unit = {
    val serverSocket = new ServerSocket(1337)

    while (true) {
      val clientSocket = serverSocket.accept()

      val writer = new PrintStream(clientSocket.getOutputStream)
      val reader = new BufferedSource(clientSocket.getInputStream).getLines()

      handle(writer, reader)

      clientSocket.close()
    }
  }


  def handle(writer: PrintStream, reader: Iterator[String]): Unit = {
    reader next match {
      case "login" =>
        val username = reader next
        val password = reader next

        writer.print(Database.login(username, password))
      case "register_app" =>
        val website = reader next
        val description = reader next
        val redirectUrl = reader next

        writer.print(Database.registerApp(website, description, redirectUrl))
      case "register_user" =>
        val username = reader next
        val email = reader next
        val password = reader next

        writer.print(Database.registerUser(username, email, password))
      case _ => writer.print("DU FLOSCHN KONNST KOANE BEFEHLE HEANEHMA HEAST?")
    }
  }
}
