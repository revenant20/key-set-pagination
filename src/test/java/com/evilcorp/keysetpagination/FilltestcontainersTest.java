package com.evilcorp.keysetpagination;

import com.evilcorp.keysetpagination.dto.PageLoadDuration;
import com.evilcorp.keysetpagination.dto.UploadCommand;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.DBLoader;
import com.evilcorp.keysetpagination.service.Walker;
import com.evilcorp.keysetpagination.service.apps.AppsKeySetByPGShortFilterWalker;
import com.evilcorp.keysetpagination.service.deals.DealsPageWalker;
import com.evilcorp.keysetpagination.service.deals.DealsSliceWalker;
import com.evilcorp.keysetpagination.testcontainers.TestcontainersInitializer;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChartBuilder;
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
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
    AppsKeySetByPGShortFilterWalker appsKeySetByPGShortFilterWalker;

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
                .setShowWithinAreaPoint(true);
        styler
                .setPlotGridVerticalLinesVisible(false);


        BitmapEncoder.saveBitmapWithDPI(chart, walker.getPath(), BitmapEncoder.BitmapFormat.PNG, 500);
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
