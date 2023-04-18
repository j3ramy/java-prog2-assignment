package util;

import mvc.AppModel;
import util.data.AudienceReview;
import util.data.Medium;

import java.text.DecimalFormat;
import java.time.Year;
import java.util.*;

public class Algorithms {

    /**
     * Sorts medium by rating
     *
     * @param mediums mediums that should be sorted
     * @param appModel AppModel reference to get the reviews
     *
     * @BigO: O(n)
     * **/
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

    /**
     * Sorts medium by release year with help of counting sort algorithm
     *
     * @param mediums mediums that should be sorted
     *
     * @BigO: O(n)
     * **/
    public static void sortMediumsByReleaseYear(ArrayList<Medium> mediums) {
        //Find the maximum release year
        int maxReleaseYear = Year.now().getValue();

        //Initialize counting array
        int[] count = new int[maxReleaseYear + 1];
        for (Medium medium : mediums) {
            count[medium.getReleaseYear()]++;
        }

        //Calculate count array
        for (int i = 1; i <= maxReleaseYear; i++) {
            count[i] += count[i - 1];
        }

        //Create sorted array
        Medium[] sorted = new Medium[mediums.size()];
        for (int i = mediums.size() - 1; i >= 0; i--) {
            Medium medium = mediums.get(i);
            int index = count[medium.getReleaseYear()] - 1;
            sorted[index] = medium;
            count[medium.getReleaseYear()]--;
        }

        //Copy sorted array into the original list by clear it and add all
        mediums.clear();
        mediums.addAll(Arrays.asList(sorted));
    }

    /**
     * Sorts audience reviews by rating with help of counting sort algorithm
     *
     * @param audienceReviews audience reviews that should be sorted
     *
     * @BigO: O(n)
     * **/
    public static void sortAudienceReviewsByRating(ArrayList<AudienceReview> audienceReviews) {
        int audienceCount = audienceReviews.size();
        int countingSortScale = 10; //Steps between ratings: [0.0, 0.9]
        float maxRating = getMaxRating(audienceReviews);

        //Create count list and count values
        ArrayList<AudienceReview>[] count = new ArrayList[(int) (maxRating * countingSortScale) + 1]; //By multiplying the rating by 10, the decimals are removed and will be used for the counting array
        for (int i = 0; i < count.length; i++) {
            count[i] = new ArrayList<>();
        }

        //Get index by rating and add every review to count array sorted
        for (AudienceReview audienceReview : audienceReviews) {
            int index = (int) (audienceReview.getRating() * countingSortScale);
            count[index].add(audienceReview);
        }

        //Create output list and fill it with default null values
        ArrayList<AudienceReview> output = new ArrayList<>(audienceCount);
        for (int i = 0; i < audienceCount; i++) {
            output.add(null);
        }

        //Add every sorted review to output list
        int index = 0;
        for (ArrayList<AudienceReview> reviews : count) {
            for (AudienceReview review : reviews) {
                output.set(index++, review);
            }
        }

        //Copy output array into original list
        for (int i = 0; i < audienceCount; i++) {
            audienceReviews.set(i, output.get(i));
        }
    }

    /**
     * Returns the maximum rating of all audience reviews
     *
     * @param audienceReviews audience reviews that should be compared
     *
     * @return maximum rating as float
     *
     * @BigO: O(n)
     * **/
    public static float getMaxRating(ArrayList<AudienceReview> audienceReviews){
        float maxFloat = 0f;

        for(AudienceReview review : audienceReviews){
            if(review.getRating() > maxFloat)
                maxFloat = review.getRating();
        }

        return maxFloat;
    }

    /**
     * Returns the average rating of all audience reviews
     *
     * @param audienceReviews audience reviews that should be compared
     *
     * @return average rating as float
     *
     * @BigO: O(n)
     * **/
    public static float getAverageRating(ArrayList<AudienceReview> audienceReviews){
        float sum = 0f;

        for(AudienceReview review : audienceReviews){
            sum += review.getRating();
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return Float.parseFloat(decimalFormat.format(sum / audienceReviews.size()).replace(",", "."));
    }
}
