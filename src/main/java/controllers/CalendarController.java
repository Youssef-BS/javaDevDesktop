package controllers;

import javafx.scene.layout.VBox;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.concurrent.ThreadLocalRandom;


import controllers.CalendarActivity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import services.CalendrierService;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class CalendarController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    @FXML
    void backOneMonth() {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth() {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        // List of activities for a given month
        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        // Check for leap year
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
                        if (calendarActivities != null) {
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if (k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    // On ... click print all activities for given date
                    System.out.println(calendarActivities);
                });
                break;
            }
            Text text = new Text(calendarActivities.get(k).getClientName() + ", " + calendarActivities.get(k).getDate_activite().toString());
            calendarActivityBox.getChildren().add(text);
            text.setOnMouseClicked(mouseEvent -> {
                // On Text clicked
                System.out.println(text.getText());
            });
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:GRAY");
        stackPane.getChildren().add(calendarActivityBox);
    }

    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity : calendarActivities) {
            LocalDate activityDate = activity.getDate_activite();
            if (activityDate != null) {
                int activityDay = activityDate.getDayOfMonth();
                if (!calendarActivityMap.containsKey(activityDay)) {
                    calendarActivityMap.put(activityDay, List.of(activity));
                } else {
                    List<CalendarActivity> oldListByDate = calendarActivityMap.get(activityDay);
                    List<CalendarActivity> newList = new ArrayList<>(oldListByDate);
                    newList.add(activity);
                    calendarActivityMap.put(activityDay, newList);
                }
            }
        }
        return calendarActivityMap;
    }

    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        List<CalendarActivity> calendarActivities = new ArrayList<>();

        // Fetch data from the database
        List<CalendarActivity> databaseData;
        CalendrierService cr = new CalendrierService();
        try {
            databaseData = cr.recuperer();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately, log or throw
            return Collections.emptyMap();
        }

        // Set year and month
        int year = 2024;
        int month = 3;

        List<CalendarActivity> activitiesForMonth = databaseData.stream()
                .filter(activity -> {
                    // Generate a random day within the specified year and month
                    int randomDay = ThreadLocalRandom.current().nextInt(1, YearMonth.of(year, month).lengthOfMonth() + 1);
                    ZonedDateTime activityDate = ZonedDateTime.of(year, month, randomDay, 0, 0, 0, 0, ZoneId.systemDefault());

                    // Filter activities randomly, e.g., consider only 50% of them
                    return Math.random() < 0.5;
                })
                .collect(Collectors.toList());

        Random random = new Random();
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        System.out.println(databaseData);
        for (int i = 0; i < databaseData.size(); i++) {
            if (i < activitiesForMonth.size()) {
                // Use data from the database
                calendarActivities.add(activitiesForMonth.get(i));
            } else {
                // Set a random day within the specified year and month
                int randomDay = random.nextInt(daysInMonth) + 1;
                LocalDate randomDate = LocalDate.of(year, month, randomDay);

                ZonedDateTime time = ZonedDateTime.of(randomDate, LocalTime.of(16, 0), dateFocus.getZone());
                calendarActivities.add(new CalendarActivity(databaseData.get(i).getTypeActivite(), databaseData.get(i).getDescription(), randomDate, "Random Heure Fermeture"));
            }
        }

        return createCalendarMap(calendarActivities);
    }
}
