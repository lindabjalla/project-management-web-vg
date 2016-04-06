package se.grouprich.projectmanagement.repository;

import org.springframework.data.repository.CrudRepository;
import se.grouprich.projectmanagement.model.TeamData;
import se.grouprich.projectmanagement.model.UserData;

import java.util.List;

public interface UserRepository extends CrudRepository<UserData, Long>
{
	UserData findByControlId(String controlId);

	List<UserData> findAllByFirstNameOrLastNameOrUsername(String firstName, String lastName, String username);

	List<UserData> findByTeam(TeamData team);
}
