package br.com.spedison.controletarefascripts.servico;

import br.com.spedison.controletarefascripts.repository.AtividadeRepository;
import br.com.spedison.controletarefascripts.repository.TarefaRepository;
import br.com.spedison.controletarefascripts.vo.Atividade;
import br.com.spedison.controletarefascripts.vo.Tarefa;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class AtividadeServico {

    @Autowired
    AtividadeRepository atividadeRepository;
    @Autowired
    TarefaRepository tarefaRepository;

    @Transactional
    public Atividade criaNovaAtividade(Long idTarefa, String nomeAtividade) {
        Atividade atividade = new Atividade();
        Tarefa tarefa = tarefaRepository.getReferenceById(idTarefa);

        if (Objects.isNull(tarefa)) {
            log.error("Tarefa [%d] está inválida para a atividade [%s].".formatted(idTarefa, nomeAtividade));
        } else {
            if (!Objects.isNull(tarefa.getFim())) {
                log.error("Tarefa [%d] já foi terminada. Problema de Ordem ao adicionar a atividade [%s]".formatted(idTarefa, nomeAtividade));
            } else {

                Optional<Integer> contaAbertosOpt = tarefaRepository.contaAtividadesAbertasDaTarefa(idTarefa);
                Integer contaAbertos = contaAbertosOpt.isEmpty() ? 0 : contaAbertosOpt.get();

                if (contaAbertos != 0) {
                    log.error(
                            "Não pode abrir atividade nova para a tarefa %d, pois já existem atividades abertas."
                                    .formatted(idTarefa));
                } else {
                    tarefa.setId(idTarefa);
                    atividade.setNome(nomeAtividade);
                    atividade.setTarefa(tarefa);
                    atividade.setInicio(new Timestamp(System.currentTimeMillis()));
                    atividade.setForcado(false);
                    atividadeRepository.save(atividade);
                    return atividade;
                }
            }
        }
        return null;
    }

    @Transactional
    public Atividade fechaAtividade(Long idTarefa, String nomeAtividade) {

        Optional<Atividade> atividade = atividadeRepository.procuraAtividadeAberta(nomeAtividade);

        if (atividade.isEmpty()) {
            log.error("Na tarefa %d, não existe nenhuma atividade pendente para fechar a atividade %s ".formatted(
                    idTarefa, nomeAtividade
            ));
        } else {
            Atividade ret = atividade.get();

            if (!Objects.isNull(ret.getTarefa().getFim())) {
                log.error("A tarefa dessa atividade já foi fechada. Inconsistência nas atividades.");
            } else if (ret.getTarefa().getId() != idTarefa) {
                log.error("A tarefa %d tem id diferente da atividade pendente.".formatted(idTarefa));
            } else {
                ret.setFim(new Timestamp(System.currentTimeMillis()));
                ret.setForcado(false);
                atividadeRepository.save(ret);
                return ret;
            }
        }

        return null;
    }

    @Transactional
    public boolean fechaAtividadeForcada(Long idTarefa) {

        List<Atividade> atividades = atividadeRepository.procuraAtividadesAbertasDaTarefa(idTarefa);

        if (atividades.isEmpty()) {
            log.info("Na tarefa %d, não existe nenhuma atividade pendente para fechar ".formatted(
                    idTarefa
            ));
            return false;
        }

        for (Atividade ret : atividades) {
            ret.setFim(new Timestamp(System.currentTimeMillis()));
            ret.setForcado(true);
            atividadeRepository.save(ret);
        }
        return true;
    }
}