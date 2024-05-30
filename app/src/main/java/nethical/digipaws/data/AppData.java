package nethical.digipaws.data;

import android.graphics.drawable.Drawable;

public class AppData {
    private String label;
    private String packageName;
    private Drawable icon;
    private boolean isSelected = false;

    public AppData(String label, Drawable icon,String packageName) {
        this.label = label;
        this.icon = icon;
        this.packageName = packageName;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean getChecked() {
        return this.isSelected;
    }

    public void setIsChecked(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
