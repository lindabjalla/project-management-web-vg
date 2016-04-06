package se.grouprich.projectmanagement.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import se.grouprich.projectmanagement.model.TeamData;

public interface TeamRepository extends CrudRepository<TeamData, Long>
{
	@Query("SELECT t FROM #{#entityName} t LEFT JOIN FETCH t.users WHERE t.id = ?1")
	TeamData findTeamById(Long id);
}
