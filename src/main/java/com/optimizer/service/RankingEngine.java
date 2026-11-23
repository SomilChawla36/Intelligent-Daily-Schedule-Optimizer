package com.optimizer.service;

import com.optimizer.comparator.StudentRankComparator;
import com.optimizer.model.StudentPerformance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Spring Service to rank students based on their percentages using custom Comparator.
 * Demonstrates:
 * - @Service annotation
 * - Sorting collections with custom Comparator
 * - Accurate rank assignment with tie handling
 */
@Service
public class RankingEngine {

    private final StudentRankComparator rankComparator = new StudentRankComparator();

    /**
     * Sorts the student performance list in descending order of percentage
     * and assigns rank 1, 2, 3...
     * Handles ties: Students with identical percentages receive the same rank.
     *
     * @param performances List of student performance records
     * @return Sorted and ranked list
     */
    public List<StudentPerformance> rankStudents(List<StudentPerformance> performances) {
        if (performances == null || performances.isEmpty()) {
            return Collections.emptyList();
        }

        List<StudentPerformance> rankedList = new ArrayList<>(performances);
        rankedList.sort(rankComparator);

        int currentRank = 1;
        for (int i = 0; i < rankedList.size(); i++) {
            if (i > 0) {
                StudentPerformance prev = rankedList.get(i - 1);
                StudentPerformance curr = rankedList.get(i);

                if (Double.compare(curr.getPercentage(), prev.getPercentage()) != 0) {
                    currentRank = i + 1;
                }
            }
            rankedList.get(i).setRank(currentRank);
        }

        return rankedList;
    }
}
