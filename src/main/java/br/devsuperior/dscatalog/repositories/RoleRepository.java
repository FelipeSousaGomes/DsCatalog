package br.devsuperior.dscatalog.repositories;

import br.devsuperior.dscatalog.entities.Role;
import br.devsuperior.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
