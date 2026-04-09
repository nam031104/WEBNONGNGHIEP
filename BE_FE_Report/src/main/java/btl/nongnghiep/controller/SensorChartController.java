package btl.nongnghiep.controller;

import btl.nongnghiep.dto.SensorChartViewDto;
import btl.nongnghiep.repository.SensorDataRepository;
import btl.nongnghiep.service.SensorChartService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/sensor/chart")
public class SensorChartController {

    private final SensorChartService sensorChartService;
    private final SensorDataRepository sensorDataRepository;

    public SensorChartController(SensorChartService sensorChartService,
                                 SensorDataRepository sensorDataRepository) {
        this.sensorChartService = sensorChartService;
        this.sensorDataRepository = sensorDataRepository;
    }

    // ===================== DAY =====================
    @GetMapping("/day")
    public String showDailyChart(
            @RequestParam(required = false) String sensorId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        List<String> sensorIds = sensorDataRepository.findAllSensorIds();
        if (sensorIds.isEmpty()) {
            model.addAttribute("error", "Chưa có dữ liệu cảm biến nào trong hệ thống.");
            return "sensor/no-data";
        }

        String resolvedSensorId = resolveId(sensorId, sensorIds);
        LocalDate selectedDate = (date != null) ? date : LocalDate.now();

        SensorChartViewDto chartView =
                sensorChartService.buildDailyChart(resolvedSensorId, selectedDate);

        bindCommonAttributes(model, chartView, resolvedSensorId, sensorIds);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("pageTitle", "Sensor Daily Chart");

        return "sensor/chart-day";
    }

    // ===================== WEEK =====================
    @GetMapping("/week")
    public String showWeeklyChart(
            @RequestParam(required = false) String sensorId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        List<String> sensorIds = sensorDataRepository.findAllSensorIds();
        if (sensorIds.isEmpty()) {
            model.addAttribute("error", "Chưa có dữ liệu cảm biến nào trong hệ thống.");
            return "sensor/no-data";
        }

        String resolvedSensorId = resolveId(sensorId, sensorIds);
        LocalDate selectedDate = (date != null) ? date : LocalDate.now();

        SensorChartViewDto chartView =
                sensorChartService.buildWeeklyChart(resolvedSensorId, selectedDate);

        bindCommonAttributes(model, chartView, resolvedSensorId, sensorIds);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("pageTitle", "Sensor Weekly Chart");

        return "sensor/chart-week";
    }

    // ===================== MONTH =====================
    @GetMapping("/month")
    public String showMonthlyChart(
            @RequestParam(required = false) String sensorId,
            @RequestParam(required = false) String month,
            Model model) {

        List<String> sensorIds = sensorDataRepository.findAllSensorIds();
        if (sensorIds.isEmpty()) {
            model.addAttribute("error", "Chưa có dữ liệu cảm biến nào trong hệ thống.");
            return "sensor/no-data";
        }

        String resolvedSensorId = resolveId(sensorId, sensorIds);

        YearMonth selectedMonth;
        try {
            selectedMonth = (month == null || month.isBlank())
                    ? YearMonth.now()
                    : YearMonth.parse(month);
        } catch (Exception e) {
            selectedMonth = YearMonth.now();
        }

        SensorChartViewDto chartView =
                sensorChartService.buildMonthlyChart(resolvedSensorId, selectedMonth);

        bindCommonAttributes(model, chartView, resolvedSensorId, sensorIds);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("pageTitle", "Sensor Monthly Chart");
        model.addAttribute("daysInMonth", selectedMonth.lengthOfMonth());

        return "sensor/chart-month";
    }

    // ===================== HELPERS =====================

    private String resolveId(String sensorId, List<String> sensorIds) {
        if (sensorId != null && !sensorId.isBlank() && sensorIds.contains(sensorId)) {
            return sensorId;
        }
        return sensorIds.get(0);
    }

    private void bindCommonAttributes(Model model,
                                  SensorChartViewDto chartView,
                                  String sensorId,
                                  List<String> sensorIds) {

        model.addAttribute("sensorId", sensorId);
        model.addAttribute("sensorIds", sensorIds);
        model.addAttribute("chartView", chartView);
        model.addAttribute("labels", chartView.getLabels());
        model.addAttribute("values", chartView.getValues());

        model.addAttribute("valueSuffix",
                chartView.getUnit() == null || chartView.getUnit().isBlank()
                        ? ""
                        : " " + chartView.getUnit());
    }
}