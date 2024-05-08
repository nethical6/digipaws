package nethical.digipaws.utils.data;

public class SurvivalModeData {
  private String startTime = "00:00";
  private String endTime = "00:00";
  private String reason;
  private boolean isEnabled = false;
  private String packageName = "null";
  private String viewId = "null";
  private Double lastLatitude = 0.0;
  private Double lastLongitude = 0.0;

  public SurvivalModeData(boolean isEnabled, String endTime, String packageName, String viewId) {
    this.isEnabled = isEnabled;
    this.endTime = endTime;
    this.packageName = packageName;
    this.viewId = viewId;
  }

  public String getStartTime() {
    return startTime;
  }

  public Double getLongitude() {
    return lastLongitude;
  }

  public Double getLatitude() {
    return lastLatitude;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getViewIdName() {
    return viewId;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public void setLatitude(Double lastLatitude) {
    this.lastLatitude = lastLatitude;
  }

  public void setLongitude(Double lastLongitude) {
    this.lastLongitude = lastLongitude;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public boolean isEnabled() {
    return isEnabled;
  }

  public void setEnabled(boolean enabled) {
    isEnabled = enabled;
  }
}
