package util;

import util.data.AudienceReview;

import java.util.ArrayList;

public class Algorithms {
    public static void quickSortAudienceReviews(ArrayList<AudienceReview> reviews, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partitionAudienceReview(reviews, begin, end);

            quickSortAudienceReviews(reviews, begin, partitionIndex - 1);
            quickSortAudienceReviews(reviews, partitionIndex + 1, end);
        }
    }

    private static int partitionAudienceReview(ArrayList<AudienceReview> reviews, int begin, int end) {
        float pivot = reviews.get(end).getRating();
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (reviews.get(j).getRating() <= pivot) {
                i++;

                AudienceReview swapTemp = reviews.get(i);
                reviews.set(i, reviews.get(j));
                reviews.set(j, swapTemp);
            }
        }

        AudienceReview swapTemp = reviews.get(i + 1);
        reviews.set(i + 1, reviews.get(end));
        reviews.set(end, swapTemp);

        return i + 1;
    }
}
