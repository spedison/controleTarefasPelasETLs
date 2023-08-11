package br.com.spedison.controletarefascripts.servico;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

@Component
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
@Profile({"elastic-prod", "elastic-dev"})
public class ContaRegistrosElastic implements ContaRegistros {

    @Value("${elastic.url.contagem.processos}")
    String urlProcessos;

    @Value("${elastic.url.contagem.variaveis}")
    String urlVariaveis;

    @Value("${elastic.user}")
    String username;
    @Value("${elastic.pass}")
    String password;


    @Override
    public Long contagemVariaveis() {
        return contagemUrl(urlVariaveis);
    }

    @Override
    public Long contagemProcessos() {
        return contagemUrl(urlProcessos);
    }

    private Long contagemUrl(String url) {

        log.info("Acessando o Elastic com a URL : " + url);

        String bodyStr = null;
        int retCode = -1;
        try {
            URL urlGet = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlGet.openConnection();
            String encoded = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
            urlConnection.setRequestProperty("Authorization", "Basic " + encoded);
            urlConnection.connect();
            byte[] bodyBts = urlConnection.getInputStream().readAllBytes();
            retCode = urlConnection.getResponseCode();
            bodyStr = new String(bodyBts);
        } catch (IOException ioe) {
            log.error(ioe);
            return -1L;
        }


        // OK quando o retorno for 2XX
        boolean retOk = retCode >= 200 && retCode <= 299;

        if (!retOk) {
            ContaRegistrosElastic.log.error("Problemas ao conectar com Elastic. StatusCode = " + retCode);
            return -1L;
        }

        if (bodyStr.isEmpty()) {
            ContaRegistrosElastic.log.error("Não tinha corpo na resposta");
            return -1L;
        }

        //Exemplo :: "count": 4488,
        final Pattern pattern = Pattern.compile("[ ]?\"count\"[ ]?:[ ]?[0-9]+[ ]?,[ ]?$");

        // Pega a linha com a contagem.
        List<String> linhaList = Arrays.stream(
                        bodyStr.split("\n"))
                .filter(cs -> pattern.matcher(cs).find())
                .toList();

        if (linhaList.isEmpty()) {
            ContaRegistrosElastic.log.error("Corpo sem o dado necessário : " + bodyStr.replace("\n", " "));
            return -1L;
        }

        String quantidadeStr = linhaList
                .get(0)
                .replaceAll("[^0-9]", "")
                .trim();

        try {
            return Long.parseLong(quantidadeStr);
        } catch (NumberFormatException nfe) {
            ContaRegistrosElastic.log.error("Problemas com o ParseLong da quantidade : " + quantidadeStr);
            return -1L;
        }
    }
}
