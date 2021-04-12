import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  public static void main(String[] args) throws IOException {
    // Занимаем порт, определяя серверный сокет
    ServerSocket servSocket = new ServerSocket(23444);
    while (true) {
      // Ждем подключения клиента и получаем потоки для дальнейшей работы
      try (Socket socket = servSocket.accept();
           PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
           BufferedReader in = new BufferedReader(new
                   InputStreamReader(socket.getInputStream()))) {
        String line;
        while ((line = in.readLine()) != null) {
          // Выход если от клиента получили end
          if (line.equals("end")) {
            break;
          } else {
            // Пишем ответ
            out.println(line + "-е число Фибоначчи = " + getFib2(Integer.parseInt(line)));
          }
        }
      } catch (IOException ex) {
        ex.printStackTrace(System.out);
      }
    }
  }

  // рекурсия
  public static BigInteger getFib(int n) {
    if (n == 0) {
      return BigInteger.valueOf(0);
    } else if (n <= 2) {
      return BigInteger.valueOf(1);
    }
    return getFib(n - 1).add(getFib(n - 2));
  }

  // итерация
  public static BigInteger getFib2(int n) {
    BigInteger prev = BigInteger.valueOf(0), next = BigInteger.valueOf(1);
    for (int i = 0; i < n; i++) {
      BigInteger tmp = next;
      next = prev.add(next);
      prev = tmp;
    }
    return prev;
  }
}
