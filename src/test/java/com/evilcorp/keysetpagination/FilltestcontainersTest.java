package com.evilcorp.keysetpagination;

import com.evilcorp.keysetpagination.dto.PageLoadDuration;
import com.evilcorp.keysetpagination.dto.UploadCommand;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.DBLoader;
import com.evilcorp.keysetpagination.service.Walker;
import com.evilcorp.keysetpagination.service.apps.AppsKeySetByFilterWalker;
import com.evilcorp.keysetpagination.service.apps.AppsKeySetByPGShortFilterWalker;
import com.evilcorp.keysetpagination.service.apps.AppsKeySetBySimpleFilterWalker;
import com.evilcorp.keysetpagination.service.apps.AppsKeySetWalker;
import com.evilcorp.keysetpagination.service.apps.AppsPageWalker;
import com.evilcorp.keysetpagination.service.apps.AppsSliceReversWalker;
import com.evilcorp.keysetpagination.service.apps.AppsSliceWalker;
import com.evilcorp.keysetpagination.service.apps.AppsSliceWalkerWithoutOrderBy;
import com.evilcorp.keysetpagination.service.deals.DealsPageWalker;
import com.evilcorp.keysetpagination.service.deals.DealsSliceWalker;
import com.evilcorp.keysetpagination.service.deals.DealsSliceWalkerWithoutOrderBy;
import com.evilcorp.keysetpagination.testcontainers.TestcontainersInitializer;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

import static java.time.LocalTime.now;
import static java.util.function.Predicate.not;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
@EnabledIf(expression = "${keysetpagination.testcontainers.enabled}", loadContext = true)
@Rollback(value = false)
@Transactional(propagation = Propagation.NEVER)
@Slf4j
public class FilltestcontainersTest {
    @Autowired
    DBLoader loader;

    @Autowired
    DealRepository repository;

    @Autowired
    List<Walker> walkers;

    @Autowired
    private AppsKeySetByFilterWalker appsKeySetByFilterWalker;
    @Autowired
    private AppsKeySetBySimpleFilterWalker appsKeySetBySimpleFilterWalker;
    @Autowired
    private AppsKeySetWalker appsKeySetWalker;
    @Autowired
    private AppsSliceWalker appsSliceWalker;
    @Autowired
    private AppsSliceReversWalker appsSliceReversWalker;
    @Autowired
    private AppsSliceWalkerWithoutOrderBy appsSliceWalkerWithoutOrderBy;
    @Autowired
    private AppsKeySetByPGShortFilterWalker appsKeySetByPGShortFilterWalker;
    @Autowired
    private AppsPageWalker appsPageWalker;

    @SneakyThrows
    @Test
    public void fillWithData() {
        assertThat(loader).isNotNull();
        loader.init();
        assertThat(repository.count()).isGreaterThan(10);
        final UploadCommand cmd = UploadCommand.builder().pageSize(500).build();

        walkers.stream()
                .filter(not(cl -> cl.getClass().equals(DealsPageWalker.class)))
                .filter(not(cl -> cl.getClass().equals(DealsSliceWalker.class)))
                .forEach(walker -> {
                    var start = now();
                    walker.walk(cmd);
                    var end = now();
                    log.info("{} загрузил за {} секунд", walker.getClass().getSimpleName(), start.until(end, ChronoUnit.SECONDS));
                });
        walkers.stream()
                .filter(not(cl -> cl.getClass().equals(DealsPageWalker.class)))
                .filter(not(cl -> cl.getClass().equals(DealsSliceWalker.class)))
                .forEach(this::drawCart);

        drawSpringDataPage();
        drawSpringDataSlice();
        drawSpringDataPageAndSlice();
        drawSpringDataPageAndSliceWitoutOrderBy();
        drawSpringDataSliceWithReverse();
        drawSpringDataSliceWithoutOrderByAndSliceWithreverse();
        drawSpringDataSliceWithoutOrderBy();
        drawSpringDataSliceAndReverseSlice();
        drawSpringDataSimpleKeySet();
        drawSpringDataSimpleKeySetAndFastKeySet();
        drawSpringDataSimpleKeySetAndFastKeySetAndPG();
        drawSpringDataFastKeySetAndPG();
        drawSpringDataSliceAndReverseSliceAndKeySet();
    }

