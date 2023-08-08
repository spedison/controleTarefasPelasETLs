package br.com.spedison.controletarefascripts.repository;

import br.com.spedison.controletarefascripts.vo.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    @Query("select a from Tarefa a where a.fim is null")
    public Optional<Tarefa> procuraTarefaAberta();

    @Query("select t from Tarefa t where t.fim is null and t.tipo = :tipo")
    public Optional<Tarefa> procuraTarefaAberta(String tipo);

    @Query("select count(a) from Atividade a where a.tarefa.id = :idTarefa and a.fim is null")
    public Optional<Integer> contaAtividadesAbertasDaTarefa(Long idTarefa);
}