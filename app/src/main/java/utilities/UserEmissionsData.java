package utilities;

import static utilities.Constants.EMISSIONS_AMOUNT_INDEX;
import static utilities.Constants.EMISSION_TYPE_INDEX;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import customDataStructures.EmissionNode;
import customDataStructures.EmissionNodeCollection;


/**
 * The UserEmissionsData class manages and processes user emissions data.
 *
 * @see EmissionNodeCollection
 * @see EmissionNode
 * @see Database
 */
public class UserEmissionsData {

    private final String userId;
    private final boolean interpolate;

    private HashMap<String, Object> data;
    private List<String> sortedDates;

    private final DataReadyListener listener;
    private final SimpleDateFormat simpleDateFormat;

    /**
     * Interface for handling database events.
     */
    public interface DataReadyListener {
        void onDataReady();
        void onError(String errorMessage);
    }

    /**
     * This class fetches user emissions data and activity data.
     */
    public UserEmissionsData(String userId, boolean interpolate, DataReadyListener listener) {
        this.userId = userId;
        this.listener = listener;
        this.interpolate = interpolate;

        // Format the data in the form yyyy-mm-dd
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Fetch data when the object is created
        fetchData();
    }

    /**
     * Fetches user data from the database and initializes the local data structures.
     *
     * @throws AssertionError If the fetched data is null.
     * @see Database#mReadDataOnce(String, Database.OnGetDataListener)
     */
    private void fetchData() {
        Database db = new Database();
        String path = "user data/" + userId + "/calendar";

        db.mReadDataOnce(path, new Database.OnGetDataListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    data = (HashMap<String, Object>) dataSnapshot.getValue();

                    assert data != null;
                    sortedDates = new ArrayList<>(data.keySet());

                    // Sort dates in descending order i.e. 1/1/2024 comes before 1/1/2023
                    sortedDates.sort((date1, date2) -> daysBetweenDates(date1, date2));

                    if (listener != null) {
                        listener.onDataReady();
                    }
                } else {
                    if (listener != null) {
                        listener.onError("No data available for user.");
                    }
                }
            }
            @Override
            public void onFailed(DatabaseError databaseError) {
                if (listener != null) {
                    listener.onError("Error fetching data: " + databaseError.getMessage());
                }
            }
        });
    }

    /**
     * Checks whether the user has any logged activity data.
     *
     * @return Returns true if the sortedDates list is not null and contains more than one entry.
     */
    public boolean userHasData() {
        // We check for > 1 entry because of the default entry 0000-00-00
        return sortedDates != null && sortedDates.size() > 1;
    }


    /**
     * Retrieves the first time an activity was logged by the user.
     *
     * @return The date of the first logged activity as a string in the yyyy-mm-dd format,
     * or null if no activity data is available.
     */
    public String getFirstLoggedDay() {
        if (!userHasData()) {
            return null;
        }

        /*
            Get the second last item in sorted dates as it is sorted in descending order and
            last item is the default date 0000-00-00
        */
        return sortedDates.get(sortedDates.size() - 2);
    }

    /**
     * Calculates the total number of days since the user first logged an activity.
     *
     * @return the total number of days since the user first logged an activity,
     * or 0 if no activity data is available.
     *
     */
    public int totalDaysSinceFirstLoggedActivity() {
        if (!userHasData()) {
            return 0;
        }

        /*
            Calculate and return the number of days between the first logged activity date
            and the current date.
        */
        return daysBetweenDates(getFirstLoggedDay(), getCurrentDate());
    }

    /**
     * Retrieves the total emissions in kilograms of CO2e for the user over the given
     * number of days.
     *
     * @param days The number of days over which the emissions should be calculated.
     * @return the total emissions in kilograms of CO2e over the given number of days, or 0 
     * if no activity data is available.
     * @see #getTotalEmissions(int)
     * @see #generateEmissionsData(int)
     */
    public float getUserEmissions(int days) {
        if (!userHasData()) {
            return 0;
        }
        return getTotalEmissions(days);
    }

    /**
     * Retrieves the emissions data for the user in kilograms of CO2e over the given
     * number of days.
     * <p>
     * This method returns a list of {@link EmissionNodeCollection} objects, where each
     * collection contains emissions data for the given days.
     * </p>
     *
     * @param days The number of days over which the emissions data should be retrieved.
     * @return a list of {@link EmissionNodeCollection} objects, or null if no data is available.
     * @see #generateEmissionsData(int)
     */
    public List<EmissionNodeCollection> getUserEmissionsData(int days) {
        if (data == null)
            return null;
        return generateEmissionsData(days);
    }

    /**
     * Retrieves the total emissions in kilograms of CO2e by summing up all the data
     * based the number of given days.
     *
     * @param days The number of days over which the emissions should be summed.
     * @return the total emissions in kilograms of CO2e for the specified days, or 0 if no data is available.
     * @see #generateEmissionsData(int)
     */
    private float getTotalEmissions(int days) {
        List<EmissionNodeCollection> emissionsData = generateEmissionsData(days);
        float totalEmissions = 0;

        if (emissionsData == null || emissionsData.isEmpty()) {
            return totalEmissions;
        }

        // Sum all the emission data
        for (EmissionNodeCollection emissionNodeCollection : emissionsData) {
            for (EmissionNode node : emissionNodeCollection.getData()) {
                totalEmissions += node.getEmissionsAmount();
            }
        }
        return totalEmissions;
    }

    /**
     * Retrieves the current date in the format yyyy-mm-dd.
     *
     * @return A string representing the current date in the format yyyy-mm-dd.
     */
    private String getCurrentDate() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();

        // Format and return the current date in the format yyyy-mm-dd
        return calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + (calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Retrieves the last date before the given date when an activity was logged
     * in the yyyy-mm-dd format.
     *
     * @param date The date to search before, formatted as yyyy-mm-dd (pre-supposed).
     * @return the last date before the given date when an activity was logged,
     * or null if no valid date is found.
     * @throws RuntimeException if an error occurs while parsing the dates.
     */
    private String getLastLoggedDate(String date) {
        if (date == null) {
            return null;
        }

        for (String sortedDate : sortedDates) {
            try {
                // Check if a new date comes before the 'date'
                if (Objects.requireNonNull(simpleDateFormat.parse(sortedDate)).compareTo(simpleDateFormat.parse(date)) < 0) {
                    return sortedDate;
                }
            } catch (ParseException e) {
                throw new RuntimeException("An error occurred while parsing through the sortedDate list." + e);
            }
        }
        return null;
    }

    /**
     * Retrieves the date before the given date.
     *
     * @param date The date from which the previous day should be calculated,
     *             formatted as yyyy-mm-dd (pre-supposed).
     * @return The date before the given date, in the yyyy-mm-dd format.
     */
    private String getTheDayBefore(String date) {
        String[] dateSegments = date.split("-");

        int[] ymdInts = new int[3];
        for (int i = 0; i < dateSegments.length; i++) {
            ymdInts[i] = Integer.parseInt(dateSegments[i]);
        }

        Calendar calendar = Calendar.getInstance();

        /*
            note here that months in Calendar class go from 0-11, but we have them set from 1-12.
            So subtract 1 from ours.
         */
        calendar.set(ymdInts[0], ymdInts[1] - 1, ymdInts[2]);

        calendar.add(Calendar.DAY_OF_MONTH, -1);  // Stores the previous day

        /*
            Add one to the month since months in Calendar class go from 0-11 and return the day
            before in the yyyy-mm-dd format.
         */
        return calendar.get(Calendar.YEAR) +"-"+(calendar.get(Calendar.MONTH)+1)+"-"
                +calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Calculates the number of days between two dates.
     *
     * @param date1 The first date, formatted as yyyy-mm-dd (pre-supposed).
     * @param date2 The second date, formatted as yyyy-mm-dd (pre-supposed).
     * @return the number of days between the two dates
     * or -1 if any of the dates is invalid or null.
     * @throws RuntimeException if an error occurs while parsing the dates.
     */
    private int daysBetweenDates(String date1, String date2) {
        if (date1 == null || date2 == null) {
            return -1;
        }

        try {
            Date parsedDate1 = simpleDateFormat.parse(date1);
            Date parsedDate2 = simpleDateFormat.parse(date2);

            // Make sure the parsed dates are not null
            assert parsedDate1 != null;
            assert parsedDate2 != null;

            // Calculate the time difference between the two dates in milliseconds
            long diffInMillis = parsedDate2.getTime() - parsedDate1.getTime();

            // Convert the time difference in milliseconds to days
            return (int) (diffInMillis / (1000 * 60 * 60 * 24));
        } catch(ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates emissions data for the user, either using raw data or interpolated data.
     *
     * @param days The number of days for which the emissions data should be generated.
     * @return A list of {@link EmissionNodeCollection} representing the emissions data for the
     *         given number of days, or null if there is no user data.
     * @see #getRawData(int)
     * @see #getInterpolatedData(int)
     */
    private List<EmissionNodeCollection> generateEmissionsData(int days) {
        if (!userHasData())
            return null;
        return interpolate? getInterpolatedData(days) : getRawData(days);
    }

    /**
     * Retrieves the raw emissions data for the given number of days.
     *
     * @param days The number of days for which interpolated emissions data should be calculated
     *             and retrieved.
     * @return A list of {@link EmissionNodeCollection} objects representing the raw emissions data.
     * @see #dataToEmissionsNodesCollection(String, Object)
     */
    private List<EmissionNodeCollection> getRawData(int days) {
        // Get the current date in the format yyyy-mm-dd
        String current = getCurrentDate();

        List<EmissionNodeCollection> chartData = new ArrayList<>();

        for (int i = 0; i < days; i++) {
            // Check the date exists in the data.
            if (data.containsKey(current)) {
                // Convert the data into an EmissionsNodesCollection before storing the list
                EmissionNodeCollection collection = dataToEmissionsNodesCollection(current, data.get(current));
                chartData.add(0, collection);
            }
            // Go back one day
            current = getTheDayBefore(current);

            // Check the search has gone past the first day an activity was logged.
            if (daysBetweenDates(getFirstLoggedDay(), current) < 0) {
                break;
            }
        }
        return chartData;
    }

    /**
     * Retrieves interpolated emissions data for the given number of days.
     *
     * @param days The number of days for which interpolated emissions data should be calculated
     *            and retrieved.
     * @return  A list of {@link EmissionNodeCollection} representing interpolated emissions data.
     */
    private List<EmissionNodeCollection> getInterpolatedData(int days) {
        // Get the current date in the format yyyy-mm-dd
        String current = getCurrentDate();

        String x1 = current;

        List<EmissionNodeCollection> chartData = new ArrayList<>();

        Set<String> addedDates = new HashSet<>();

        for (int i = sortedDates.size() - 1; i >= 1; i--) {
            if (data.containsKey(current) && !addedDates.contains(current)) {
                EmissionNodeCollection collection = dataToEmissionsNodesCollection(current, data.get(current));
                chartData.add(0, collection);
                addedDates.add(current);
                current = getTheDayBefore(current);
                x1 = getLastLoggedDate(current);
            } else if (chartData.isEmpty()) {
                x1 = getLastLoggedDate(current);
                EmissionNodeCollection collection = dataToEmissionsNodesCollection(current, data.get(x1));
                chartData.add(0, collection);
                addedDates.add(current);
                current = getTheDayBefore(current);
                x1 = getLastLoggedDate(current);

                i++;  //Ignore the iteration as new data was created.
            } else {
                EmissionNodeCollection dataForX1 = dataToEmissionsNodesCollection(x1, data.get(x1));

                assert dataForX1 != null;
                EmissionNodeCollection interpolatedCollection = interpolateData(dataForX1, chartData.get(0), current);
                chartData.add(0, interpolatedCollection);
                addedDates.add(current);

                current = getTheDayBefore(current);
                x1 = getLastLoggedDate(current);

                i++; // Ignore the iteration as new data was created.
            }

            if (chartData.size() == days) {
                break;
            }
        }

        return chartData;
    }

    /**
     * Interpolates emissions data between two existing data points for a specific date.
     *
     * @param data1 The emissions data in form of a {@link EmissionNodeCollection}
     *             for the first known date (x1).
     * @param data2 The emissions data in form of a {@link EmissionNodeCollection}
     *              for the second known date (x2).
     * @param newDate The date for which emissions data needs to be interpolated (x1 <= x <= x2) .
     * @return An {@link EmissionNodeCollection} containing the interpolated emissions data.
     */
    private EmissionNodeCollection interpolateData(EmissionNodeCollection data1,
                                                   EmissionNodeCollection data2, String newDate) {
        // Make sure the newDate is between the two dates.
        if (daysBetweenDates(data1.getDate(), data2.getDate()) < 0
                && daysBetweenDates(data1.getDate(), newDate) <= 0
                && daysBetweenDates(newDate, data2.getDate()) >= 0) {
            return null;
        }

        // EmissionsNodeCollection representing the interpolated emissions data. (y)
        EmissionNodeCollection interpolatedCollection = new EmissionNodeCollection(newDate);

        // Store the data to be used for interpolation from the two EmissionsNodeCollections
        ArrayList<EmissionNode> nodes1 = data1.getData();
        ArrayList<EmissionNode> nodes2 = data2.getData();


        Map<String, Float> emissionMap1 = new HashMap<>();
        Map<String, Float> emissionMap2 = new HashMap<>();

        // Store the individual emissions type alongside their amount
        for (EmissionNode node : nodes1) {
            emissionMap1.put(node.getEmissionType(), node.getEmissionsAmount());
        }

        for (EmissionNode node : nodes2) {
            emissionMap2.put(node.getEmissionType(), node.getEmissionsAmount());
        }

        Set<String> allEmissionTypes = new HashSet<>();

        // Store emissions type from both Nodes
        allEmissionTypes.addAll(emissionMap1.keySet());
        allEmissionTypes.addAll(emissionMap2.keySet());

        // Create the interpolated data
        for (String emissionType : allEmissionTypes) {
            /*
                Determine the Emission amount from each Category and default 0 if that category
                doesn't exist in the other EmissionsNodeCollection
             */
            float value1 = emissionMap1.getOrDefault(emissionType, 0f);
            float value2 = emissionMap2.getOrDefault(emissionType, 0f);

            // Represents x - x1 in the interpolation formula
            int daysBetweenDate1AndNewDate = daysBetweenDates(data1.getDate(), newDate);

            // Represents x2 - x1 in the interpolation formula
            int daysBetweenDate1AndDate2 = daysBetweenDates(data1.getDate(), data2.getDate());

            // Apply the linear interpolation formula to estimate the emissions value
            float interpolatedEmissionsValue = value1 + ((value2 - value1)
                    * daysBetweenDate1AndNewDate) / daysBetweenDate1AndDate2;

            // Create an Emission node to represent the interpolated data for a single category
            EmissionNode interpolatedNode = new EmissionNode(emissionType, interpolatedEmissionsValue);

            // Add all interpolated categories
            interpolatedCollection.addData(interpolatedNode);
        }

        return interpolatedCollection;
    }

    /**
     * Converts raw Firebase data for a given date into a {@link EmissionNodeCollection}.
     *
     * @param date The date for the Firebase data.
     * @param data The Firebase data to be converted.
     * @return An {@link EmissionNodeCollection} representing the emissions data for the given date.
     * @see EmissionNodeCollection#addData(EmissionNode)
     */
    private EmissionNodeCollection dataToEmissionsNodesCollection(String date, Object data) {
        if (data == null) {
            return null;
        }

        EmissionNodeCollection collection = new EmissionNodeCollection(date);

        List<List<Object>> dataList = (List<List<Object>>) data;

        // Breakup the data into different emission categories and sum the amount for each category.
        for (List<Object> row : dataList) {
            String emissionType = (String) row.get(EMISSION_TYPE_INDEX);
            float emissionAmount = Float.parseFloat(row.get(EMISSIONS_AMOUNT_INDEX).toString());

            EmissionNode node = new EmissionNode(emissionType, emissionAmount);

            /*
                Either sums into an existing Node
                or creates new node if the categories doesn't already exists
             */
            collection.addData(node);
        }

        return collection;
    }
}
