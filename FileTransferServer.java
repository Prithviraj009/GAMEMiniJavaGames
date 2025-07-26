import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;

public class FileTransferServer {
    public static void main(String[] args) throws IOException {
        // Set up the server on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        System.out.println("Server started at http://localhost:8000");

        // Handle file upload requests
        server.createContext("/upload", new FileUploadHandler());

        // Start the server
        server.setExecutor(null); // Use the default executor
        server.start();
    }

    static class FileUploadHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                // Save the uploaded file
                InputStream inputStream = exchange.getRequestBody();
                File file = new File("uploaded_file_" + System.currentTimeMillis());
                FileOutputStream fos = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                fos.close();
                inputStream.close();

                // Send a response
                String response = "File uploaded successfully: " + file.getName();
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Send a response for non-POST requests
                String response = "Use POST to upload files.";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}
