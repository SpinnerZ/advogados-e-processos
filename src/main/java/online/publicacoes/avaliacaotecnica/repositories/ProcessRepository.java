package online.publicacoes.avaliacaotecnica.repositories;

import online.publicacoes.avaliacaotecnica.entities.Lawyer;
import online.publicacoes.avaliacaotecnica.entities.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProcessRepository extends JpaRepository<Process, Long> {

  Boolean existsByNumber(Long number);

  Optional<Process> findByNumber(Long number);

  Long deleteByNumber(Long number);

  @Query(
      "SELECT process FROM Process process"
          + " WHERE (:lawyer is null or process.lawyer = :lawyer)"
          + " and (:archived is null or process.archived = :archived)")
  List<Process> findAllByLawyerAndArchived(
      @Param("lawyer") Lawyer lawyer, @Param("archived") Boolean archived);
}
