package br.com.spedison.controletarefascripts.vo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@Table(schema = "ADMTAREFAS")
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String nome;

    private Timestamp inicio;
    private Timestamp fim;

    private Boolean forcado;

    @ManyToOne(fetch = FetchType.EAGER)
    private Tarefa tarefa;

    public Atividade() {
        forcado = false;
    }
}
