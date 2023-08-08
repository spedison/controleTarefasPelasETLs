package br.com.spedison.controletarefascripts;

import br.com.spedison.controletarefascripts.servico.AtividadeServico;
import br.com.spedison.controletarefascripts.vo.Atividade;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Log4j2
@Component
public class ControleAtividadeMain {
    public Long executa(AtividadeServico as, String[] args) {
        Long id = null;
        if (args[0].equalsIgnoreCase("IniciaAtividade")) {

            if (args.length != 3) {
                log.error("Problemas com a quantidade de argumentos. (3)");
            } else {
                log.debug("Iniciando a Atividade [%s] ta tarefa %s".formatted(args[1], args[2]));
                Atividade a = as.criaNovaAtividade(Long.parseLong(args[1]), args[2]);
                if (!Objects.isNull(a))
                    id = a.getId();
            }
        }
        if (args[0].equalsIgnoreCase("TerminaAtividade")) {

            if (args.length != 3) {
                log.error("Problemas com a quantidade de argumentos. (3)");
            } else {
                log.debug("Iniciando a Atividade [%s] ta tarefa %s".formatted(args[1], args[2]));
                Atividade a = as.fechaAtividade(Long.parseLong(args[1]), args[2]);
                if (!Objects.isNull(a))
                    id = a.getId();
            }

        }
        return id;
    }
}
