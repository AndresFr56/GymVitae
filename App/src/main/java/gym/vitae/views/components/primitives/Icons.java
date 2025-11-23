package gym.vitae.views.components.primitives;

public enum Icons {
  ADD("icons/add.svg"),
  CLEAR("icons/clear.svg"),
  CLOSE("icons/close.svg"),
  COLOR("icons/color.svg"),
  COPY("icons/copy.svg"),
  EYE("icons/eye.svg"),
  FAVORITE("icons/favorite.svg"),
  FAVORITE_FILLED("icons/favorite_filled.svg"),
  GIT("icons/git.svg"),
  MENU("icons/menu.svg"),
  REDO("icons/redo.svg"),
  REFRESH("icons/refresh.svg"),
  SEARCH("icons/search.svg"),
  UNDO("icons/undo.svg"),
  CUSTOMER("icons/customer.svg"),
  EXPENSE("icons/expense.svg"),
  INCOME("icons/income.svg"),
  AVATAR_MALE("icons/avatar_male.svg"),
  AVATAR_FEMALE("icons/avatar_female.svg"),
  PROFIT("icons/profit.svg"),
  SETTINGS("icons/modules/settings.svg"),
  ABOUT("icons/modules/about.svg"),
  CALENDAR("icons/modules/calendar.svg"),
  CHART("icons/modules/chart.svg"),
  CHAT("icons/modules/chat.svg"),
  COMPONENTS("icons/modules/components.svg"),
  DASHBOARD("icons/modules/dashboard.svg"),
  EMAIL("icons/modules/email.svg"),
  FORMS("icons/modules/forms.svg"),
  LOGOUT("icons/modules/logout.svg"),
  PACK("icons/modules/pack.svg"),
  PAGE("icons/modules/page.svg"),
  PLUGIN("icons/modules/plugin.svg"),
  EMPLOYEE("icons/modules/employee.svg"),
  UI("icons/modules/ui.svg");

  private final String path;

  Icons(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  @Override
  public String toString() {
    return path;
  }
}
