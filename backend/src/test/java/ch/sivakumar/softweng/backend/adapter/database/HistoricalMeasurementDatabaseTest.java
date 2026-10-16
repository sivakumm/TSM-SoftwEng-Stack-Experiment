package ch.sivakumar.softweng.backend.adapter.database;

import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.types.PastTimeRange;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class HistoricalMeasurementDatabaseTest {

    private static final String BUCKET = "test-bucket";

    private InfluxDBClient influxDBClientMock;

    private QueryApi queryApiMock;

    private HistoricalMeasurementDatabase database;

    private ArgumentCaptor<String> queryArgumentCaptor;

    private static MeasurementRow createRow(Instant time, Double temperature, Double humidity) {
        return MeasurementRow.builder()
                .time(time)
                .temperature(temperature)
                .humidity(humidity)
                .build();
    }

    private static PastTimeRange lastDay() {
        Instant now = Instant.now();
        return new PastTimeRange(now.minus(1, ChronoUnit.DAYS), now);
    }

    @BeforeEach
    void setup() {
        queryArgumentCaptor = ArgumentCaptor.forClass(String.class);

        influxDBClientMock = mock();
        queryApiMock = mock();
        when(influxDBClientMock.getQueryApi()).thenReturn(queryApiMock);

        database = new HistoricalMeasurementDatabase(influxDBClientMock, BUCKET);
    }

    @Test
    void shouldReturnEmptyListWithoutQueryingWhenTimeRangeIsNull() {
        List<Measurement> result = database.findMeasurements(null);

        assertTrue(result.isEmpty());
        verifyNoInteractions(influxDBClientMock, queryApiMock);
    }

    @Test
    void shouldQueryConfiguredBucketForGivenTimeRange() {
        PastTimeRange timeRange = new PastTimeRange(
                Instant.parse("2026-01-01T10:00:00Z"),
                Instant.parse("2026-01-02T10:00:00Z"));
        when(queryApiMock.query(anyString(), eq(MeasurementRow.class))).thenReturn(List.of());

        database.findMeasurements(timeRange);

        verify(queryApiMock).query(queryArgumentCaptor.capture(), eq(MeasurementRow.class));
        String query = queryArgumentCaptor.getValue();
        assertTrue(query.contains("from(bucket:\"" + BUCKET + "\")"));
        assertTrue(query.contains("start:2026-01-01T10:00:00"));
        assertTrue(query.contains("stop:2026-01-02T10:00:00"));
    }

    @Test
    void shouldFilterForTemperatureAndHumidityOfMqttConsumerMeasurement() {
        when(queryApiMock.query(anyString(), eq(MeasurementRow.class))).thenReturn(List.of());

        database.findMeasurements(lastDay());

        verify(queryApiMock).query(queryArgumentCaptor.capture(), eq(MeasurementRow.class));
        String query = queryArgumentCaptor.getValue();
        assertTrue(query.contains("r[\"_measurement\"] == \"mqtt_consumer\""));
        assertTrue(query.contains("r[\"_field\"] == \"temperature\""));
        assertTrue(query.contains("r[\"_field\"] == \"humidity\""));
    }

    @Test
    void shouldReturnEmptyListWhenNoRowsAreFound() {
        when(queryApiMock.query(anyString(), eq(MeasurementRow.class))).thenReturn(List.of());

        List<Measurement> result = database.findMeasurements(lastDay());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldMapRowsToMeasurements() {
        Instant firstTime = Instant.now().minus(2, ChronoUnit.HOURS);
        Instant secondTime = Instant.now().minus(1, ChronoUnit.HOURS);
        when(queryApiMock.query(anyString(), eq(MeasurementRow.class))).thenReturn(List.of(
                createRow(firstTime, 21.5, 45.0),
                createRow(secondTime, 22.0, 50.5)));

        List<Measurement> result = database.findMeasurements(lastDay());

        assertEquals(2, result.size());
        assertEquals(firstTime, result.getFirst().timestamp());
        assertEquals(21.5, result.getFirst().temperature().value());
        assertEquals(45.0, result.getFirst().humidity().value());
        assertEquals(secondTime, result.get(1).timestamp());
        assertEquals(22.0, result.get(1).temperature().value());
        assertEquals(50.5, result.get(1).humidity().value());
    }

    @Test
    void shouldMapRowsWithOnlyOneValueGiven() {
        Instant time = Instant.now().minus(1, ChronoUnit.HOURS);
        when(queryApiMock.query(anyString(), eq(MeasurementRow.class))).thenReturn(List.of(
                createRow(time, 21.5, null),
                createRow(time, null, 45.0)));

        List<Measurement> result = database.findMeasurements(lastDay());

        assertEquals(2, result.size());
        assertEquals(21.5, result.getFirst().temperature().value());
        assertNull(result.getFirst().humidity());
        assertNull(result.get(1).temperature());
        assertEquals(45.0, result.get(1).humidity().value());
    }

    @Test
    void shouldSkipInvalidRows() {
        Instant time = Instant.now().minus(1, ChronoUnit.HOURS);
        when(queryApiMock.query(anyString(), eq(MeasurementRow.class))).thenReturn(List.of(
                createRow(null, 21.5, 45.0),
                createRow(time, null, null),
                createRow(time, 22.0, 50.5)));

        List<Measurement> result = database.findMeasurements(lastDay());

        assertEquals(1, result.size());
        assertEquals(22.0, result.getFirst().temperature().value());
        assertEquals(50.5, result.getFirst().humidity().value());
    }
}
