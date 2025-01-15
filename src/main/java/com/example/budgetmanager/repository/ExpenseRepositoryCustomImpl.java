package com.example.budgetmanager.repository;

import com.example.budgetmanager.entity.Expense;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ExpenseRepositoryCustomImpl {
    @PersistenceContext
    private EntityManager entityManager;


    public List<Expense> findExpensesByDateRanges(List<LocalDate[]> dateRanges) {
        StringBuilder queryBuilder = new StringBuilder("SELECT e FROM Expense e WHERE ");
        List<Object> parameters = new ArrayList<>();

        for (int i = 0; i < dateRanges.size(); i++) {
            LocalDate[] range = dateRanges.get(i);
            queryBuilder.append("(e.date BETWEEN ?").append(parameters.size() + 1).append(" AND ?")
                    .append(parameters.size() + 2).append(")");

            parameters.add(range[0]); // Start date
            parameters.add(range[1]); // End date

            if (i < dateRanges.size() - 1) {
                queryBuilder.append(" OR ");
            }
        }

        Query query = entityManager.createQuery(queryBuilder.toString(), Expense.class);
        for (int i = 0; i < parameters.size(); i++) {
            query.setParameter(i + 1, parameters.get(i));
        }

        return query.getResultList();
    }
}
