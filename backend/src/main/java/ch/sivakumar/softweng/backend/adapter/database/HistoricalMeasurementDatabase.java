package ch.sivakumar.softweng.backend.adapter.database;

import ch.sivakumar.softweng.backend.application.ports.out.HistoricalMeasurementRepository;
import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.types.PastTimeRange;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.dsl.Flux;
import com.influxdb.query.dsl.functions.restriction.Restrictions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class HistoricalMeasurementDatabase implements HistoricalMeasurementRepository {

    private static final String MEASUREMENT_NAME = "mqtt_consumer";

    private final InfluxDBClient influxDBClient;
    private final String bucket;

    public HistoricalMeasurementDatabase(InfluxDBClient influxDBClient,
                                         @Value("${influxdb.bucket}") String bucket) {
        this.influxDBClient = influxDBClient;
        this.bucket = bucket;
    }

    @Override
    public List<Measurement> findMeasurements(PastTimeRange timeRange) {
        if (timeRange == null) {
            return Collections.emptyList();
        }

        Flux query = Flux.from(bucket)
                .range(timeRange.start(), timeRange.end())
                .filter(Restrictions.and(
                        Restrictions.measurement().equal(MEASUREMENT_NAME),
                        Restrictions.or(
                                Restrictions.field().equal(MeasurementRow.FIELD_TEMPERATURE),
                                Restrictions.field().equal(MeasurementRow.FIELD_HUMIDITY))))
                .pivot(List.of("_time"), List.of("_field"), "_value")
                .groupBy(List.of())
                .sort(List.of("_time"));

        return influxDBClient.getQueryApi()
                .query(query.toString(), MeasurementRow.class)
                .stream()
                .map(MeasurementRow::toDomain)
                .flatMap(Optional::stream)
                .toList();
    }
}
