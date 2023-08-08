package br.com.spedison.controletarefascripts;

import br.com.spedison.controletarefascripts.servico.TarefaServico;
import br.com.spedison.controletarefascripts.vo.Tarefa;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Log4j2
@Component
public class ControleTarefaMain {
    public Long executa(TarefaServico ts, String[] args) {
        Long id = null;

        if (args[0].equalsIgnoreCase("IniciaIDTarefa")) {

            log.debug("Gerando o ID");

            Tarefa t = ts.criaNovaTarefa(args[1]);

            if (!Objects.isNull(t))
                id = t.getId();
        }
        if (args[0].equalsIgnoreCase("TerminaIDTarefa")) {
            log.debug("Fechando o ID");

            if (args.length != 3) {
                log.error("Problemas com a quantidade de argumentos. (2)");
            } else {

                Tarefa t = ts.fechaTarefa(args[1], Long.parseLong(args[2]));
                if (!Objects.isNull(t)) {
                    id = t.getId();
                }
            }
        }
        return id;
    }

//        SpringApplication.run(ControleTarefaScriptsApplication.class, args).close();
//        System.out.println("done");




}
