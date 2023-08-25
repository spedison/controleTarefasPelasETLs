package br.com.spedison.controletarefascripts;

import br.com.spedison.controletarefascripts.servico.TarefaServico;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ControleConexaoMain {

    private Long testaConexaoBanco(TarefaServico ts) {
        Long ret = 1L;
        try {
            ts.listaTarefasExecutadas();
            log.info("Listagem executada com sucesso");
        } catch (RuntimeException re) {
            log.error("Problemas ao executar listagem das tarefas RTE :: ", re);
            ret = null;
        } catch (Exception ex) {
            log.error("Problemas ao executar listagem das tarefas EX :: ", ex);
            ret = null;
        }
        return ret;
    }

    public Long executa(TarefaServico ts, String[] args) {
        return switch (args[0].toLowerCase()) {
            case "testarconexaobanco" -> testaConexaoBanco(ts);
            default -> null;
        };
    }
}
