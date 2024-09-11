import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Сервер запущен на порту 8080");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream()) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String requestLine = reader.readLine();

            if (requestLine != null && !requestLine.isEmpty()) {
                // Извлекаем URL из строки запроса (например, "GET /messages?last=10 HTTP/1.1")
                String[] requestParts = requestLine.split(" ");
                String url = requestParts[1];

                Request request = new Request(url);

                if (request.getPath().equals("/messages")) {
                    String last = request.getQueryParam("last");

                    output.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                    output.write(("Last message: " + last).getBytes());
                } else {
                    output.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
                }

                output.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
