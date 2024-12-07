package utilities;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class BarChartValueFormatter extends ValueFormatter {
    private final String[] labels;

    public BarChartValueFormatter(String[] labels) {
        this.labels = labels;
    }

    @Override
    public String getBarLabel(BarEntry barEntry) {
        int index = (int) barEntry.getX();
        if (index >= 0 && index < labels.length) {
            return labels[index];
        }
        return super.getBarLabel(barEntry);
    }
}
