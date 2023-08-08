package br.com.spedison.controletarefascripts.repository;

import br.com.spedison.controletarefascripts.vo.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

    @Query("select a from Atividade a where a.fim is null")
    Optional<Atividade> procuraAtividadeAberta();

    @Query("select a from Atividade a where a.fim is null and a.nome = :nome")
    Optional<Atividade> procuraAtividadeAberta(String nome);

    @Query("select a from Atividade a where a.fim is null and a.tarefa.id = :idTarefa")
    List<Atividade> procuraAtividadesAbertasDaTarefa(Long idTarefa);
}
