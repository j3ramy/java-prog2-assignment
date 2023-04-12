package util;

import util.data.AudienceReview;

import java.util.ArrayList;
import java.util.HashMap;

public class Algorithms {
    public static void countingSort(ArrayList<AudienceReview> arr) {
        int n = arr.size();
        float maxRating = getMaxRating(arr);

        // Erstellen des Count-Arrays und Zählen der Werte
        ArrayList<AudienceReview>[] count = new ArrayList[(int) (maxRating * 10) + 1];
        for (int i = 0; i < count.length; i++) {
            count[i] = new ArrayList<>();
        }

        for (AudienceReview audienceReview : arr) {
            int index = (int) (audienceReview.getRating() * 10);
            count[index].add(audienceReview);
        }

        // Erstellen des Output-Arrays und Sortieren der Werte
        ArrayList<AudienceReview> output = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            output.add(null);
        }

        int k = 0;
        for (ArrayList<AudienceReview> reviews : count) {
            for (AudienceReview review : reviews) {
                output.set(k++, review);
            }
        }

        // Kopieren des Output-Arrays in die ursprüngliche ArrayList
        for (int i = 0; i < n; i++) {
            arr.set(i, output.get(i));
        }
    }

    private static float getMaxRating(ArrayList<AudienceReview> arr){
        float maxFloat = 0f;

        for(AudienceReview review : arr){
            if(review.getRating() > maxFloat)
                maxFloat = review.getRating();
        }

        return maxFloat;
    }
}
