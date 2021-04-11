import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
  // выбран NonBlocking подход, чтоб пока выполняются тяжелые вычисления не блокировать основной поток
  public static void main(String[] args) throws IOException {
    // Занимаем порт, определяя серверный сокет
    final ServerSocketChannel serverChannel = ServerSocketChannel.open();
    serverChannel.bind(new InetSocketAddress("localhost", 23334));
    while (true) {
      // Ждем подключения клиента и получаем потоки для дальнейшей работы
      try (SocketChannel socketChannel = serverChannel.accept()) {
        // Определяем буфер для получения данных
        final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
        while (socketChannel.isConnected()) {
          // читаем данные из канала в буфер
          int bytesCount = socketChannel.read(inputBuffer);
          // если из потока читать нельзя, перестаем работать с этим клиентом
          if (bytesCount == -1) break;
          // получаем переданную от клиента строку в нужной кодировке и очищаем буфер
          final String msg = new String(inputBuffer.array(), 0, bytesCount,
                  StandardCharsets.UTF_8);
          inputBuffer.clear();
          System.out.println("Получено от клиента: " + msg);
          BigInteger number = getFib2(Integer.parseInt(msg));
          // отправляем результат
          socketChannel.write(ByteBuffer.wrap((msg + "-е число Фибоначчи = " +
                  number).getBytes(StandardCharsets.UTF_8)));
        }
      } catch (IOException err) {
        System.out.println(err.getMessage());
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
