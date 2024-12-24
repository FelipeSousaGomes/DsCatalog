package br.devsuperior.dscatalog.repositories;

import br.devsuperior.dscatalog.entities.Product;
import br.devsuperior.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
