package br.com.spedison.controletarefascripts.servico;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
@Log4j2
public class ContaRegistrosElastic {

    @Value("${elastic.url.contagem.processos}")
    String urlProcessos;

    @Value("${elastic.url.contagem.variaveis}")
    String urlVariaveis;

    public Long contagemVariaveis() {
        return contagemUrl(urlVariaveis);
    }

    public Long contagemProcessos() {
        return contagemUrl(urlProcessos);
    }

    private Long contagemUrl(String url) {

        System.out.println("Carregrando Main ... ");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET() // GET is default
                .build();

        HttpResponse<Void> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.discarding());
        } catch (InterruptedException ie) {
            log.error("Conexao interrompida " + ie.getMessage());
            return -1L;
        } catch (IOException ioe) {
            log.error("Problemas na comunicação " + ioe.getMessage());
            return -1L;
        }

        // OK quando o retorno for 2XX
        boolean retOk = response.statusCode() >= 200 && response.statusCode() <= 299;

        if (!retOk) {
            log.error("Problemas ao conectar com Elastic. StatusCode = " + response.statusCode());
            return -1L;
        }

        if (response.request().bodyPublisher().isEmpty()) {
            log.error("Não tinha corpo na resposta");
            return -1L;
        }

        //Exemplo :: "count": 4488,
        final Pattern pattern = Pattern.compile("[ ]?\"count\"[ ]?:[ ]?[0-9]+[ ]?,[ ]?$");
        final String corpoResposta = response
                .request()
                .bodyPublisher()
                .get()
                .toString();

        // Pega a linha com a contagem.
        List<String> linhaList = Arrays.stream(
                        corpoResposta.split("\n"))
                .filter(cs -> pattern.matcher(cs).find())
                .toList();

        if (linhaList.isEmpty()) {
            log.error("Corpo sem o dado necessário : " + corpoResposta.replace("\n", " "));
            return -1L;
        }

        String quantidadeStr = linhaList
                .get(0)
                .replaceAll("[^0-9]", "")
                .trim();

        try {
            return Long.parseLong(quantidadeStr);
        } catch (NumberFormatException nfe) {
            log.error("Problemas com o ParseLong da quantidade : " + quantidadeStr);
            return -1L;
        }

//        System.out.println(response.uri());
//// ------------------------------
//
//        System.out.println("---------------------------------------");
//
//        // Create a URL object for the website you want to GET from.
//        URL url = new URL("https://spedison:12345678@127.0.0.1:9200/dados_abertos/_count");
//
//        // Open a connection to the website.
//        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//        // Set the request method to GET.
//        connection.setRequestMethod("GET");
//        connection.setRequestProperty("kbn-xsrf", "reporting");
//        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setHostnameVerifier((hostname, session) -> true);
//        // Set the request body.
//        // Set the request body.
//        connection.setDoOutput(true);
//        OutputStream os = connection.getOutputStream();
//        os.write("This is the request body.".getBytes());
//        os.flush();
//
//        // Send the request.
//        connection.connect();
//
//        // Get the response code.
//        int responseCode = connection.getResponseCode();
//
//        // Check the response code.
//        if (responseCode == 200) {
//            // The request was successful.
//
//            // Get the response body.
//            InputStream inputStream = connection.getInputStream();
//            byte[] responseBody = new byte[inputStream.available()];
//            inputStream.read(responseBody);
//
//            // Print the response body.
//            System.out.println(new String(responseBody));
//        } else {
//            // The request was not successful.
//
//            // Print the response code.
//            System.out.println("Response code: " + responseCode);
//        }
//        return 0L;
    }

//    public static void main(String[] args) throws IOException, InterruptedException {
//        ContaRegistrosElastic cre = new ContaRegistrosElastic();
//        cre.contagem();
//    }

}
