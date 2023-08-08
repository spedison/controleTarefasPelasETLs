package br.com.spedison.controletarefascripts.vo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "ADMTAREFAS")
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String tipo;

    @Column(nullable = false)
    private Timestamp inicio;
    @Column(nullable = true)
    private Timestamp fim;

    @Column(nullable = false)
    private Long contagemRegistrosProcessosElasticInicioProcessamento;
    @Column(nullable = false)
    private Long contagemRegistrosVariaveisElasticInicioProcessamento;

    @Column(nullable = true)
    private Long contagemRegistrosProcessosElasticFinalProcessamento;
    @Column(nullable = true)
    private Long contagemRegistrosVariaveisElasticFinalProcessamento;

    @Column(nullable = true)
    private Boolean sucesso;

    @OneToMany(mappedBy = "tarefa")
    private List<Atividade> atividade;
}
