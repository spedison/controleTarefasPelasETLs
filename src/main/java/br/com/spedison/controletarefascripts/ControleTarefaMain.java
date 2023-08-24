package br.com.spedison.controletarefascripts;

import br.com.spedison.controletarefascripts.servico.TarefaServico;
import br.com.spedison.controletarefascripts.servico.vo.TarefaVisualizacao;
import br.com.spedison.controletarefascripts.vo.Tarefa;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
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
        } else if (args[0].equalsIgnoreCase("TerminaIDTarefa")) {
            log.debug("Fechando o ID");

            if (args.length != 3) {
                log.error("Problemas com a quantidade de argumentos. (2)");
            } else {

                Tarefa t = ts.fechaTarefa(args[1], Long.parseLong(args[2]));
                if (!Objects.isNull(t)) {
                    id = t.getId();
                }
            }
        } else if (args[0].equalsIgnoreCase("FechaForcadoTarefa")) {
            ts.fecharForcadamenteTarefa(Long.parseLong(args[1]));
        } else if (args[0].equalsIgnoreCase("tempoUltimaExecucaoTarefa")){
            id = ts.tempoHorasUltimoSucesso(args[1]);
        } else if (args[0].equalsIgnoreCase("ListaTarefa")) {
            List<TarefaVisualizacao> tarefasPendentes = ts.listaTarefasPendetes();
            List<TarefaVisualizacao> tarefasExecutadas = ts.listaTarefasExecutadas();


            //"%05d\t%-40s\t%10d\t%3s\t%3s\t%03d\t%03d\t%03d".
            log.info("ID        = ID da Tarefa");
            log.info("Nome/Tipo = Nome da Tarefa");
            log.info("Tempo(Min)= Tempo gasto pela tarefa em minutos");
            log.info("Ter       = Se a tarefa terminou sim ou nao");
            log.info("Suc       = Se foi Terminada com Sucesso sim ou nao");
            log.info("#At       = Quantidade de atividades iniciadas na tarefa");
            log.info("#AT       = Quantidade de atividades iniciadas terminadas");
            log.info("#ATSuc    = Quantidade de atividades iniciadas terminadas com sucesso");
            System.out.println(" ID  \tNome/Tipo                               \tTempo(Min)\tTer\tSuc\t#At\t#AT\t#ATSuc");
            tarefasExecutadas
                    .stream()
                    .map(TarefaVisualizacao::toString)
                    .forEach(System.out::println);
            tarefasPendentes
                    .stream()
                    .map(TarefaVisualizacao::toString)
                    .forEach(System.out::println);
            id = -1L;
        }
        return id;
    }
}
