import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    // Порт, на котором будет запущен сервер
    private final int port;

    // Пул потоков для обработки подключений
    private final ExecutorService threadPool;

    public Server(int port) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(64);
    }

    // Метод для запуска сервера
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новое подключение: " + clientSocket.getInetAddress());
                threadPool.submit(() -> handleClient(clientSocket));
            }

        } catch (IOException e) {
            System.err.println("Ошибка при запуске сервера: " + e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) {
        try (InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
                output.flush();
            }

        } catch (IOException e) {
            System.err.println("Ошибка при обработке клиента: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Соединение закрыто: " + clientSocket.getInetAddress());
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }

    public void stop() {
        threadPool.shutdown();
        System.out.println("Сервер остановлен.");
    }

    public static void main(String[] args) {
        Server server = new Server(8080);
        server.start();
    }
}
