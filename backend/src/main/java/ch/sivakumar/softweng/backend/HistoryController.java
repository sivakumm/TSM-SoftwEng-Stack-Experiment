package ch.sivakumar.softweng.backend;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxTable;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@AllArgsConstructor
public class HistoryController {

    private final InfluxDBClient influxDBClient;

    @GetMapping
    public List<DataPoint> getLastTwoMinutes() {
        String fluxQuery = """
                from(bucket: "study_project")
                  |> range(start: -2m)
                  |> filter(fn: (r) => r["_measurement"] == "mqtt_consumer")
                  |> filter(fn: (r) => r["_field"] == "temperature")
                  |> yield(name: "results")""";
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(fluxQuery);

        return tables.stream()
                .flatMap(table -> table.getRecords().stream()
                        .map(data -> new DataPoint(data.getTime(), data.getValue())))
                .toList();
    }
}
