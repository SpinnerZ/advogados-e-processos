package online.publicacoes.avaliacaotecnica.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import online.publicacoes.avaliacaotecnica.dto.ProcessDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_process")
@Builder
public class Process {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @EqualsAndHashCode.Include private Long number;
  private String defendant;
  private Boolean archived;

  @ManyToOne
  @JoinColumn(name = "lawyer_id", nullable = false)
  private Lawyer lawyer;

  public ProcessDTO toDTO() {

    return ProcessDTO.builder()
        .defendant(defendant)
        .archived(archived)
        .lawyerUsername(lawyer.getUsername())
        .build();
  }
}
