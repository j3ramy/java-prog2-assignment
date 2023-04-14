package util;

import mvc.AppModel;
import util.data.AudienceReview;
import util.data.Medium;

import java.text.DecimalFormat;
import java.time.Year;
import java.util.*;

public class Algorithms {

    public static void sortMediumsByRating(ArrayList<Medium> mediums, AppModel appModel){
        HashMap<Integer, ArrayList<Medium>> mediumsGroupedByRelease = new HashMap<>();
        for(Medium medium : mediums){
            //if(appModel.getAudienceReviewsByTitle(medium.getTitle()).isEmpty())
            //    continue;

            if(!mediumsGroupedByRelease.containsKey(medium.getReleaseYear()))
                mediumsGroupedByRelease.put(medium.getReleaseYear(), new ArrayList<>(List.of(medium)));
            else
                mediumsGroupedByRelease.get(medium.getReleaseYear()).add(medium);
        }

        for(Map.Entry<Integer, ArrayList<Medium>> entry : mediumsGroupedByRelease.entrySet()){
            Comparator<Medium> comparator = (medium1, medium2) -> {
                float bestReview1 = Algorithms.getMaxRating(appModel.getAudienceReviewsByTitle(medium1.getTitle()));
                float bestReview2 = Algorithms.getMaxRating(appModel.getAudienceReviewsByTitle(medium2.getTitle()));
                return Float.compare(bestReview1, bestReview2);
            };

            entry.getValue().sort(comparator);
        }

        mediums.clear();
        for(ArrayList<Medium> list : mediumsGroupedByRelease.values()){
            mediums.addAll(list);
        }
    }

    //Counting sort
    public static void sortMediumsByReleaseYear(ArrayList<Medium> mediums) {
        // Find the maximum release year in the list
        int maxReleaseYear = Year.now().getValue();

        // Initialize the counting array
        int[] count = new int[maxReleaseYear + 1];
        for (Medium medium : mediums) {
            count[medium.getReleaseYear()]++;
        }

        // Calculate the cumulative count array
        for (int i = 1; i <= maxReleaseYear; i++) {
            count[i] += count[i - 1];
        }

        // Create the sorted array
        Medium[] sorted = new Medium[mediums.size()];
        for (int i = mediums.size() - 1; i >= 0; i--) {
            Medium medium = mediums.get(i);
            int index = count[medium.getReleaseYear()] - 1;
            sorted[index] = medium;
            count[medium.getReleaseYear()]--;
        }

        // Copy the sorted array back into the original list
        mediums.clear();
        mediums.addAll(Arrays.asList(sorted));
    }

    public static void sortAudienceReviewsByRating(ArrayList<AudienceReview> arr) {
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

    public static float getMaxRating(ArrayList<AudienceReview> arr){
        float maxFloat = 0f;

        for(AudienceReview review : arr){
            if(review.getRating() > maxFloat)
                maxFloat = review.getRating();
        }

        return maxFloat;
    }

    public static float getAverageRating(ArrayList<AudienceReview> arr){
        float sum = 0f;

        for(AudienceReview review : arr){
            sum += review.getRating();
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return Float.parseFloat(decimalFormat.format(sum / arr.size()).replace(",", "."));
    }
}
