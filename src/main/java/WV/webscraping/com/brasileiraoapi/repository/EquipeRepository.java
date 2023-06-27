package wv.webscraping.com.brasileiraoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import wv.webscraping.com.brasileiraoapi.entites.Equipe;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long>{

}
