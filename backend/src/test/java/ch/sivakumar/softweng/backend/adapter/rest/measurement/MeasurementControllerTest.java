package ch.sivakumar.softweng.backend.adapter.rest.measurement;

import ch.sivakumar.softweng.backend.adapter.rest.measurement.dto.MeasurementResponse;
import ch.sivakumar.softweng.backend.adapter.rest.measurement.mapper.MeasurementMapper;
import ch.sivakumar.softweng.backend.application.ports.in.FetchHistoricalMeasurement;
import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.exception.ApplicationException;
import ch.sivakumar.softweng.backend.domain.types.OpenTimeRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class MeasurementControllerTest {

    private static final String VALID_START_TIMESTAMP = "2026-10-15T10:00:00Z";
    private static final String VALID_END_TIMESTAMP = "2026-10-16T10:00:00Z";

    private FetchHistoricalMeasurement fetchHistoricalMeasurementMock;
    private MeasurementMapper measurementMapperMock;

    private MeasurementController controller;

    private ArgumentCaptor<OpenTimeRange> openTimeRangeArgumentCaptor;

    @BeforeEach
    void setUp() {
        openTimeRangeArgumentCaptor = ArgumentCaptor.forClass(OpenTimeRange.class);

        fetchHistoricalMeasurementMock = mock();
        measurementMapperMock = mock();

        controller = new MeasurementController(fetchHistoricalMeasurementMock, measurementMapperMock);
    }

    @Test
    void shouldThrowWhenGivenStartTimestampIsInvalid() {
        assertThrows(ApplicationException.class, () -> controller.getMeasurements("abc", VALID_END_TIMESTAMP));
    }

    @Test
    void shouldThrowWhenGivenEndTimestampIsInvalid() {
        assertThrows(ApplicationException.class, () -> controller.getMeasurements(VALID_START_TIMESTAMP, "abc"));
    }

    @Test
    void shouldFetchMeasurementsDataForGivenTimeRange() {
        Measurement measurementMock = mock();
        MeasurementResponse measurementResponseMock = mock();
        when(fetchHistoricalMeasurementMock.execute(any())).thenReturn(List.of(measurementMock));
        when(measurementMapperMock.toMeasurementResponses(anyList())).thenReturn(List.of(measurementResponseMock));

        List<MeasurementResponse> result = controller.getMeasurements(VALID_START_TIMESTAMP, VALID_END_TIMESTAMP);

        assertEquals(1, result.size());
        assertEquals(measurementResponseMock, result.getFirst());
        verify(fetchHistoricalMeasurementMock).execute(openTimeRangeArgumentCaptor.capture());
        assertEquals(VALID_START_TIMESTAMP, openTimeRangeArgumentCaptor.getValue().start().toString());
        assertEquals(VALID_END_TIMESTAMP, openTimeRangeArgumentCaptor.getValue().end().toString());
        verify(measurementMapperMock).toMeasurementResponses(List.of(measurementMock));
    }

    @Test
    void shouldFetchMeasurementsDataForFromGivenRangeStartWithoutRangeEnd() {
        Measurement measurementMock = mock();
        MeasurementResponse measurementResponseMock = mock();
        when(fetchHistoricalMeasurementMock.execute(any())).thenReturn(List.of(measurementMock));
        when(measurementMapperMock.toMeasurementResponses(anyList())).thenReturn(List.of(measurementResponseMock));

        List<MeasurementResponse> result = controller.getMeasurements(VALID_START_TIMESTAMP, null);

        assertEquals(1, result.size());
        assertEquals(measurementResponseMock, result.getFirst());
        verify(fetchHistoricalMeasurementMock).execute(openTimeRangeArgumentCaptor.capture());
        assertEquals(VALID_START_TIMESTAMP, openTimeRangeArgumentCaptor.getValue().start().toString());
        assertNull(openTimeRangeArgumentCaptor.getValue().end());
        verify(measurementMapperMock).toMeasurementResponses(List.of(measurementMock));
    }

    @Test
    void shouldFetchMeasurementsDataForTillGivenRangeEndWithoutRangeStart() {
        Measurement measurementMock = mock();
        MeasurementResponse measurementResponseMock = mock();
        when(fetchHistoricalMeasurementMock.execute(any())).thenReturn(List.of(measurementMock));
        when(measurementMapperMock.toMeasurementResponses(anyList())).thenReturn(List.of(measurementResponseMock));

        List<MeasurementResponse> result = controller.getMeasurements(null, VALID_END_TIMESTAMP);

        assertEquals(1, result.size());
        assertEquals(measurementResponseMock, result.getFirst());
        verify(fetchHistoricalMeasurementMock).execute(openTimeRangeArgumentCaptor.capture());
        assertNull(openTimeRangeArgumentCaptor.getValue().start());
        assertEquals(VALID_END_TIMESTAMP, openTimeRangeArgumentCaptor.getValue().end().toString());
        verify(measurementMapperMock).toMeasurementResponses(List.of(measurementMock));
    }

    @Test
    void shouldFetchMeasurementsDataForMaximumAllowedTimeRangeWhenNoTimeRangeIsProvided() {
        Measurement measurementMock = mock();
        MeasurementResponse measurementResponseMock = mock();
        when(fetchHistoricalMeasurementMock.execute(any())).thenReturn(List.of(measurementMock));
        when(measurementMapperMock.toMeasurementResponses(anyList())).thenReturn(List.of(measurementResponseMock));

        List<MeasurementResponse> result = controller.getMeasurements(null, null);

        assertEquals(1, result.size());
        assertEquals(measurementResponseMock, result.getFirst());
        verify(fetchHistoricalMeasurementMock).execute(openTimeRangeArgumentCaptor.capture());
        assertNull(openTimeRangeArgumentCaptor.getValue().start());
        assertNull(openTimeRangeArgumentCaptor.getValue().end());
        verify(measurementMapperMock).toMeasurementResponses(List.of(measurementMock));
    }

    @Test
    void shouldFetchMeasurementsDataForMaximumAllowedTimeRangeWhenNoTimeRangeIsProvidedAsEmptyString() {
        Measurement measurementMock = mock();
        MeasurementResponse measurementResponseMock = mock();
        when(fetchHistoricalMeasurementMock.execute(any())).thenReturn(List.of(measurementMock));
        when(measurementMapperMock.toMeasurementResponses(anyList())).thenReturn(List.of(measurementResponseMock));

        List<MeasurementResponse> result = controller.getMeasurements("", " ");

        assertEquals(1, result.size());
        assertEquals(measurementResponseMock, result.getFirst());
        verify(fetchHistoricalMeasurementMock).execute(openTimeRangeArgumentCaptor.capture());
        assertNull(openTimeRangeArgumentCaptor.getValue().start());
        assertNull(openTimeRangeArgumentCaptor.getValue().end());
        verify(measurementMapperMock).toMeasurementResponses(List.of(measurementMock));
    }
}
