package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.FileModel;
import com.example.budgetmanager.entity.Budget;
import com.example.budgetmanager.entity.Expense;
import com.example.budgetmanager.repository.ExpenseRepository;
import com.example.budgetmanager.repository.ExpenseRepositoryCustomImpl;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.budgetmanager.entity.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseRepositoryCustomImpl expenseRepositoryCustom;
    @Autowired
    private CsvService csvService;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, ExpenseRepositoryCustomImpl expenseRepositoryCustom) {
        this.expenseRepository = expenseRepository;
        this.expenseRepositoryCustom = expenseRepositoryCustom;
    }

    @Autowired
    private UserRepository userRepository; // Repository pentru User

    @Autowired
    private BudgetService budgetService;

    // Adăugarea unei noi cheltuieli
    public Expense addExpense(Expense expense) {
        // Verificăm dacă utilizatorul există
        Optional<User> user = userRepository.findById(expense.getUserId());
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }

        // Salvăm cheltuiala
        Expense savedExpense = expenseRepository.save(expense);

        // Conversia sumei cheltuielii din Double în BigDecimal
        BigDecimal expenseAmount = BigDecimal.valueOf(expense.getAmount());

        // Apelul metodei pentru actualizarea bugetului
        budgetService.updateBudgetAfterExpense(expense.getUserId(), expenseAmount);

        return savedExpense;
    }

    // Obținerea tuturor cheltuielilor
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    // Obținerea unei cheltuieli după ID
    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    // Obținerea cheltuielilor unui utilizator
    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    // Calcularea totalului cheltuielilor
    public Double getTotalExpensesByUserId(Long userId) {
        List<Expense> expenses = getExpensesByUserId(userId);

        // Obține luna și anul curent
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();

        // Filtrăm cheltuielile pentru a include doar pe cele din luna și anul curent
        return expenses.stream()
                .filter(expense -> expense.getDate().getMonthValue() == currentMonth
                        && expense.getDate().getYear() == currentYear)
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    // Actualizarea unei cheltuieli pentru un utilizator specific
    public Expense updateExpense(Long userId, Long id, Expense updatedExpense) {
        Optional<Expense> existingExpenseOpt = expenseRepository.findById(id);
        if (existingExpenseOpt.isPresent()) {
            Expense existingExpense = existingExpenseOpt.get();

            // Verificăm dacă cheltuiala aparține utilizatorului
            if (!existingExpense.getUserId().equals(userId)) {
                throw new RuntimeException("User not authorized to update this expense");
            }

            // Obținem utilizatorul pentru actualizarea bugetului
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Calculăm diferența dintre suma veche și suma nouă
            BigDecimal oldAmount = BigDecimal.valueOf(existingExpense.getAmount());
            BigDecimal newAmount = BigDecimal.valueOf(updatedExpense.getAmount());
            BigDecimal difference = newAmount.subtract(oldAmount);

            // Actualizăm bugetul rămas
            Budget budget = user.getBudget();
            BigDecimal updatedRemainingAmount = budget.getRemainingAmount().subtract(difference);
            budget.setRemainingAmount(updatedRemainingAmount);
            userRepository.save(user);

            // Actualizăm câmpurile cheltuielii
            existingExpense.setName(updatedExpense.getName());
            existingExpense.setAmount(updatedExpense.getAmount());
            existingExpense.setCategory(updatedExpense.getCategory());
            existingExpense.setDate(updatedExpense.getDate());

            // Salvăm cheltuiala actualizată
            return expenseRepository.save(existingExpense);
        } else {
            throw new RuntimeException("Expense not found");
        }
    }

    //grupează cheltuielile după categorie și returnează toate detaliile necesare
    public Map<String, List<Expense>> getExpensesGroupedByCategory(Long userId) {
        List<Expense> expenses = expenseRepository.findByUserId(userId);

        if (expenses == null || expenses.isEmpty()) {
            throw new RuntimeException("No expenses found for user with ID: " + userId);
        }

        // Convertim categoria (CategoryEnum) în String pentru a se potrivi cu Map<String, List<Expense>>
        return expenses.stream()
                .collect(Collectors.groupingBy(expense -> expense.getCategory().toString()));
    }

    // Ștergerea unei cheltuieli pentru un utilizator specific
    public void deleteExpense(Long userId, Long id) {
        Optional<Expense> existingExpense = expenseRepository.findById(id);
        if (existingExpense.isPresent()) {
            Expense expense = existingExpense.get();

            // Verificăm dacă cheltuiala aparține utilizatorului
            if (!expense.getUserId().equals(userId)) {
                throw new RuntimeException("User not authorized to delete this expense");
            }

            // Actualizează bugetul utilizatorului înainte de ștergere
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Conversii și actualizări pentru BigDecimal
            BigDecimal remainingAmount = user.getBudget().getRemainingAmount();
            BigDecimal expenseAmount = BigDecimal.valueOf(expense.getAmount());
            BigDecimal updatedRemainingAmount = remainingAmount.add(expenseAmount);

            user.getBudget().setRemainingAmount(updatedRemainingAmount);
            userRepository.save(user);

            // Șterge cheltuiala
            expenseRepository.deleteById(id);
        } else {
            throw new RuntimeException("Expense not found");
        }
    }
    //Afisarea cheltuielilor dupa categorie specifica
    public Map<String, Double> getTotalExpensesByCategory(Long userId) {
        List<Object[]> results = expenseRepository.findTotalExpensesByCategory(userId);
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Object[] result : results) {
            categoryTotals.put(result[0].toString(), (Double) result[1]);
        }
        return categoryTotals;
    }

    //Afisarea cheltuielilor pentru o luna specifica
    public Map<Integer, Double> getTotalExpensesByMonth(Long userId) {
        List<Expense> expenses = expenseRepository.findByUserId(userId);

        if (expenses == null || expenses.isEmpty()) {
            return new HashMap<>(); // Returnează un map gol dacă nu sunt cheltuieli
        }

        return expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getDate().getMonthValue(),
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    public BigDecimal calculateTotalExpenses(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        BigDecimal totalExpenses = BigDecimal.ZERO;

        for (Expense expense : expenses) {
            totalExpenses = totalExpenses.add(BigDecimal.valueOf(expense.getAmount()));
        }

        return totalExpenses;
    }

    public List<Expense> getExpenseByMonths( List<String> selectedMonths) {
        LocalDate currentDate = LocalDate.now(); // Data curentă
        int currentYear = currentDate.getYear(); // Anul curent
        int currentMonthValue = currentDate.getMonthValue(); // Luna curentă

        List<LocalDate[]> dateRanges = new ArrayList<>();

        for (String selectedMonth : selectedMonths) {
            // Convertim luna din input într-un obiect Month
            String monthStr = selectedMonth.toUpperCase(); // Convertim în majuscule
            Month month;
            try {
                month = Month.valueOf(monthStr);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Luna '" + monthStr + "' nu este validă.");
            }

            // Calculăm anul pe baza lunii curente
            int year = currentYear;
            if (month.getValue() > currentMonthValue) {
                year--; // Dacă luna este înainte de luna curentă, folosim anul anterior
            }

            // Construim intervalul de început și sfârșit pentru lună
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate startOfMonth = yearMonth.atDay(1);
            LocalDate endOfMonth = yearMonth.atEndOfMonth();

            dateRanges.add(new LocalDate[]{startOfMonth, endOfMonth});
        }

        // Apelăm metoda din repository cu intervalele calculate
        return expenseRepositoryCustom.findExpensesByDateRanges(dateRanges);
    }

    public List<String[]> getExpensesRows(List<Expense> allExpenses){
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Name","Category","Date","Amount"});

        for (Expense expense : allExpenses) {
            rows.add(new String[]{expense.getName(),expense.getCategory().toString(),expense.getDate().toString(),expense.getAmount().toString()});
        }
        return rows;
    }

    public FileModel GetExpensesCsv(List<String> selectedMonths){
        List<Expense> expenses = getExpenseByMonths(selectedMonths);
        List<String[]> data = getExpensesRows(expenses);
        return csvService.generateCsvBase64(data);
    }

}