    @Test
    public void test() {
        assertThat(loader).isNotNull();
        loader.init();
        assertThat(repository.count()).isGreaterThan(10);
        final UploadCommand cmd = UploadCommand.builder().pageSize(500).build();

        Stream.of(appsKeySetWalker, appsSliceWalker, appsSliceReversWalker)
                .forEach(walker -> {
                    var start = now();
                    walker.walk(cmd);
                    var end = now();
                    log.info("{} загрузил за {} секунд", walker.getClass().getSimpleName(), start.until(end, ChronoUnit.SECONDS));
                });
        walkers.stream()
                .filter(not(cl -> cl.getClass().equals(DealsPageWalker.class)))
                .filter(not(cl -> cl.getClass().equals(DealsSliceWalker.class)))
                .forEach(this::drawCart);
    }

    @Test
    public void test1() {
        assertThat(loader).isNotNull();
        loader.init();
        assertThat(repository.count()).isGreaterThan(10);
        final UploadCommand cmd = UploadCommand.builder().pageSize(500).build();

        Stream.of(
                        appsPageWalker
                      //  appsKeySetWalker, appsKeySetByFilterWalker, appsKeySetBySimpleFilterWalker,
                       // appsKeySetByPGShortFilterWalker,
                        //appsSliceWalker, appsSliceReversWalker, appsSliceWalkerWithoutOrderBy
                )
                .forEach(walker -> {
                    var start = now();
                    walker.walk(cmd);
                    var end = now();
                    log.info("{} загрузил за {} секунд", walker.getClass().getSimpleName(), start.until(end, ChronoUnit.SECONDS));
                });
        walkers.stream()
                .filter(not(cl -> cl.getClass().equals(DealsPageWalker.class)))
                .filter(not(cl -> cl.getClass().equals(DealsSliceWalker.class)))
                .filter(not(cl -> cl.getClass().equals(DealsSliceWalkerWithoutOrderBy.class)))
                .forEach(this::drawCart);
    }


    @SneakyThrows
    void drawCart(Walker walker) {
        var pageLoadDurations = beanBuilderExample(new File(walker.getPath().replace("./", "")).toPath());
        var xData = pageLoadDurations.stream()
                .map(PageLoadDuration::getPage)
                .mapToDouble(Integer::doubleValue)
                .toArray();
        var yData = pageLoadDurations.stream()
                .map(PageLoadDuration::getTime)
                .mapToDouble(Long::doubleValue)
                .toArray();
        var chart = new XYChartBuilder().width(1000).height(500).title(walker.getName()).xAxisTitle("page").yAxisTitle("time (ms)").build();
        chart.addSeries("some", xData, yData);
        var styler = chart.getStyler();
        styler.setCursorBackgroundColor(Color.red)
                .setChartBackgroundColor(Color.WHITE)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false)
                .setLegendVisible(false)
                .setShowWithinAreaPoint(true)
                .setMarkerSize(0);
        styler
                .setPlotGridVerticalLinesVisible(false);


