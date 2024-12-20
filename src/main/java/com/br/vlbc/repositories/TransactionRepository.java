package com.br.vlbc.repositories;

import com.br.vlbc.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    @Query("SELECT t FROM Transactions t WHERE t.user.id = :idUser")
    List<Transactions> findAllOfIdUser(@Param("idUser") Long idUser);

    @Query("SELECT t FROM Transactions t WHERE t.user.id = :userId AND t.dateOfCreation BETWEEN :startDate AND :endDate")
    List<Transactions> findByUserIdAndDateBetween(@Param("userId") Long userId,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);

}
