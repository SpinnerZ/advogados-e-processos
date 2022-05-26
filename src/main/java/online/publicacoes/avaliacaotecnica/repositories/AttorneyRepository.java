package online.publicacoes.avaliacaotecnica.repositories;

import online.publicacoes.avaliacaotecnica.entities.Attorney;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttorneyRepository extends JpaRepository<Attorney, Long> {

  Optional<Attorney> findByUserName(String userName);
}
