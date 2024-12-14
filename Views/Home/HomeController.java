package Views.Home;

import animatefx.animation.*;
import animatefx.util.ParallelAnimationFX;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import helpers.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.glyphfont.Glyph;

import java.math.BigInteger;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;

public class HomeController implements Initializable {


    @FXML
    private StackPane stackPane;

    @FXML
    private Label categories_number;

    @FXML
    private Label stores_number;

    @FXML
    private Pane categoriesInfo;


    @FXML
    private Pane storesInfo;

    @FXML
    private HBox chartsHbox;

    @FXML
    private PieChart pieChart;
    private NumberAxis numberAxis = new NumberAxis();

    @FXML
    private LineChart<String, Number> lineChart = new LineChart<>(new CategoryAxis(), numberAxis);

    @FXML
    private HBox trending;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categories_number.setText(String.valueOf(DbConnect.getEntityManager().createNamedQuery("Category.getNumberOfCategories", Long.class).getSingleResult()));
        stores_number.setText(String.valueOf(DbConnect.getEntityManager().createNamedQuery("Store.getNumberOfStores", Long.class).getSingleResult()));
        try {
            loadCharts();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        // Animations
        Node circle = pieChart.lookup(".chart-content");

        AnimationFX pieChartAnimation1 = new Pulse(circle);
        pieChartAnimation1.setDelay(new Duration(300));

        AnimationFX pieChartAnimation2 = new RotateIn(circle);

        ParallelAnimationFX parallelPieChartAnimation = new ParallelAnimationFX(pieChartAnimation1, pieChartAnimation2);
        parallelPieChartAnimation.play();

        AnimationFX lineChartAnimation = new ZoomIn(lineChart.lookup(".chart-plot-background").getParent());
        lineChartAnimation.setSpeed(1.1);
        lineChartAnimation.play();

        AnimationFX categoriesInfoAnimation = new FadeInRight(storesInfo);
        categoriesInfoAnimation.setSpeed(0.6);
        categoriesInfoAnimation.play();

        AnimationFX storesInfoAnimation = new FadeInLeft(categoriesInfo);
        storesInfoAnimation.setSpeed(0.6);
        storesInfoAnimation.play();

        AnimationFX trendingAnimation = new FadeInRightBig(trending);
        trendingAnimation.setSpeed(0.6);
        trendingAnimation.play();
    }

    private void loadCharts() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("visits");
        List<ChartData> chartData = new ArrayList<>();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        ////////////////////// Pie Chart

        double total = 0;
        List topRatings = DbConnect.getEntityManager().createNamedQuery("Rating.getTopRatingsASC").getResultList();
        for (Object rate : topRatings) {
            Object[] rating = (Object[]) rate;
            double rates = ((BigInteger) rating[1]).doubleValue();
            pieChartData.add(new PieChart.Data((String) rating[0], rates));
            total += rates;
        }
        for (PieChart.Data data : pieChartData) {
            double percentage = (data.getPieValue() / total) * 100;
            data.setName(data.getName() + " " + (int) percentage + "%");
            data.setPieValue(percentage);
        }

        pieChart.setLabelLineLength(20);
        pieChart.setLegendVisible(false);
        pieChart.setData(pieChartData);

        ////////////////////// Line Chart
        List visitsStats = DbConnect.getEntityManager().createNamedQuery("Visitor.getVisitsStats").getResultList();
        for (Object state : visitsStats) {
            Object[] visitState = (Object[]) state;
            chartData.add(new ChartData(convert(((java.sql.Date) visitState[0]).toLocalDate()), ((BigInteger) visitState[1]).doubleValue()));
        }
        double minVisitsNum = Double.MAX_VALUE;
        double maxVisitsNum = Double.MIN_VALUE;
        // Add Data Points to Series
        for (ChartData cd : chartData) {
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(cd.date, cd.visits);
            series.getData().add(dataPoint);
            if (cd.visits < minVisitsNum) minVisitsNum = cd.visits;
            if (cd.visits > maxVisitsNum) maxVisitsNum = cd.visits;
        }
        // Disable auto-ranging of the Y-axis
        lineChart.getYAxis().setAutoRanging(false);

        // Set the minimum and maximum values of the Y-axis
        ((NumberAxis) lineChart.getYAxis()).setLowerBound(minVisitsNum - 10 < 0 ? 0 : minVisitsNum - 10);
        ((NumberAxis) lineChart.getYAxis()).setUpperBound(maxVisitsNum + 10);
        // Add Series to Chart
        lineChart.getData().add(series);


        // Trending Stores
        List storesRatingTrend = DbConnect.getEntityManager().createNamedQuery("Store.getStoresRatingTrend").getResultList();

        for (Object rating : storesRatingTrend) {
            Object[] ratingTrend = (Object[]) rating;
            addTrendingStore((String) ratingTrend[0], Double.parseDouble((String) ratingTrend[1]));
        }
    }

    private void addTrendingStore(String storeName, double storeStats) {
        Text name = new Text(storeName);
        name.setStyle("-fx-font-size: 18px;-fx-text-fill: #575962;");
        HBox nameHBox = new HBox(name);
        nameHBox.setAlignment(Pos.CENTER);
        Label stat = new Label(String.format("%.2f", storeStats));
        stat.setStyle("-fx-font-size: 22pt;");
        // Wrap the FontAwesomeIcon inside a Glyph object
        Glyph icon = new Glyph("FontAwesome", storeStats >= 0 ? FontAwesomeIcon.ARROW_UP : FontAwesomeIcon.ARROW_DOWN);
        stat.getStyleClass().add(storeStats >= 0 ? "up" : "down");
        icon.getStyleClass().add(storeStats >= 0 ? "up" : "down");
        icon.setFontSize(30); // set the font size of the Glyph
        HBox stats = new HBox(10.0, stat, icon);
        stats.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(30, nameHBox, stats);
        vBox.setPadding(new Insets(30));
        vBox.setStyle("-fx-background-color: WHITE;-fx-background-radius: 5px; -fx-effect:  dropshadow(gaussian, rgba(69,65,78,.06), 20, 0.5, 0, 0); -fx-pref-width: 240px;");
        trending.getChildren().add(vBox);
    }

    private static class ChartData {
        public String date;
        public double visits;

        public ChartData(String date, double visits) {
            this.date = date;
            this.visits = visits;
        }

    }

    public static String convert(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }
}
