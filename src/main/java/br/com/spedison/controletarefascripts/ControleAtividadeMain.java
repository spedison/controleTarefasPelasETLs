package br.com.spedison.controletarefascripts;

import br.com.spedison.controletarefascripts.repository.AtividadeRepository;
import br.com.spedison.controletarefascripts.servico.AtividadeServico;
import br.com.spedison.controletarefascripts.vo.Atividade;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Log4j2
@Component
public class ControleAtividadeMain {
    public Long executa(AtividadeServico as, String[] args) {
        return switch (args[0].toLowerCase().trim()) {
            case "iniciaatividade" ->
                    iniciaAtividade(args, as.criaNovaAtividade(Long.parseLong(args[1]), args[2]));
            case "terminaatividade" ->
                    iniciaAtividade(args, as.fechaAtividade(Long.parseLong(args[1]), args[2]));
            case "listaatividades" -> listAtividades(args, as);
            default -> null;
        };
    }

    private Long listAtividades(String[] args, AtividadeServico as) {
        Long id = null;
        List<Atividade> atividades = as.listaAtividadesTarefa(Long.parseLong(args[1]));
        log.info("ID           Nome                                     Inicio                  Fim                     FechadoForcado");
        for (Atividade a : atividades) {
            log.info("%010d   %-40s %-23s %-23s %s"
                    .formatted(a.getId(), a.getNome(), a.getInicio(), a.getFim(), a.getForcado() ? "Sim" : "Não"));
        }
        Integer a = Objects.nonNull(atividades) ? atividades.size() : 0;
        id = a.longValue();
        return id;
    }

    private Long iniciaAtividade(String[] args, Atividade as) {
        Long id = null;
        if (args.length != 3) {
            log.error("Problemas com a quantidade de argumentos. (3)");
        } else {
            log.debug("Iniciando a Atividade [%s] ta tarefa %s".formatted(args[1], args[2]));
            Atividade a = as;
            if (!Objects.isNull(a))
                id = a.getId();
        }
        return id;
    }
}
