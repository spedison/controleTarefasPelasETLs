package br.com.spedison.controletarefascripts.servico.vo;

import br.com.spedison.controletarefascripts.vo.Tarefa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Data
@NoArgsConstructor
public class TarefaVisualizacao {

    private Tarefa tarefa;
    private int quantidadeAtividades;
    private int quantidadeAtividadesTerminadas;
    private int quantidadeAtividadesTerminadasSucesso;

    Long getTempoMinutos() {
        if (Objects.nonNull(tarefa.getFim())) {
            return ((tarefa.getFim().getTime() - tarefa.getInicio().getTime()) / 10_000) / 6;
        } else {
            return ((System.currentTimeMillis() - tarefa.getInicio().getTime()) / 10_000) / 6;
        }
    }

    @Override
    public String toString() {
        // Formato : Nome - Tempo de Execução - Terminada - Sucesso - Quantidade de Atividades - Quantidade Atividades Terminadas - Quantidade Atividades Terminadas com Sucesso.
        return "%05d\t%-40s\t%10d\t%3s\t%3s\t%03d\t%03d\t%03d".
                formatted(
                        tarefa.getId(),
                        tarefa.getTipo(),
                        getTempoMinutos(),
                        Objects.nonNull(tarefa.getFim()) ? "Sim" : "Não",
                        (Objects.isNull(tarefa.getSucesso()) ? "?" : (tarefa.getSucesso() ? "Sim" : "Não")),
                        quantidadeAtividades,
                        quantidadeAtividadesTerminadas,
                        quantidadeAtividadesTerminadasSucesso
                );
    }
}