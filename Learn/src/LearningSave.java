import java.io.Serializable;

public class LearningSave implements Serializable {

  private String champ1;

  public String getChamp1() {
    return this.champ1;
  }

  public void setChamp1(final String champ1) {
    this.champ1 = champ1;
  }

  @Override
  public String toString() {
    return "MonBean [champ1=" + this.champ1 + "]";
  }
}