package br.com.spedison.controletarefascripts.servico;

import br.com.spedison.controletarefascripts.repository.AtividadeRepository;
import br.com.spedison.controletarefascripts.repository.TarefaRepository;
import br.com.spedison.controletarefascripts.servico.vo.TarefaVisualizacao;
import br.com.spedison.controletarefascripts.vo.Atividade;
import br.com.spedison.controletarefascripts.vo.Tarefa;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
public class TarefaServico {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private AtividadeRepository atividadeRepository;

    @Autowired
    private ContaRegistros contaRegistrosElastic;

    @Transactional
    public Tarefa criaNovaTarefa(String tipoTarefa) {

        Optional<Tarefa> tarefaAbertaOpt = tarefaRepository.procuraTarefaAberta(tipoTarefa);

        if (tarefaAbertaOpt.isPresent()) {
            log.error("Localizada tabefas abertas do tipo %s. Corrija o processo se necessário."
                    .formatted(tipoTarefa));
            return null;
        }

        Tarefa tarefa = new Tarefa();
        tarefa.setTipo(tipoTarefa);
        tarefa.setInicio(new Timestamp(System.currentTimeMillis()));

        //Conta a quantidade de registros no Elastic.
        tarefa.setContagemRegistrosProcessosElasticInicioProcessamento(contaRegistrosElastic.contagemProcessos());
        tarefa.setContagemRegistrosVariaveisElasticInicioProcessamento(contaRegistrosElastic.contagemVariaveis());

        tarefa.setAtividade(new LinkedList<>());

        tarefaRepository.save(tarefa);
        return tarefa;
    }

    @Transactional
    public Tarefa fechaTarefa(String tipoTarefa, Long idTarefa) {

        Optional<Tarefa> tarefa = tarefaRepository.findById(idTarefa);

        if (tarefa.isEmpty()) {
            log.error("Tarefa %d - %s não pode ser fechada pois não foi localizada.".formatted(idTarefa, tipoTarefa));
            return null;
        }

        Optional<Integer> contagem = tarefaRepository.contaAtividadesAbertasDaTarefa(idTarefa);

        if (!tipoTarefa.equalsIgnoreCase(tarefa.get().getTipo())) {
            log.error("Tarefa %d - %s não pode ser fechada pois pois o tipo está diferente : %s.".formatted(idTarefa, tipoTarefa, tarefa.get().getTipo()));
            return null;
        }

        if (contagem.get() != 0) {
            log.error("Tarefa %d - %s não pode ser fechada pois tem atividades pendetes.".formatted(idTarefa, tipoTarefa));
            return null;
        }

        Tarefa ret = tarefa.get();

        if (!Objects.isNull(ret.getFim())) {
            log.error("Tarefa Já foi fechada.");
            return null;
        }

        //Conta a quantidade de registros processados no elastic.
        ret.setContagemRegistrosProcessosElasticFinalProcessamento(contaRegistrosElastic.contagemProcessos());
        ret.setContagemRegistrosVariaveisElasticFinalProcessamento(contaRegistrosElastic.contagemVariaveis());

        boolean sucesso =
                ret.getContagemRegistrosVariaveisElasticFinalProcessamento() > ret.getContagemRegistrosVariaveisElasticInicioProcessamento()
                        && ret.getContagemRegistrosProcessosElasticFinalProcessamento() > ret.getContagemRegistrosProcessosElasticInicioProcessamento();

        boolean alerta =
                ret.getContagemRegistrosVariaveisElasticFinalProcessamento().equals(ret.getContagemRegistrosVariaveisElasticInicioProcessamento())
                        && ret.getContagemRegistrosProcessosElasticFinalProcessamento().equals(ret.getContagemRegistrosProcessosElasticInicioProcessamento());

        if (alerta)
            ret.setSucesso(null);
        else
            ret.setSucesso(sucesso);

        ret.setFim(new Timestamp(System.currentTimeMillis()));

        tarefaRepository.save(ret);
        return ret;
    }


    @Transactional
    public void fecharForcadamenteTarefa(Long idTarefa) {
        Optional<Tarefa> tarefaOpt = tarefaRepository.findById(idTarefa);
        if (tarefaOpt.isEmpty()) {
            log.error("Tarefa não localizada %d".formatted(idTarefa));
            return;
        }

        Tarefa tarefa = tarefaOpt.get();

        for (Atividade ativ : tarefa.getAtividade()) {
            ativ.setFim(new Timestamp(System.currentTimeMillis()));
            ativ.setForcado(true);
        }
        atividadeRepository.saveAll(tarefa.getAtividade());

        tarefa.setFim(new Timestamp(System.currentTimeMillis()));
        tarefa.setContagemRegistrosVariaveisElasticFinalProcessamento(-1L);
        tarefa.setContagemRegistrosProcessosElasticFinalProcessamento(-1L);
        tarefa.setSucesso(false);
        tarefaRepository.save(tarefa);
    }


    public List<TarefaVisualizacao> listaTarefasPendetes() {
        List<Tarefa> listTarefas = tarefaRepository.findByFimEmpty();
        return listTarefas
                .stream()
                .map(t -> {
                    TarefaVisualizacao retMapLocal = new TarefaVisualizacao();
                    retMapLocal.setTarefa(t);
                    retMapLocal.setQuantidadeAtividades(t.getAtividade().size());
                    retMapLocal.setQuantidadeAtividadesTerminadas(
                            (int) (t
                                    .getAtividade()
                                    .stream()
                                    .map(Atividade::getFim)
                                    .filter(Objects::nonNull)
                                    .count())
                    );

                    Function<Atividade, Boolean> comp = (atividade) -> Objects.nonNull(atividade.getFim()) && !atividade.getForcado();

                    retMapLocal.setQuantidadeAtividadesTerminadasSucesso(
                            (int) (t
                                    .getAtividade()
                                    .stream()
                                    .filter(comp::apply)
                                    .count())
                    );
                    return retMapLocal;
                }).toList();
    }

    public List<TarefaVisualizacao> listaTarefasExecutadas() {
        List<Tarefa> listTarefas = tarefaRepository.findByFimNotEmpty();

        return listTarefas
                .stream()
                .map(t -> {
                    TarefaVisualizacao retMapLocal = new TarefaVisualizacao();
                    retMapLocal.setTarefa(t);
                    retMapLocal.setQuantidadeAtividades(t.getAtividade().size());

                    retMapLocal.setQuantidadeAtividadesTerminadas(
                            (int) (t
                                    .getAtividade()
                                    .stream()
                                    .map(Atividade::getFim)
                                    .filter(Objects::nonNull)
                                    .count())
                    );

                    Function<Atividade, Boolean> comp =
                            (atividade) ->
                                    Objects.nonNull(atividade.getFim()) && !atividade.getForcado();

                    retMapLocal.setQuantidadeAtividadesTerminadasSucesso(
                            (int) (t
                                    .getAtividade()
                                    .stream()
                                    .filter(comp::apply)
                                    .count())
                    );
                    return retMapLocal;
                })
                .sorted(
                        (TarefaVisualizacao o1, TarefaVisualizacao o2) ->
                                o1.getTarefa().getInicio().compareTo(o2.getTarefa().getInicio()))
                .toList();
    }
}