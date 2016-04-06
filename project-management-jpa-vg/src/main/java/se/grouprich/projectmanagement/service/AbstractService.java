package se.grouprich.projectmanagement.service;

import com.google.common.collect.Iterables;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import se.grouprich.projectmanagement.exception.InvalidValueException;
import se.grouprich.projectmanagement.exception.RepositoryException;
import se.grouprich.projectmanagement.model.AbstractEntityData;

abstract class AbstractService<E extends AbstractEntityData, R extends CrudRepository<E, Long>>
{
	R superRepository;
	private Class<E> classType;

	AbstractService(final R superRepository, final Class<E> classType)
	{
		this.superRepository = superRepository;
		this.classType = classType;
	}

	public E createOrUpdate(final E entity) throws InvalidValueException
	{
		return superRepository.save(entity);
	}

	public E findById(final Long id) throws RepositoryException
	{
		final E entity = superRepository.findOne(id);
		if (entity == null)
		{
			throw new RepositoryException(classType.getSimpleName().replace("Data", "") + " with id: " + id + " was not found");
		}

		return entity;
	}

	@Transactional
	public E deleteById(final Long id) throws RepositoryException, InvalidValueException
	{
		final E entity = findById(id);
		superRepository.delete(id);
		return entity;
	}

	public Iterable<E> findAll() throws RepositoryException
	{
		final Iterable<E> entities = superRepository.findAll();
		if (Iterables.isEmpty(entities))
		{
			throw new RepositoryException("No entity was found");
		}
		return entities;
	}
}