        BitmapEncoder.saveBitmapWithDPI(chart, walker.getPath(), BitmapEncoder.BitmapFormat.PNG, 500);
    }


    void drawSpringDataPage() throws IOException { //1
        var chart = new XYChartBuilder().width(1000).height(500).title("").xAxisTitle("page").yAxisTitle("time (ms)").build();
        addSerias(chart, appsPageWalker.getPath(), "Spring Data Page", Color.BLUE);
        var styler = chart.getStyler();
        styler.setCursorBackgroundColor(Color.red)
                .setChartBackgroundColor(Color.WHITE)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false)
                .setLegendVisible(true)
                .setLegendBorderColor(Color.WHITE)
                .setLegendPosition(Styler.LegendPosition.InsideN)
                .setShowWithinAreaPoint(true)
                .setMarkerSize(0);
        styler
                .setPlotGridVerticalLinesVisible(false);
        BitmapEncoder.saveBitmapWithDPI(chart, "testSpringDataPage", BitmapEncoder.BitmapFormat.PNG, 500);
    }

    void drawSpringDataSlice() throws IOException { //2
        var chart = new XYChartBuilder().width(1000).height(500).title("").xAxisTitle("page").yAxisTitle("time (ms)").build();
        addSerias(chart, appsSliceWalker.getPath(), "Spring Data Slice", Color.GRAY);
        var styler = chart.getStyler();
        styler.setCursorBackgroundColor(Color.red)
                .setChartBackgroundColor(Color.WHITE)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false)
                .setLegendVisible(true)
                .setLegendBorderColor(Color.WHITE)
                .setLegendPosition(Styler.LegendPosition.InsideN)
                .setShowWithinAreaPoint(true)
                .setMarkerSize(0);
        styler
                .setPlotGridVerticalLinesVisible(false);
        BitmapEncoder.saveBitmapWithDPI(chart, "testSpringDataSlice", BitmapEncoder.BitmapFormat.PNG, 500);
    }
    
    void drawSpringDataPageAndSlice() throws IOException { // 3
        var chart = new XYChartBuilder().width(1000).height(500).title("").xAxisTitle("page").yAxisTitle("time (ms)").build();
        addSerias(chart, appsSliceWalker.getPath(), "Spring Data Slice", Color.GRAY);
        addSerias(chart, appsPageWalker.getPath(), "Spring Data Page", Color.BLUE);
        var styler = chart.getStyler();
        styler.setCursorBackgroundColor(Color.red)
                .setChartBackgroundColor(Color.WHITE)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false)
                .setLegendVisible(true)
                .setLegendBorderColor(Color.WHITE)
                .setLegendPosition(Styler.LegendPosition.InsideN)
                .setShowWithinAreaPoint(true)
                .setMarkerSize(0);
        styler
                .setPlotGridVerticalLinesVisible(false);
        BitmapEncoder.saveBitmapWithDPI(chart, "testSpringDataPageAndSlice", BitmapEncoder.BitmapFormat.PNG, 500);
    }

    @Test
    void drawSpringDataPageAndSliceWitoutOrderBy() throws IOException { // 2
        var chart = new XYChartBuilder().width(1000).height(500).title("").xAxisTitle("page").yAxisTitle("time (ms)").build();
        addSerias(chart, appsSliceWalkerWithoutOrderBy.getPath(), "Spring Data Slice no order", Color.LIGHT_GRAY);
        addSerias(chart, appsPageWalker.getPath(), "Spring Data Page", Color.BLUE);
        var styler = chart.getStyler();
        styler.setCursorBackgroundColor(Color.red)
                .setChartBackgroundColor(Color.WHITE)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false)
                .setLegendVisible(true)
                .setLegendBorderColor(Color.WHITE)
                .setLegendPosition(Styler.LegendPosition.InsideN)
                .setShowWithinAreaPoint(true)
                .setMarkerSize(0);
        styler
                .setPlotGridVerticalLinesVisible(false);
        BitmapEncoder.saveBitmapWithDPI(chart, "testSpringDataPageAndSliceWitoutOrderBy", BitmapEncoder.BitmapFormat.PNG, 500);
    }

    
    void drawSpringDataSliceWithReverse() throws IOException { // 4
        var chart = new XYChartBuilder().width(1000).height(500).title("").xAxisTitle("page").yAxisTitle("time (ms)").build();
        addSerias(chart, appsSliceReversWalker.getPath(), "Spring Data Slice with reverse", Color.DARK_GRAY);
        var styler = chart.getStyler();
        styler.setCursorBackgroundColor(Color.red)
                .setChartBackgroundColor(Color.WHITE)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false)
                .setLegendVisible(true)
                .setLegendBorderColor(Color.WHITE)
                .setLegendPosition(Styler.LegendPosition.InsideNW)
                .setShowWithinAreaPoint(true)
                .setMarkerSize(0);
        styler
                .setPlotGridVerticalLinesVisible(false);
        BitmapEncoder.saveBitmapWithDPI(chart, "testSpringDataSliceWithReverse", BitmapEncoder.BitmapFormat.PNG, 500);
    }

    void drawSpringDataSliceWithoutOrderByAndSliceWithreverse() throws IOException { // 5
        var chart = new XYChartBuilder().width(1000).height(500).title("").xAxisTitle("page").yAxisTitle("time (ms)").build();
        addSerias(chart, appsSliceWalkerWithoutOrderBy.getPath(), "Spring Data Slice no order", Color.LIGHT_GRAY);
        addSerias(chart, appsSliceReversWalker.getPath(), "Spring Data Slice with reverse", Color.DARK_GRAY);
        var styler = chart.getStyler();
        styler.setCursorBackgroundColor(Color.red)
                .setChartBackgroundColor(Color.WHITE)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false)
                .setLegendVisible(true)
                .setLegendBorderColor(Color.WHITE)
                .setLegendPosition(Styler.LegendPosition.InsideNW)
                .setShowWithinAreaPoint(true)
                .setMarkerSize(0);
        styler
                .setPlotGridVerticalLinesVisible(false);
        BitmapEncoder.saveBitmapWithDPI(chart, "testSpringDataSliceWithoutOrderByAndSliceWithreverse", BitmapEncoder.BitmapFormat.PNG, 500);
    }

    void drawSpringDataSliceWithoutOrderBy() throws IOException { // 5
        var chart = new XYChartBuilder().width(1000).height(500).title("").xAxisTitle("page").yAxisTitle("time (ms)").build();
        addSerias(chart, appsSliceWalkerWithoutOrderBy.getPath(), "Spring Data Slice no order", Color.LIGHT_GRAY);
        var styler = chart.getStyler();
        styler.setCursorBackgroundColor(Color.red)
                .setChartBackgroundColor(Color.WHITE)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false)
                .setLegendVisible(true)
                .setLegendBorderColor(Color.WHITE)
                .setLegendPosition(Styler.LegendPosition.InsideNW)
                .setShowWithinAreaPoint(true)
                .setMarkerSize(0);
        styler
                .setPlotGridVerticalLinesVisible(false);
        BitmapEncoder.saveBitmapWithDPI(chart, "testSpringDataSliceWithoutOrderBy", BitmapEncoder.BitmapFormat.PNG, 500);
    }

    void drawSpringDataSliceAndReverseSlice() throws IOException { // 7
        var chart = new XYChartBuilder().width(1000).height(500).title("").xAxisTitle("page").yAxisTitle("time (ms)").build();
        var styler = chart.getStyler();
        addSerias(chart, appsSliceReversWalker.getPath(), "Spring Data Slice with reverse", Color.DARK_GRAY);
        addSerias(chart, appsSliceWalker.getPath(), "Spring Data Slice", Color.GRAY);
        styler.setCursorBackgroundColor(Color.red)
                .setChartBackgroundColor(Color.WHITE)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false)
                .setLegendVisible(true)
                .setLegendBorderColor(Color.WHITE)
                .setLegendPosition(Styler.LegendPosition.InsideN)
                .setShowWithinAreaPoint(true)
                .setMarkerSize(0);
        styler
                .setPlotGridVerticalLinesVisible(false);
        BitmapEncoder.saveBitmapWithDPI(chart, "testSpringDataSliceAndSliceWithReverse", BitmapEncoder.BitmapFormat.PNG, 500);
    }

    void drawSpringDataSimpleKeySet() throws IOException { // 9
        var chart = new XYChartBuilder().width(1000).height(500).title("").xAxisTitle("page").yAxisTitle("time (ms)").build();
        var styler = chart.getStyler();
        addSerias(chart, appsKeySetBySimpleFilterWalker.getPath(), "Keyset createdAt, id", Color.CYAN.brighter().brighter());
        styler.setCursorBackgroundColor(Color.red)
                .setChartBackgroundColor(Color.WHITE)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false)
                .setLegendVisible(true)
                .setLegendBorderColor(Color.WHITE)
                .setLegendPosition(Styler.LegendPosition.InsideN)
                .setShowWithinAreaPoint(true)
                .setMarkerSize(0);
        styler
                .setPlotGridVerticalLinesVisible(false);
        BitmapEncoder.saveBitmapWithDPI(chart, "testSpringDataSimpleKeySet", BitmapEncoder.BitmapFormat.PNG, 500);
    }

    void drawSpringDataSimpleKeySetAndFastKeySet() throws IOException { // 10
        var chart = new XYChartBuilder().width(1000).height(500).title("").xAxisTitle("page").yAxisTitle("time (ms)").build();
        var styler = chart.getStyler();
        addSerias(chart, appsKeySetBySimpleFilterWalker.getPath(), "Keyset createdAt, id", Color.CYAN.brighter().brighter());
        addSerias(chart, appsKeySetByFilterWalker.getPath(), "Keyset createdAt, id fast", Color.CYAN.darker());
        styler.setCursorBackgroundColor(Color.red)
                .setChartBackgroundColor(Color.WHITE)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false)
                .setLegendVisible(true)
                .setLegendBorderColor(Color.WHITE)
                .setLegendPosition(Styler.LegendPosition.InsideN)
                .setShowWithinAreaPoint(true)
                .setMarkerSize(0);
        styler
                .setPlotGridVerticalLinesVisible(false);
        BitmapEncoder.saveBitmapWithDPI(chart, "testSpringDataSimpleKeySetAndFastKeySet", BitmapEncoder.BitmapFormat.PNG, 500);
    }

    void drawSpringDataSimpleKeySetAndFastKeySetAndPG() throws IOException { // 11
        var chart = new XYChartBuilder().width(1000).height(500).title("").xAxisTitle("page").yAxisTitle("time (ms)").build();
        var styler = chart.getStyler();
        addSerias(chart, appsKeySetBySimpleFilterWalker.getPath(), "Keyset createdAt, id", Color.CYAN.brighter().brighter());
        addSerias(chart, appsKeySetByFilterWalker.getPath(), "Keyset createdAt, id fast", Color.CYAN.darker());
        addSerias(chart, appsKeySetByPGShortFilterWalker.getPath(), "Keyset row value constructor", Color.CYAN.darker().darker());
        styler.setCursorBackgroundColor(Color.red)
                .setChartBackgroundColor(Color.WHITE)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false)
                .setLegendVisible(true)
                .setLegendBorderColor(Color.WHITE)
                .setLegendPosition(Styler.LegendPosition.InsideN)
                .setShowWithinAreaPoint(true)
                .setMarkerSize(0);
        styler
                .setPlotGridVerticalLinesVisible(false);
        BitmapEncoder.saveBitmapWithDPI(chart, "testSpringDataSimpleKeySetAndFastKeySetAndPGFilter", BitmapEncoder.BitmapFormat.PNG, 500);
    }

    void drawSpringDataFastKeySetAndPG() throws IOException { // 11
        var chart = new XYChartBuilder().width(1000).height(500).title("").xAxisTitle("page").yAxisTitle("time (ms)").build();
        var styler = chart.getStyler();
        addSerias(chart, appsKeySetByFilterWalker.getPath(), "Keyset createdAt, id fast", Color.CYAN.darker());
        addSerias(chart, appsKeySetByPGShortFilterWalker.getPath(), "Keyset row value constructor", Color.CYAN.darker().darker());
        styler.setCursorBackgroundColor(Color.red)
                .setChartBackgroundColor(Color.WHITE)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false)
                .setLegendVisible(true)
                .setLegendBorderColor(Color.WHITE)
                .setLegendPosition(Styler.LegendPosition.InsideN)
                .setShowWithinAreaPoint(true)
                .setMarkerSize(0);
        styler
                .setPlotGridVerticalLinesVisible(false);
        BitmapEncoder.saveBitmapWithDPI(chart, "testSpringDataFastKeySetAndPGFilter", BitmapEncoder.BitmapFormat.PNG, 500);
    }


    void drawSpringDataSliceAndReverseSliceAndKeySet() throws IOException { // 8
        var chart = new XYChartBuilder().width(1000).height(500).title("").xAxisTitle("page").yAxisTitle("time (ms)").build();
        var styler = chart.getStyler();
        addSerias(chart, appsSliceReversWalker.getPath(), "Spring Data Slice with reverse", Color.DARK_GRAY);
        addSerias(chart, appsSliceWalker.getPath(), "Spring Data Slice", Color.GRAY);
        addSerias(chart, appsKeySetWalker.getPath(), "Keyset по id", Color.CYAN);
        styler.setCursorBackgroundColor(Color.red)
                .setChartBackgroundColor(Color.WHITE)
                .setChartTitleBoxVisible(false)
                .setPlotBorderVisible(false)
                .setLegendVisible(true)
                .setLegendBorderColor(Color.WHITE)
                .setLegendPosition(Styler.LegendPosition.InsideN)
                .setShowWithinAreaPoint(true)
                .setMarkerSize(0);
        styler
                .setPlotGridVerticalLinesVisible(false);
        BitmapEncoder.saveBitmapWithDPI(chart, "testSpringDataSliceAndSliceWithReverseAndKeySet", BitmapEncoder.BitmapFormat.PNG, 500);
    }

    private void addSerias(XYChart chart, String path, String seriasName) {
        addSerias(chart, path, seriasName, null);
    }

    private void addSerias(XYChart chart, String path, String seriasName, Color color) {
        var pageLoadDurations = beanBuilderExample(new File(path.replace("./", "")).toPath());
        var xData = pageLoadDurations.stream()
                .map(PageLoadDuration::getPage)
                .mapToDouble(Integer::doubleValue)
                .toArray();
        var yData = pageLoadDurations.stream()
                .map(PageLoadDuration::getTime)
                .mapToDouble(Long::doubleValue)
                .toArray();
        XYSeries xySeries = chart.addSeries(seriasName, xData, yData);
        if (color != null) {
            xySeries.setLineColor(color);
        }
    }


    @SneakyThrows
    public List<PageLoadDuration> beanBuilderExample(Path path) {

        try (Reader reader = Files.newBufferedReader(path)) {
            CsvToBean<PageLoadDuration> cb = new CsvToBeanBuilder<PageLoadDuration>(reader)
                    .withType(PageLoadDuration.class)
                    .withSeparator(';')
                    .build();
            return cb.parse();
        }
    }

}
