package com.noxsid.nks.crmai.repository;

import com.noxsid.nks.crmai.data.Status;
import com.noxsid.nks.crmai.data.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("""
            SELECT t FROM Task t inner join User u
            on t.user.id = u.id
            where t.user.id = :userId
            """)
    List<Task> findAllByUser(Long userId);

    @Query("""
            SELECT t FROM Task t inner join User u
            on t.user.id = u.id
            where t.user.id = :userId and t.status = :status
            """)
    List<Task> findByUserStatus(Long userId, Status status);

}
